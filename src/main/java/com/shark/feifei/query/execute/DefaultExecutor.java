package com.shark.feifei.query.execute;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.shark.feifei.Exception.QueryException;
import com.shark.feifei.Exception.SqlException;
import com.shark.feifei.FeiFeiBootStrap;
import com.shark.feifei.annoation.ForeignKey;
import com.shark.feifei.annoation.Mask;
import com.shark.feifei.annoation.OneToMany;
import com.shark.feifei.consts.Status;
import com.shark.feifei.consts.StatusCode;
import com.shark.feifei.container.FeiFeiContainer;
import com.shark.feifei.query.QueryCommon;
import com.shark.feifei.query.config.QueryConfig;
import com.shark.feifei.query.consts.OperationType;
import com.shark.feifei.query.consts.QueryOptions;
import com.shark.feifei.query.consts.Sql;
import com.shark.feifei.query.entity.Entity;
import com.shark.feifei.query.entity.EntityInfo;
import com.shark.feifei.query.entity.EntityUtil;
import com.shark.feifei.query.query.AbstractQuery;
import com.shark.feifei.query.query.EntityQuery;
import com.shark.feifei.query.query.Query;
import com.shark.util.classes.ClassUtil;
import com.shark.util.util.MaskUtil;
import com.shark.util.util.MathUtil;
import com.shark.util.util.NumberUtil;
import com.shark.util.util.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static com.shark.util.util.LogUtil.LOGGER;


/**
 * Execute sql with mysql
 *
 * @Author: Shark Chili
 * @Date: 2018/10/15 0015
 */
public class DefaultExecutor extends AbstractSqlExecutor {

    @Override
    public <T> List<T> query(Query query) throws SQLException {
        List<T> resultList = Lists.newArrayList();

        String sql = query.queryData().sql();
        List parameters = query.queryData().getParameters();
        PreparedStatement statement = query.queryData().connection().prepareStatement(sql);

        for (int i = 0; i < parameters.size(); i++) {
            statement.setObject(i + 1, parameters.get(i));
        }

        // 反射创建对象
        try {
            // 封装结果集到对象
            ResultSet resultSet = statement.executeQuery();

            // 1.判断resultType是否为空 2.若空则取出ResultSet中的表名 3.返回map形式记录
            EntityInfo resultEntityInfo = null;
            if (query.queryData().getResultType() != null) {
                resultEntityInfo = FeiFeiBootStrap.get().<FeiFeiContainer>container().getEntityInfoGet().get(query.queryData().getResultType());
            } else {
                String tableName = resultSet.getMetaData().getTableName(3);
                QueryConfig queryConfig = FeiFeiBootStrap.get().<FeiFeiContainer>container().queryConfig();
                tableName = queryConfig.getNameStyle().tableToEntity(tableName, queryConfig.getIgnore());
                resultEntityInfo = FeiFeiBootStrap.get().<FeiFeiContainer>container().getEntityInfoGet().get(tableName);
            }

            // 填充字段到对象
            while (resultSet.next()) if (resultEntityInfo != null) {
                Object resultObject = resultEntityInfo.getClassInfo().newInstance();
                // set值
                for (Map.Entry<String, Field> entry : resultEntityInfo.getFieldInfo().entrySet()) {
                    String fieldName = entry.getKey();
                    Field field = entry.getValue();
                    Object fieldValue = null;
                    // 结果集可能没有这个字段
                    try {
                        String columnName = resultEntityInfo.getColumn(fieldName);
                        fieldValue = resultSet.getObject(columnName);

                        // 获取column value,可能是外键
                        ForeignKey foreignKey = field.getAnnotation(ForeignKey.class);
                        if (foreignKey != null&&fieldValue!=null) {
                            // 查询外键表,赋值到字段
                            fieldValue = QueryCommon.selectByPrimaryKey(query.queryData().connection(), Long.valueOf(fieldValue.toString()), foreignKey.entity());
                        }

                        // 可能是1对多关系
                        OneToMany oneToMany = field.getAnnotation(OneToMany.class);
                        if (oneToMany != null) {
                            // 获取主键值
                            Field primaryKeyField = EntityUtil.getPrimaryKeyField(resultEntityInfo);
                            String primaryKeyFieldColumn = resultEntityInfo.getColumn(primaryKeyField.getName());
                            // 查询many表
                            fieldValue = QueryCommon.selectByField(query.queryData().connection(), fieldName, Long.valueOf((String) resultSet.getObject(primaryKeyFieldColumn)), oneToMany.entity());
                        }

                        // 可能是数字对应enum类
                        if (StatusCode.class.isAssignableFrom(field.getType())) {
                            int code = Integer.valueOf(fieldValue.toString());
                            StatusCode firstStatusCode= (StatusCode) ClassUtil.getFirstEnum(field.getType());
                            fieldValue = firstStatusCode.getStatus(code);
                        }

                        // 可能是字符串对应enum类
                        if (Status.class.isAssignableFrom(field.getType())) {
                            String name = fieldValue.toString();
                            Status firstStatus= (Status) ClassUtil.getFirstEnum(field.getType());
                            fieldValue = firstStatus.getStatus(name);
                        }

                        // 可能是数字对应的List<Enum>
                        Mask mask = field.getAnnotation(Mask.class);
                        if (mask != null && List.class.isAssignableFrom(field.getType())) {
                            List<Integer> trueBit= MaskUtil.split(Integer.valueOf(fieldValue.toString()));
                            List<StatusCode> statusCodes=Lists.newArrayList();
                            Class<StatusCode> enumClass= ClassUtil.getGenericClass(field);
                            StatusCode firstStatusCode=ClassUtil.getFirstEnum(enumClass);
                            for (Integer code : trueBit) {
                                statusCodes.add(firstStatusCode.getStatus(code));
                            }
                            fieldValue=statusCodes;
                        }

                    } catch (SQLException e) {
                        continue;
                    }
                    String methodSetName = EntityUtil.methodSet(fieldName);
                    Method methodSet = resultEntityInfo.getMethodInfo().get(methodSetName);
                    if (methodSet == null) {
                        throw new QueryException("class {} haven`t set method", resultEntityInfo.getClassInfo());
                    }
                    try {
                        methodSet.invoke(resultObject, fieldValue);
                    }catch (IllegalArgumentException e) {
                        // 判断是不是数字类型
                        Class numberClass=methodSet.getParameterTypes()[0];
                        if (Number.class.isAssignableFrom(numberClass)){
                            try {
                                // 调用参数为字符串的构造函数
                                fieldValue=numberClass.getDeclaredConstructor(String.class).newInstance(fieldValue.toString());
                                methodSet.invoke(resultObject, fieldValue);
                            } catch (NoSuchMethodException ex) {
                                ex.printStackTrace();
                            }catch (InvocationTargetException e1){
                                throw new SqlException("field: %s",fieldName);
                            }
                        }else {
                            throw new SqlException(e.getMessage()+", field name: %s",fieldName);
                        }
                    }
                }
                resultList.add((T) resultObject);
            } else {
                // 没有类型默认使用Map<string,Object>存储数据
                Map<String, Object> recordMap = Maps.newHashMap();
                storageResultToMap(resultSet, recordMap);
                resultList.add((T) recordMap);
            }
        } catch (InstantiationException | IllegalAccessException | SQLException | InvocationTargetException e) {
            LOGGER(DefaultExecutor.class).error("sql: {}, parameters: {}", query.queryData().sql(), query.queryData().getParameters());
            e.printStackTrace();
        }
        return resultList;
    }

