package com.shark.feifei.query.query;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.shark.feifei.Exception.QueryException;
import com.shark.feifei.FeiFeiBootStrap;
import com.shark.feifei.container.FeiFeiContainer;
import com.shark.feifei.query.condition.Condition;
import com.shark.feifei.query.condition.EntityCondition;
import com.shark.feifei.query.consts.QueryOptions;
import com.shark.feifei.query.consts.Sql;
import com.shark.feifei.query.entity.Entity;
import com.shark.feifei.query.entity.EntityInfo;
import com.shark.feifei.query.entity.EntityUtil;
import com.shark.feifei.query.entity.ObjectZero;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The entity model of {@link Query},use {@link StringQuery} query in substance
 * @Author: Shark Chili
 * @Date: 2018/10/13 0013
 */
public class EntityQuery<T> extends AbstractQuery<T> {

	/**String query*/
	private StringQuery<String> query;

	public EntityQuery() {
		super();
		query = new StringQuery<>();
		copy(query);
	}

	public EntityQuery(String sql) {
		super(sql);
		query = new StringQuery<>(sql);
		copy(query);
	}

	private void copy(StringQuery query) {
		query.queryData = this.queryData;
	}

	@Override
	public Query<T> select(Serializable... tables) {
		Set<String> fieldName = EntityUtil.getAllTableFieldNames(EntityUtil.convertToEntity(tables));
		query.selectColumns(EntityUtil.concatTargetsWithComma(fieldName.toArray(new String[]{})));
		// 判断是否有初始equal条件
		addInitCondition(tables);
		// 自动添加from字句
		addInitFrom(tables);
		return this;
	}

	@Override
	public Query<T> selectColumns(Serializable... tables) {
		Set<String> fieldName = EntityUtil.getNotNullTableFieldNames(EntityUtil.convertToEntity(tables));
		query.selectColumns(EntityUtil.concatTargetsWithComma(fieldName.toArray(new String[]{})));
		// 判断是否有初始equal条件
		addInitCondition(tables);
		// 自动添加from字句
		addInitFrom(tables);
		return this;
	}

	/**
	 * 添加初始from字句
	 *
	 * @param tables tables.
	 */
	private void addInitFrom(Serializable[] tables) {
		boolean autoFrom=this.queryData.options.contains(QueryOptions.AUTO_FROM);
		Class[] ts=new Class[tables.length];
		boolean notNull=false;
		if (autoFrom){
			for (int i = 0; i < tables.length; i++) {
				Serializable column = tables[i];
				if (column != null) {
					notNull=true;
					ts[i]=column.getClass();
				}
			}
		}
		if (notNull){
			from(ts);
		}
	}

	/**
	 * Whether add init condition.
	 *
	 * @param entities entities
	 */
	private void addInitCondition(Serializable... entities) {
		for (Serializable column : entities) {
			if (column != null) {
				Object objectCondition = EntityUtil.generateValueEntity((Entity) column);
				if (objectCondition != null) {
					Condition condition = EntityCondition.create((Entity) objectCondition);
					this.queryData.initCondition.add(condition);
				}
			}
		}
	}

	@Override
	public Query<T> from(Class... tables) {
		query.from(tables);
		return this;
	}

	@Override
	public Query<T> where(Condition... conditions) {
		query.where(conditions);
		return this;
	}

	@Override
	public Query<T> or(Condition... conditions) {
		query.or(conditions);
		return this;
	}

	@Override
	public Query<T> and(Condition... conditions) {
		query.and(conditions);
		return this;
	}

	@Override
	public Query<T> innerJoin(Class table) {
		query.innerJoin(table);
		return this;
	}

	@Override
	public Query<T> leftJoin(Class table) {
		query.leftJoin(table);
		return this;
	}

	@Override
	public Query<T> rightJoin(Class table) {
		query.rightJoin(table);
		return this;
	}

	@Override
	public Query<T> on(Condition... conditions) {
		query.on(conditions);
		return this;
	}

	@Override
	public Query<T> desc(Serializable column) {
		String firstFieldName=EntityUtil.getFirstTableFieldName((Entity) column);
		this.query.desc(firstFieldName);
		return this;
	}

	@Override
	public Query<T> asc(Serializable column) {
		String firstFieldName=EntityUtil.getFirstTableFieldName((Entity) column);
		this.query.asc(firstFieldName);
		return this;
	}

	@Override
	public Query<T> as(Serializable column) {
		String firstFieldName=EntityUtil.getFirstTableFieldName((Entity) column);
		this.query.as(firstFieldName);
		return this;
	}

	@Override
	public Query<T> count(Serializable column) {
		if (Entity.class.isAssignableFrom(column.getClass())){
			String firstFieldName=EntityUtil.getFirstTableFieldName((Entity) column);
			this.query.count(firstFieldName);
		}else {
			if (column== Sql.STAR){
				this.query.count(column);
			}else{
				Integer digit;
				try {
					digit = (Integer) column;
				} catch (Exception e) {
					throw new QueryException("count phrase is illegal,{}",column);
				}
				if (digit==Sql.DIGIT_0||digit==Sql.DIGIT_1){
					this.query.count(Sql.STRING_1);
				}
			}
		}
		return this;
	}

