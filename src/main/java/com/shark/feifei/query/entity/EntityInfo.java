package com.shark.feifei.query.entity;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import com.shark.feifei.annoation.Column;
import com.shark.feifei.annoation.ForeignKey;
import com.shark.feifei.annoation.Table;
import com.shark.feifei.query.config.Ignore;
import com.shark.feifei.query.consts.NameStyle;
import com.shark.feifei.query.consts.Sql;
import com.shark.util.util.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Contain entity field,method,class info.
 *
 * @Author: Shark Chili
 * @Date: 2018/10/12 0012
 */
public class EntityInfo {

    /**
     * key: field name,value: field
     */
    Map<String, Field> fieldInfo;
    /**
     * key: method name,value: method
     */
    Map<String, Method> methodInfo;
    /**
     * key: field name,value: column name
     */
    BiMap<String, String> entityMapTable;
    /**
     * key: field,value: table.column
     */
    BiMap<String, String> tableColumn;
    /**
     * class
     */
    Class classInfo;
    /**
     * table name
     */
    String tableName;

    public EntityInfo(Class entity, NameStyle nameStyle, Ignore ignore) {
        this.classInfo = entity;
        fieldInfo = Maps.newHashMap();
        methodInfo = Maps.newHashMap();
        entityMapTable = HashBiMap.create();
        tableColumn = HashBiMap.create();
        init(nameStyle, ignore);
    }

    /**
     * Story entity info
     */
    private void init(NameStyle nameStyle, Ignore ignore) {
        for (Field declaredField : classInfo.getDeclaredFields()) {
            fieldInfo.put(declaredField.getName(), declaredField);
        }
        for (Method declaredMethod : classInfo.getDeclaredMethods()) {
            methodInfo.put(declaredMethod.getName(), declaredMethod);
        }
        // 存入column和field映射信息
        for (String fieldName : fieldInfo.keySet()) {
            Field field = fieldInfo.get(fieldName);
            String column;
            // 先判断有无注解字段
            Column columnAnnotation = field.getAnnotation(Column.class);
            ForeignKey foreignKeyAnnotation = field.getAnnotation(ForeignKey.class);
            if (foreignKeyAnnotation != null) {
                column = foreignKeyAnnotation.column();
            } else if (columnAnnotation != null) {
                column = columnAnnotation.name();
            } else {
                column = nameStyle.fieldToColumn(fieldName, ignore);
            }
            entityMapTable.put(fieldName, column);
        }
        // 存入表名
        String entity = StringUtil.classLowerHump(classInfo);
        // 先判断有无注解表名
        Object tableObj = classInfo.getAnnotation(Table.class);
        if (tableObj == null) {
            tableName = nameStyle.entityToTable(entity, ignore);
        } else {
            tableName = ((Table) tableObj).name();
        }
        // 存入表名.列明
        for (String field : entityMapTable.keySet()) {
            tableColumn.put(field, tableName + Sql.POINT + entityMapTable.get(field));
        }
    }

    public Map<String, Field> getFieldInfo() {
        return fieldInfo;
    }

    public Map<String, Method> getMethodInfo() {
        return methodInfo;
    }

    public Class getClassInfo() {
        return classInfo;
    }

    public String getColumn(String field) {
        return entityMapTable.get(field);
    }

    public String getField(String column) {
        return entityMapTable.inverse().get(column);
    }

    public BiMap<String, String> getTableColumn() {
        return tableColumn;
    }

    public BiMap<String, String> getEntityMapTable() {
        return entityMapTable;
    }

    /**
     * Get table.column according to field name or column name.
     *
     * @param field Column name
     * @return table.column name
     */
    public String getTableColumn(String field) {
        String result = tableColumn.get(field);
        if (result == null) {
            result = tableColumn.get(getColumn(field));
        }
        return result;
    }

    public String getTableName() {
        return tableName;
    }

    @Override
    public String toString() {
        return "EntityInfo{" +
                "fieldInfo=" + fieldInfo +
                ", methodInfo=" + methodInfo +
                ", entityMapTable=" + entityMapTable +
                ", tableColumn=" + tableColumn +
                ", classInfo=" + classInfo +
                ", tableName='" + tableName + '\'' +
                '}';
    }
}
