package com.shark.feifei.query.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.shark.feifei.Exception.QueryException;
import com.shark.feifei.FeiFeiBootStrap;
import com.shark.feifei.annoation.PrimaryKey;
import com.shark.feifei.annoation.UnionPrimaryKey;
import com.shark.feifei.container.FeiFeiContainer;
import com.shark.feifei.query.condition.Condition;
import com.shark.feifei.query.condition.EntityCondition;
import com.shark.feifei.query.consts.Sql;
import com.shark.util.classes.ClassUtil;
import com.shark.util.util.StringUtil;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: Shark Chili
 * @Date: 2018/10/12 0012
 */
public class EntityUtil {

	/**
	 * Update entity with old entity.
	 * @param oldEntity {@link Entity}
	 * @param newEntity {@link Entity}
	 */
	public static void updateEntity(Entity oldEntity,Entity newEntity){
		EntityInfo info= FeiFeiBootStrap.get().<FeiFeiContainer>container().getEntityInfoGet().get(oldEntity);
		if (info!=null){
			for (String fieldName : info.getFieldInfo().keySet()) {
				String methodGetName=methodGet(fieldName);
				Method methodGet=info.getMethodInfo().get(methodGetName);
				try {
					Object oldValue=methodGet.invoke(oldEntity);
					Object newValue=methodGet.invoke(newEntity);
					if (oldValue==null||!oldValue.equals(newValue)){
						String methodSetName=methodSet(fieldName);
						Method methodSet=info.getMethodInfo().get(methodSetName);
						methodSet.invoke(oldEntity,newValue);
					}
				} catch (IllegalAccessException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Generate object according to field map.
	 *
	 * @param fields fields that Object to generated contain.
	 * @param c the class of Entity to generated.
	 * @return {@link Entity}
	 */
	public static Object generateEntity(Map<String, Object> fields, Class c) {
		EntityInfo entityInfo = FeiFeiBootStrap.get().<FeiFeiContainer>container().getEntityInfoGet().get(c);
		Object obj = ClassUtil.newInstance(c);
		boolean notNull = false;
		for (String fieldName : fields.keySet()) {
			String methodName = methodSet(fieldName);
			Object fieldValue = fields.get(fieldName);
			Method methodSet = entityInfo.getMethodInfo().get(methodName);
			try {
				methodSet.invoke(obj, fieldValue);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
			notNull = true;
		}
		if (notNull) {
			return obj;
		} else {
			return null;
		}
	}

	/**
	 * Get a clone of entity that contain fields that its value is`t {@link ObjectZero}.
	 *
	 * @param entity {@link Entity}
	 * @return a {@link Entity} only have fields that is`t object zero from parameter entity
	 */
	public static Object generateValueEntity(Entity entity) {
		EntityInfo entityInfo = FeiFeiBootStrap.get().<FeiFeiContainer>container().getEntityInfoGet().get(entity);
		Map<String, Object> notNullField = getNotNullField(entity);
		Object obj = ClassUtil.newInstance(entity.getClass());
		boolean notObjectO = false;
		for (String fieldName : notNullField.keySet()) {
			Object fieldValue = notNullField.get(fieldName);
			if (!ObjectZero.isObject_0(fieldValue)) {
				notObjectO = true;
				String methodName = methodSet(fieldName);
				Method methodSet = entityInfo.getMethodInfo().get(methodName);
				try {
					methodSet.invoke(obj, fieldValue);
				} catch (IllegalAccessException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		if (notObjectO) {
			return obj;
		} else {
			return null;
		}
	}

	/**
	 * Get field name from table.field .
	 *
	 * @param tablePointField tableName.fieldName
	 * @return fieldName
	 */
	public static String getFieldName(String tablePointField) {
		String[] tableFiled = tablePointField.split("\\" + Sql.POINT);
		return tableFiled[1];
	}

	/**
	 * Get a entity key condition
	 * eg: student(id=i5): id=15
	 *
	 * @param entity entity contain entity key(id,primary key,union primary key)
	 * @return  Condition
	 */
	public static Condition getEntityKeyCondition(Entity entity) {
		EntityInfo entityInfo = FeiFeiBootStrap.get().<FeiFeiContainer>container().getEntityInfoGet().get(entity);
		Map<String, Object> notNullFields = EntityUtil.getNotNullField(entity);
		List<String> entityKeys = getEntityKey(entity).stream().map(Field::getName).collect(Collectors.toList());
		Object entityKeyObj = ClassUtil.newInstance(entity.getClass());
		for (String fieldName : entityKeys) {
			Method methodSet = entityInfo.getMethodInfo().get(EntityUtil.methodSet(fieldName));
			if (methodSet == null) {
				throw new QueryException("Entity class {} field {} have no set method", entity.getClass(), fieldName);
			}
			Object fieldValue = notNullFields.get(fieldName);
			if (fieldValue == null) {
				throw new QueryException("Entity {} field {} value is null", entity, fieldName);
			}
			try {
				methodSet.invoke(entityKeyObj, fieldValue);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return EntityCondition.create((Entity) entityKeyObj);
	}

	/**
	 * Get primary key or union primary keys or null;
	 *
	 * @param entity {@link Entity}
	 * @return a list of key column
	 */
	public static List<Field> getEntityKey(Entity entity) {
		List<Field> entityKeys = Lists.newArrayList();
		Field primaryKey = getPrimaryKey(entity);
		List<Field> unionPrimaryKeys = getUnionPrimaryKey(entity);
		Field entityId = getEntityId(entity);

		// 标记添加了几次entity key.
		int addNum = 0;
		if (primaryKey != null) {
			entityKeys.add(primaryKey);
			addNum++;
		}
		if (!unionPrimaryKeys.isEmpty()) {
			entityKeys.addAll(unionPrimaryKeys);
			addNum++;
		}
		if (entityId != null) {
			entityKeys.add(entityId);
			addNum++;
		}

		if (addNum == 0) {
			throw new QueryException("Entity class {} have no id or primary key or union primary keys", entity.getClass());
		}
		if (addNum > 1&&!(entityId!=null&&primaryKey!=null)) {
			throw new QueryException("Entity class {} can only have one key of {id and union primary keys,primary key,union primary keys}", entity.getClass());
		}
		return entityKeys;
	}

	/**
	 * Get primary key [id] from entity.
	 *
	 * @param entity {@link Entity}
	 * @return the "id" column
	 */
	public static Field getEntityId(Entity entity) {
		EntityInfo entityInfo = FeiFeiBootStrap.get().<FeiFeiContainer>container().getEntityInfoGet().get(entity);
		for (String fieldName : entityInfo.getFieldInfo().keySet()) {
			if (fieldName.equals(Sql.PRIMARY_KEY_ID)) return entityInfo.getFieldInfo().get(fieldName);
		}
		return null;
	}

	/**
	 * Get primary key [id] from entity.
	 *
	 * @param entity {@link Entity}
	 * @return the "id" column
	 */
	public static Field getEntityId(Class entity) {
		EntityInfo entityInfo = FeiFeiBootStrap.get().<FeiFeiContainer>container().getEntityInfoGet().get(entity);
		for (String fieldName : entityInfo.getFieldInfo().keySet()) {
			if (fieldName.equals(Sql.PRIMARY_KEY_ID)) return entityInfo.getFieldInfo().get(fieldName);
		}
		return null;
	}

	/**
	 * Get union primary keys from entity.
	 *
	 * @param entity {@link Entity}
	 * @return the foreign key of entity
	 */
	public static List<Field> getUnionPrimaryKey(Entity entity) {
		List<Field> unionPrimaryKeys = Lists.newArrayList();
		EntityInfo entityInfo = FeiFeiBootStrap.get().<FeiFeiContainer>container().getEntityInfoGet().get(entity);
		for (Field field : entityInfo.getFieldInfo().values()) {
			UnionPrimaryKey unionPrimaryKey = field.getAnnotation(UnionPrimaryKey.class);
			if (unionPrimaryKey != null) unionPrimaryKeys.add(field);
		}
		return unionPrimaryKeys;
	}

	/**
	 * Get primary key from entity.
	 *
	 * @param entity entity
	 * @return the primary key of entity
	 */
	public static Field getPrimaryKey(Entity entity) {
		EntityInfo entityInfo = FeiFeiBootStrap.get().<FeiFeiContainer>container().getEntityInfoGet().get(entity);
		return getPrimaryKeyField(entityInfo);
	}

	/**
	 * Get primary key from entity.
	 *
	 * @param entity entity
	 * @return the primary key of entity
	 */
	public static Field getPrimaryKey(Class entity) {
		EntityInfo entityInfo = FeiFeiBootStrap.get().<FeiFeiContainer>container().getEntityInfoGet().get(entity);
		return getPrimaryKeyField(entityInfo);
	}

	public static Field getPrimaryKeyField(EntityInfo entityInfo) {
		for (Field field : entityInfo.getFieldInfo().values()) {
			PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
			if (primaryKey != null) return field;
		}
		return null;
		//throw new QueryException("Entity class {} have no primary ke",entity.getClass());
	}

	/**
	 * Get not null field from entity.
	 *
	 * @param entity {@link Entity}
	 * @return HashMap contained not null fields
	 */
	public static Map<String, Object> getNotNullField(Entity entity) {
		Map<String, Object> fields = Maps.newHashMap();
		addNotNullField(entity, fields);
		return fields;
	}

	/**
	 * Add not null field from entity to map provided.
	 *
	 * @param entity entity
	 * @param fields a map
	 */
	public static void addNotNullField(Entity entity, Map<String, Object> fields) {
		EntityInfo entityInfo = FeiFeiBootStrap.get().<FeiFeiContainer>container().getEntityInfoGet().get(entity);
		for (String fieldName : entityInfo.getFieldInfo().keySet()) {
			Method methodGet = entityInfo.getMethodInfo().get(methodGet(fieldName));
			try {
				Object fieldValue = methodGet.invoke(entity);
				if (fieldValue != null) {
					fields.put(fieldName, fieldValue);
				}
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Combine method getXxx according to field name.
	 *
	 * @param fieldName field name
	 * @return the get method name of the field
	 */
	public static String methodGet(String fieldName) {
		return "get" + StringUtil.firstUppercase(fieldName);
	}

	/**
	 * Combine method setXxx according to field name.
	 *
	 * @param fieldName field name
	 * @return the set method name of the field
	 */
	public static String methodSet(String fieldName) {
		return "set" + StringUtil.firstUppercase(fieldName);
	}

	/**
	 * Get first not null table.field name from entity.
	 *
	 * @param entity {@link Entity}
	 * @return the first not null field table.name
	 */
	public static String getFirstTableFieldName(Entity entity) {
		String fieldName = getNotNullField(entity).keySet().stream().findFirst().orElse(null);
		EntityInfo entityInfo=FeiFeiBootStrap.get().<FeiFeiContainer>container().getEntityInfoGet().get(entity);
		return entityInfo.getTableColumn(fieldName);
	}

	/**
	 * Get first not null field name from entity.
	 *
	 * @param entity {@link Entity}
	 * @return first not null field name
	 */
	public static String getNotNullFirstFieldName(Entity entity) {
		String fieldName = getNotNullField(entity).keySet().stream().findFirst().orElse(null);
		return fieldName;
	}

	/**
	 * Get not null field name.
	 *
	 * @param entices multi {@link Entity}
	 * @return a set of not null table.field name
	 */
	public static Set<String> getNotNullTableFieldNames(Entity... entices) {
		Set<String> fieldNames = Sets.newHashSet();
		for (Entity entity : entices) {
			fieldNames.addAll(notNullTableFieldName(entity));
		}
		return fieldNames;
	}

	/**
	 * Get all field name.
	 *
	 * @param entices multi {@link Entity}
	 * @return a set of all table.field name
	 */
	public static Set<String> getAllTableFieldNames(Entity... entices) {
		Set<String> fieldNames = Sets.newHashSet();
		for (Entity entity : entices) {
			fieldNames.addAll(allTableFieldName(entity));
		}
		return fieldNames;
	}

	/**
	 * Get all not null table field name,eg: table.name
	 *
	 * @param entity {@link Entity}
	 * @return a set of not null table.field name from the entity
	 */
	private static Set<String> notNullTableFieldName(Entity entity) {
		Set<String> fieldNames = getNotNullField(entity).keySet();
		EntityInfo entityInfo = FeiFeiBootStrap.get().<FeiFeiContainer>container().getEntityInfoGet().get(entity);
		Set<String> result=Sets.newHashSet();
		for (String fieldName : fieldNames) {
			result.add(entityInfo.getTableColumn().get(fieldName));
		}
		return result;
	}

	/**
	 * Get all field name.
	 *
	 * @param entity {@link Entity}
	 * @return a set of all table.field name
	 */
	private static Set<String> allTableFieldName(Entity entity) {
		EntityInfo entityInfo = FeiFeiBootStrap.get().<FeiFeiContainer>container().getEntityInfoGet().get(entity);
		return entityInfo.getTableColumn().values();
	}

	/**
	 * Get first not null field value from entity.
	 *
	 * @param entity {@link Entity}
	 * @return first field value
	 */
	public static Object getFirstFieldValue(Entity entity) {
		Map<String, Object> fields = getNotNullField(entity);
		String fieldName = fields.keySet().stream().findFirst().orElse(null);
		if (fieldName != null) {
			return fields.get(fieldName);
		}
		return null;
	}

	/**
	 * Concat field name with comma,eg: f1,f2,f3
	 *
	 * @param targets many String
	 * @return String
	 */
	public static String concatTargetsWithComma(String... targets) {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < targets.length; i++) {
			if (i != 0) {
				str.append(Sql.COMMA);
			}
			str.append(targets[i]);
		}
		return str.toString();
	}

	/**
	 * Get all entity names. [Student,Teacher] : {student,teacher}
	 *
	 * @param entities multi {@link Entity}
	 * @return a set of entity name
	 */
	public static Set<String> entityNames(Entity... entities) {
		Set<String> entityNames = Sets.newHashSet();
		for (Entity entity : entities) {
			entityNames.add(StringUtil.classLowerHump(entity));
		}
		return entityNames;
	}

	/**
	 * Convert Serializable to Entity
	 * @param entities multi {@link Serializable}
	 * @return a array of {@link Entity}
	 */
	public static Entity[] convertToEntity(Serializable... entities) {
		Entity[] arrays = new Entity[entities.length];
		for (int i = 0, entitiesLength = entities.length; i < entitiesLength; i++) {
			arrays[i] = (Entity) entities[i];
		}
		return arrays;
	}

	/**
	 * Convert Serializable to String
	 * @param entities multi {@link Serializable}
	 * @return a array of string
	 */
	public static String[] convertToString(Serializable... entities) {
		String[] arrays = new String[entities.length];
		for (int i = 0, entitiesLength = entities.length; i < entitiesLength; i++) {
			arrays[i] = (String) entities[i];
		}
		return arrays;
	}

	/**
	 * Get all class names.<p>
	 *     eg: [Student.class,Teacher.class] : [student,teacher]
	 * @param classes classes
	 * @return String[]
	 */
	public static String[] tableNames(Class... classes) {
		String[] names = new String[classes.length];
		for (int i = 0; i < classes.length; i++) {
			// 转换成表名
			EntityInfo entityInfo=FeiFeiBootStrap.get().<FeiFeiContainer>container().getEntityInfoGet().get(classes[i]);
			names[i] = entityInfo.getTableName();
		}
		return names;
	}

	/**
	 * Get a object that only contain one field that is primary key.
	 * @param entity {@link Entity}
	 * @param id id column value
	 * @param <T> entity type
	 * @return a entity have only one field that is primary key
	 */
	public static <T> T getPrimaryKeyOrIdObject(Entity entity, Integer id) {
		Object idObject = ClassUtil.newInstance(entity.getClass());
		EntityInfo entityInfo = FeiFeiBootStrap.get().<FeiFeiContainer>container().getEntityInfoGet().get(entity.getClass());
		//获取自增主键
		Field idField = getEntityId(entity);
		if (idField == null) {
			idField = getPrimaryKey(entity);
		}
		if (idField == null) {
			throw new QueryException("Entity class {} have no primary key or id", entity.getClass());
		}

		String idName = idField.getName();
		if (entityInfo != null) {
			// 调用 setId方法
			String methodSetName = methodSet(idName);
			Method methodSet = entityInfo.getMethodInfo().get(methodSetName);
			try {
				methodSet.invoke(idObject, id);
				return (T) idObject;
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Get a object that only contain one field that is primary key.
	 * @param entity {@link Entity}
	 * @param id id column value
	 * @param <T> entity type
	 * @return a entity have only one field that is primary key
	 */
	public static <T> T getPrimaryKeyOrIdObject(Class entity, Integer id) {
		Object idObject = ClassUtil.newInstance(entity);
		EntityInfo entityInfo = FeiFeiBootStrap.get().<FeiFeiContainer>container().getEntityInfoGet().get(entity);
		//获取自增主键
		Field idField = getEntityId(entity);
		if (idField == null) {
			idField = getPrimaryKey(entity);
		}
		if (idField == null) {
			throw new QueryException("Entity class {} have no primary key or id", entity);
		}

		String idName = idField.getName();
		if (entityInfo != null) {
			// 调用 setId方法
			String methodSetName = methodSet(idName);
			Method methodSet = entityInfo.getMethodInfo().get(methodSetName);
			try {
				methodSet.invoke(idObject, id);
				return (T) idObject;
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