	@Override
	public Query<T> sum(Serializable column) {
		String firstFieldName=EntityUtil.getFirstTableFieldName((Entity) column);
		this.query.sum(firstFieldName);
		return this;
	}

	@Override
	public Query<T> max(Serializable column) {
		String firstFieldName=EntityUtil.getFirstTableFieldName((Entity) column);
		this.query.max(firstFieldName);
		return this;
	}

	@Override
	public Query<T> min(Serializable column) {
		String firstFieldName=EntityUtil.getFirstTableFieldName((Entity) column);
		this.query.min(firstFieldName);
		return this;
	}

	@Override
	public Query<T> insert(Serializable... records) {
		beforeUpdate(records);

		String[] SQls;
		// 判断是否批量处理
		switch (this.queryData.operationType) {
			case SINGLE_SQL:
			case BATCH_SQL: {
				SQls = new String[1];
				// 生成前缀
				Entity entity = (Entity) records[0];
				EntityInfo entityInfo= FeiFeiBootStrap.get().<FeiFeiContainer>container().getEntityInfoGet().get(entity);
				this.queryData.sqlEntity=entity;
				// 获取非空字段
				List<String> notNullColumn = EntityUtil.getNotNullField(entity).keySet().stream().map(entityInfo::getColumn).collect(Collectors.toList());
				StringBuilder prefix = generateInsertSql(records[0], notNullColumn);
				// 添加参数
				for (Serializable record : records) {
					Entity myEntity = (Entity) record;
					// 获取非空字段
					Map<String, Object> myNotNullField = EntityUtil.getNotNullField(myEntity);
					for (String column : notNullColumn) {
						String fieldName=entityInfo.getField(column);
						this.queryData.parameters.add(myNotNullField.get(fieldName));
					}
				}
				SQls[0] = prefix.toString();
				break;
			}
			case MULTI_SQL: {
				SQls = new String[records.length];
				// 生成n条语句
				for (int i = 0; i < records.length; i++) {
					StringBuilder mySql = generateInsertSql(records[i]);
					Entity entity = (Entity) records[i];
					// 获取非空字段
					Map<String, Object> notNullField = EntityUtil.getNotNullField(entity);
					this.queryData.parameters.addAll(notNullField.values());
					SQls[i] = mySql.toString();
				}
				break;
			}
			default: {
				throw new QueryException("operation is`t initialized");
			}
		}
		query.insert(SQls);
		return this;
	}

	@Override
	public Query<T> update(Serializable... records) {
		beforeUpdate(records);
		// 判断是否批量处理
		switch (this.queryData.operationType) {
			case SINGLE_SQL: {
				this.queryData.sqlEntity= (Entity) records[0];
				StringBuilder mysql = generateUpdateSql(records[0]);
				query.update(mysql.toString());
				break;
			}
			case BATCH_SQL: {

			}
			case MULTI_SQL: {
				List<String> mySQLs = Lists.newArrayList();
				for (Serializable record : records) {
					mySQLs.add(generateUpdateSql(record).toString());
				}
				query.update(mySQLs.toArray(new String[]{}));
				break;
			}
		}
		return this;
	}

	@Override
	public Query<T> delete(Serializable... records) {
		beforeUpdate(records);

		String[] SQls = null;
		// 判断是否批量处理
		switch (this.queryData.operationType) {
			case SINGLE_SQL: {
				// 先判断有无entity key.若无则根据字段值生成条件
				SQls = new String[1];
				SQls[0] = generateDeleteSql(records[0]).toString();
				break;
			}
			case BATCH_SQL:
			case MULTI_SQL: {
				SQls = new String[records.length];
				for (int i = 0; i < records.length; i++) {
					Serializable record = records[i];
					SQls[i] = generateDeleteSql(record).toString();
				}
				break;
			}
		}
		query.delete(SQls);
		return this;
	}

	/**
	 * Generate delete sql.
	 *
	 * @param record record
	 * @return a delete sql.
	 */
	private StringBuilder generateDeleteSql(Serializable record) {
		StringBuilder builder = new StringBuilder();
		EntityInfo entityInfo=FeiFeiBootStrap.get().<FeiFeiContainer>container().getEntityInfoGet().get((Entity) record);
		builder.append(Sql.DELETE).append(Sql.FROM).append(entityInfo.getTableName());

		Entity entity = (Entity) record;
		Map<String, Object> notNullField = EntityUtil.getNotNullField(entity);
		if (!notNullField.isEmpty()) {
			builder.append(Sql.WHERE).append(Sql.ONE_EQUAL_ONE);
			List<String> entityKeyFieldName = EntityUtil.getEntityKey(entity.getClass()).stream().map(Field::getName).collect(Collectors.toList());
			if (!entityKeyFieldName.isEmpty()) {
				Map<String, Object> entityKeyValueMap = Maps.newHashMap();
				entityKeyFieldName.forEach(e -> {
					Object entityKeyValue = notNullField.get(e);
					if (entityKeyValue != null) {
						entityKeyValueMap.put(e, notNullField.get(e));
					}
				});
				// entity key 是否有赋值
				if (!entityKeyValueMap.isEmpty()) {
					Object keyObj = EntityUtil.generateEntity(entityKeyValueMap, entity.getClass());
					Condition condition = EntityCondition.create((Entity) keyObj);
					builder.append(Sql.AND).append(condition.sql());
					this.queryData.parameters.addAll(condition.parameters());
				} else {
					Object objectCondition = EntityUtil.generateValueEntity(entity);
					if (objectCondition != null) {
						Condition condition = EntityCondition.create((Entity) objectCondition);
						builder.append(Sql.AND).append(condition.sql());
						this.queryData.parameters.addAll(condition.parameters());
					}
				}
			}
		}
		return builder;
	}

