package com.shark.feifei.query;


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

    public static <T> T selectByField(Connection connection, String fieldName,Long fieldValue,Class entityClass) {
        Query query = getSelectByFieldQuery(connection, fieldName, fieldValue, entityClass);

        return (T) query.singleQuery();
    }

    public static <T> List<T> selectsByField(Connection connection, String fieldName, Long fieldValue, Class entityClass) {
        Query query = getSelectByFieldQuery(connection, fieldName, fieldValue, entityClass);

        return (List<T>) query.query();
    }

    private static Query getSelectByFieldQuery(Connection connection, String fieldName, Long fieldValue, Class entityClass) {
        Object condition = ClassUtil.newInstance(entityClass);
        EntityInfo entityInfo = FeiFeiBootStrap.get().<FeiFeiContainer>container().getEntityInfoGet().get(entityClass);
        String methodSetName = EntityUtil.methodSet(fieldName);
        Method methodSet = entityInfo.getMethodInfo().get(methodSetName);
        try {
            methodSet.invoke(condition, fieldValue);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return EntityQuery.create()
                .addOption(QueryOptions.AUTO_FROM)
                .setConnection(connection)
                .setResultType(entityClass)
                .select((Entity) condition);
    }
}
