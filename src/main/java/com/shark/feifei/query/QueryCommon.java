package com.shark.feifei.query;


import com.shark.feifei.Exception.SqlException;
import com.shark.feifei.FeiFeiBootStrap;
import com.shark.feifei.annoation.ForeignKey;
import com.shark.feifei.container.FeiFeiContainer;
import com.shark.feifei.query.consts.QueryOptions;
import com.shark.feifei.query.consts.Sql;
import com.shark.feifei.query.entity.Entity;
import com.shark.feifei.query.entity.EntityInfo;
import com.shark.feifei.query.entity.EntityUtil;
import com.shark.feifei.query.query.EntityQuery;
import com.shark.feifei.query.query.Query;
import com.shark.feifei.query.query.StringQuery;
import com.shark.util.classes.ClassUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Execute sql query indirectly through calling static method
 *
 * @Author: Shark Chili
 * @Date: 2018/10/23 0023
 */
public class QueryCommon {

    /**
     * Select last insert id.
     *
     * @param connection {@link Connection}
     * @return id
     */
    public static Long selectLastInsertId(Connection connection) {
        Query<Long> query = StringQuery.<Long>create(Sql.SELECT_LAST_INSERT_ID).setNestedQuery(true);
        return query.setConnection(connection).singleQuery();
    }

    /**
     * Set @update_id=0.
     *
     * @param connection {@link Connection}
     */
    public static void setUpdateId0(Connection connection) {
        Query setUpdateIdEqual0 = StringQuery.create(Sql.SET_UPDATE_ID_EQUAL_0).setNestedQuery(true);
        setUpdateIdEqual0.queryData().getParameters().add(Sql.DIGIT_0);
        setUpdateIdEqual0.setConnection(connection).query();
    }

    /**
     * Select @update_id.
     *
     * @param connection {@link Connection}
     * @return id
     */
    public static Long selectUpdateId(Connection connection) {
        Query<Long> selectUpdateId = StringQuery.<Long>create(Sql.SELECT_UPDATE_ID).setNestedQuery(true);
        return selectUpdateId.setConnection(connection).singleQuery();
    }

    /**
     * Execute query according to specified field
     * @param connection connection
     * @param fieldName field name from entity
     * @param fieldValue field value from db
     * @param entityClass class of entity
     * @param <T> class of entity
     * @return Result querying
     */
    public static <T> List<T> selectByField(Connection connection, String fieldName, Long fieldValue, Class<T> entityClass) {
        Object condition = ClassUtil.newInstance(entityClass);
        EntityInfo entityInfo = FeiFeiBootStrap.get().<FeiFeiContainer>container().getEntityInfoGet().get(entityClass);
        String methodSetName = EntityUtil.methodSet(fieldName);
        Method methodSet = entityInfo.getMethodInfo().get(methodSetName);
        if (methodSet==null){
            throw new SqlException("Entity %s has no set method fro field %s",entityClass,fieldName);
        }
        try {
            methodSet.invoke(condition, fieldValue);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return EntityQuery.<T>create()
                .addOption(QueryOptions.AUTO_FROM)
                .setConnection(connection)
                .setResultType(entityClass)
                .select((Entity) condition)
                .query();
    }

    /**
     * Execute query according to specified primary key from entity
     * @param connection connection
     * @param fieldValue field value from db
     * @param entityClass class of entity
     * @param <T> class of entity
     * @return Result querying
     */
    public static <T> T selectByPrimaryKey(Connection connection,Long fieldValue,Class<T> entityClass){
        Object condition = EntityUtil.getPrimaryKeyOrIdObject(entityClass,fieldValue.intValue());
        return EntityQuery.<T>create()
                .addOption(QueryOptions.AUTO_FROM)
                .setConnection(connection)
                .setResultType(entityClass)
                .select((Entity) condition)
                .singleQuery();
    }
}