    /**
     * Storage result to a map.
     *
     * @param resultSet query result
     * @param recordMap story result
     */
    private void storageResultToMap(ResultSet resultSet, Map<String, Object> recordMap) {
        try {
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int count = rsmd.getColumnCount();
            for (int i = 1; i <= count; i++) {
                String columnName = rsmd.getColumnName(i);
                Object columnVaule = resultSet.getObject(i);
                // skip null value
                if (StringUtil.isEmpty(columnName) || columnVaule == null) continue;
                if (Number.class.isAssignableFrom(columnVaule.getClass())) {
                    recordMap.put(columnName, NumberUtil.convertToLong((Number) columnVaule));
                } else {
                    recordMap.put(columnName, columnVaule);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> List<T> update(Query query) throws SQLException {
        List<T> resultList = Lists.newArrayList();

        List parameters = query.queryData().getParameters();

        switch (query.queryData().getOperationType()) {
            case BATCH_SQL: {
                // 关闭自动提交
                query.queryData().connection().setAutoCommit(false);

                String sql = query.queryData().sql();
                PreparedStatement statement = query.queryData().connection().prepareStatement(sql);
                Map<Integer, Integer> indexes = StringUtil.indexOf(sql, Sql.QUESTION);
                if (indexes == null || indexes.isEmpty()) {
                    throw new QueryException("batch sql have no placeholder ?");
                }
                // 含有字段个数
                int fieldNumber = indexes.keySet().size();
                // 添加参数
                for (int i = 0; i < query.queryData().getParameters().size(); i++) {
                    int myIndex = (i + 1) % fieldNumber;
                    if (myIndex == 0) myIndex = fieldNumber;
                    Object fieldValue = query.queryData().getParameters().get(i);
                    statement.setObject(myIndex, fieldValue);
                    if ((i + 1) % fieldNumber == 0) {
                        statement.addBatch();
                    }
                    // 每 BATCH_NUMBER_MAX 执行一次
                    if (i > 0 && (i + 1) % ExecuteConst.BATCH_NUMBER_MAX == 0) {
                        statement.executeBatch();
                    }
                }
                // 执行剩下的sql
                int[] countUpdates = statement.executeBatch();
                statement.close();
                query.queryData().connection().commit();
                query.queryData().connection().setAutoCommit(true);
                resultList.add((T) MathUtil.sum(countUpdates));
                break;
            }
            case MULTI_SQL: {
                List<String> SQLs = query.queryData().multiSql();
                long updateCount = 0;
                // 记录已经取出的参数
                int cursor = 0;
                for (String sql : SQLs) {
                    PreparedStatement statement = query.queryData().connection().prepareStatement(sql);
                    Map<Integer, Integer> indexes = StringUtil.indexOf(sql, Sql.QUESTION);
                    if (indexes == null || indexes.isEmpty()) {
                        throw new QueryException("batch sql have no placeholder ?");
                    }
                    // 含有字段个数
                    int fieldNumber = indexes.keySet().size();
                    // 添加参数
                    for (int i = 0; i < fieldNumber; i++, cursor++) {
                        int myIndex = (i + 1) % fieldNumber;
                        if (myIndex == 0) myIndex = fieldNumber;
                        Object object = query.queryData().getParameters().get(cursor);
                        statement.setObject(myIndex, object);
                    }
                    updateCount += statement.executeUpdate();
                }
                resultList.add((T) Long.valueOf(updateCount));
                break;
            }
            case SINGLE_SQL: {
                beforeQueryOptionDo(query);
                String sql = query.queryData().sql();
                PreparedStatement statement = query.queryData().connection().prepareStatement(sql);
                for (int i = 0; i < parameters.size(); i++) {
                    statement.setObject(i + 1, parameters.get(i));
                }
                long updateCount = statement.executeUpdate();
                boolean returnOther = afterQueryOptionDo(query, resultList);
                // 都不需要返回时,只返回更新记录数
                if (!returnOther) {
                    resultList.add((T) Long.valueOf(updateCount));
                }
                break;
            }
        }
        return resultList;
    }

    @Override
    public void set(Query query) throws SQLException {
        switch (query.queryData().getOperationType()) {
            case SINGLE_SQL: {
                PreparedStatement statement = query.queryData().connection().prepareStatement(query.queryData().sql());
                List parameters = query.queryData().getParameters();
                for (int i = 0; i < parameters.size(); i++) {
                    Object parameter = parameters.get(i);
                    statement.setObject(i + 1, parameter);
                }
                statement.execute();
                break;
            }
            default: {
                throw new QueryException("Not support other operation instead of single sql,{}", query.queryData().sql());
            }
        }
    }

    /**
     * Operation after query according to QueryOption.
     *
     * @param query      {@link Query}
     * @param resultList a list of record
     * @param <T>        type of record
     * @return whether return query record or not
     */
    private <T> boolean afterQueryOptionDo(Query query, List<T> resultList) {
        boolean returnQueryRecord = false;
        switch (query.queryData().getSqlType()) {
            case UPDATE: {
                boolean returnId = query.queryData().getOptions().contains(QueryOptions.RETURN_ID);
                boolean returnRecord = QueryOptions.isReturnRecord(query.queryData().getOptions());

                if (!returnRecord & returnId) {
                    returnQueryRecord = true;
                    Long updateId = QueryCommon.selectUpdateId(query.queryData().connection());
                    resultList.add((T) updateId);
                }

                if (!returnId & returnRecord) {
                    returnQueryRecord = true;
                    // 先查出id
                    Long updateId = QueryCommon.selectUpdateId(query.queryData().connection());

                    returnRecord(query, resultList, updateId);
                }

                break;
            }
            case INSERT: {
                boolean returnId = query.queryData().getOptions().contains(QueryOptions.RETURN_ID);
                boolean returnRecord = QueryOptions.isReturnRecord(query.queryData().getOptions());

                if (!returnRecord & returnId) {
                    returnQueryRecord = true;
                    Long insertId = QueryCommon.selectLastInsertId(query.queryData().connection());
                    resultList.add((T) insertId);
                }

                if (!returnId & returnRecord) {
                    returnQueryRecord = true;
                    // 先查询 last insert id.
                    Long insertId = QueryCommon.selectLastInsertId(query.queryData().connection());

                    returnRecord(query, resultList, insertId);
                }
                break;
            }
        }
        return returnQueryRecord;
    }

    private <T> void returnRecord(Query query, List<T> resultList, Long insertId) {
        Entity singleSqlEntity = query.queryData().getSqlEntity();
        Entity idEntity = EntityUtil.getPrimaryKeyOrIdObject(singleSqlEntity.getClass(), insertId.intValue());

        // 根据idEntity查询
        Query<T> queryIdEntity = EntityQuery.<T>create()
                .setNestedQuery(true)
                .addOption(QueryOptions.AUTO_FROM)
                .setResultType((Class<T>) singleSqlEntity.getClass()).select(idEntity);
        T updateEntity = queryIdEntity.setConnection(query.queryData().connection()).singleQuery();

        resultList.add(updateEntity);
    }

    /**
     * Do something before query according to query option.
     *
     * @param query {@link Query}
     */
    private void beforeQueryOptionDo(Query query) {
        boolean returnId = QueryOptions.isReturnId(query.queryData().getOptions());
        if (returnId && query.queryData().getSqlType() == AbstractQuery.SqlType.UPDATE && query.queryData().getOperationType() == OperationType.SINGLE_SQL) {
            // 执行 SET @update_id:=0;
            QueryCommon.setUpdateId0(query.queryData().connection());
        }
    }
}