	/**
	 * Generate a insert sql.
	 * eg: INSERT INTO student(name,age) VALUES(?,?);
	 *
	 * @param record record
	 * @return a insert sql
	 */
	private StringBuilder generateInsertSql(Serializable record) {
		return generateInsertSql(record, null);
	}

	/**
	 * Generate update sql and add parameters.
	 *
	 * @param record record
	 * @return a update sql
	 */
	private StringBuilder generateUpdateSql(Serializable record) {
		StringBuilder mysql = new StringBuilder();
		Entity entity = (Entity) record;
		EntityInfo entityInfo= FeiFeiBootStrap.get().<FeiFeiContainer>container().getEntityInfoGet().get(entity);
		Map<String, Object> notNullFields = EntityUtil.getNotNullField(entity);
		List<String> entityKeys = EntityUtil.getEntityKey(entity.getClass()).stream().map(Field::getName).collect(Collectors.toList());
		// 生成前缀
		mysql.append(Sql.UPDATE).append(entityInfo.getTableName()).append(Sql.SET);
		// 添加属性
		boolean start = false;
		for (String fieldName : notNullFields.keySet()) {
			if (!entityKeys.contains(fieldName)) {
				if (start) mysql.append(Sql.COMMA);
				Object fieldValue = notNullFields.get(fieldName);
				// case when 字句
				if (ObjectZero.isObject_0(fieldValue)) {
					mysql.append(entityInfo.getTableColumn(fieldName));
				} else {
					mysql.append(entityInfo.getTableColumn(fieldName)).append(Sql.EQUAL).append(Sql.QUESTION);
					this.queryData.parameters.add(fieldValue);
				}
				start = true;
			}
		}
		boolean containEntityKey = notNullFields.keySet().containsAll(entityKeys);
		if (containEntityKey) {
			// 生成condition.
			Condition condition = EntityUtil.getEntityKeyCondition(entity);
			// 拼接成最终语句
			mysql.append(Sql.WHERE).append(Sql.ONE_EQUAL_ONE);
			mysql.append(Sql.AND).append(condition.sql());
			this.queryData.parameters.addAll(condition.parameters());
		}
		return mysql;
	}

	/**
	 * @param record record
	 * @param fields insert columns
	 * @return a insert sql.
	 */
	private StringBuilder generateInsertSql(Serializable record, List<String> fields) {
		StringBuilder mySql = new StringBuilder();

		Entity entity = (Entity) record;
		EntityInfo entityInfo=FeiFeiBootStrap.get().<FeiFeiContainer>container().getEntityInfoGet().get(entity);
		// 获取非空字段
		Set<String> notNullColumn = EntityUtil.getNotNullField(entity).keySet().stream().map(entityInfo::getColumn).collect(Collectors.toSet());
		// 表名
		String tableName =entityInfo.getTableName();
		// 拼接头部
		mySql.append(Sql.INSERT).append(tableName).append(Sql.BRACKET_LEFT);
		boolean start = false;
		// 占位符部分 values(?,?);
		StringBuilder placeholder = new StringBuilder();
		placeholder.append(Sql.VALUES).append(Sql.BRACKET_LEFT);
		// 拼接字段值
		if (fields == null || fields.isEmpty()) {
			fields = Lists.newArrayList(notNullColumn);
		}
		for (String fieldName : fields) {
			if (start) {
				mySql.append(Sql.COMMA);
				placeholder.append(Sql.COMMA);
			}
			mySql.append(fieldName);
			placeholder.append(Sql.QUESTION);
			start = true;
		}
		mySql.append(Sql.BRACKET_RIGHT);
		placeholder.append(Sql.BRACKET_RIGHT);
		mySql.append(placeholder.toString());
		return mySql;
	}

	public static <T> Query<T> create() {
		return new EntityQuery<>();
	}

	public static <T> Query<T> create(String sql) {
		return new EntityQuery<>(sql);
	}

	public static <T> Query<T> createAutoFrom(){
		return EntityQuery.<T>create().addOption(QueryOptions.AUTO_FROM);
	}
}
