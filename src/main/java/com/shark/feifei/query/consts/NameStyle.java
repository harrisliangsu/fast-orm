package com.shark.feifei.query.consts;


import com.shark.container.common.ConfigConst;
import com.shark.feifei.Exception.ConfigException;
import com.shark.feifei.query.config.Ignore;
import com.shark.util.util.StringUtil;


/**
 * The style of name to database table.
 * @Author: Shark Chili
 * @Date: 2018/10/30 0030
 */
public enum NameStyle implements ConfigConst {
	NULL("null"),
	/**lower camel Case (first letter lowercase)*/
	LOWER_CAMEL_CASE("lowerCamelCase"),
	/**upper camel Case (first capital letters)*/
	UPPER_CAMEL_CASE("upperCamelCase"){
		@Override
		public String tableToEntity(String name,Ignore ignore) {
			name=removeTableFix(name,ignore);
			return StringUtil.firstLowercase(name);
		}

		@Override
		public String columnToField(String column,Ignore ignore) {
			column=removeColumnFix(column,ignore);
			return StringUtil.firstLowercase(column);
		}

		@Override
		public String entityToTable(String entity,Ignore ignore) {
			entity=StringUtil.firstUppercase(entity);
			return NameStyle.addTableFix(entity,ignore);
		}

		@Override
		public String fieldToColumn(String field,Ignore ignore) {
			field=StringUtil.firstUppercase(field);
			return NameStyle.addColumnFix(field,ignore);
		}
	},
	/**underline*/
	UNDERLINE("underline"){
		@Override
		public String tableToEntity(String name,Ignore ignore) {
			name=removeTableFix(name,ignore);
			return underlineToLowerCamelCase(name);
		}

		@Override
		public String columnToField(String column,Ignore ignore) {
			column=removeColumnFix(column,ignore);
			return underlineToLowerCamelCase(column);
		}

		@Override
		public String entityToTable(String entity,Ignore ignore) {
			entity=lowerCamelCaseToUnderline(entity);
			entity=NameStyle.addTableFix(entity,ignore);
			return entity;
		}

		@Override
		public String fieldToColumn(String field,Ignore ignore) {
			field=lowerCamelCaseToUnderline(field);
			field=NameStyle.addColumnFix(field,ignore);
			return field;
		}
	};

	String styleName;

	NameStyle(String styleName) {
		this.styleName = styleName;
	}

	public String getStyleName() {
		return styleName;
	}

	/**
	 * Convert lower came case to underline
	 * @param field field name
	 * @return field name that name style is underline
	 */
	public static String lowerCamelCaseToUnderline(String field){
		StringBuilder builder=new StringBuilder();
		int end=0;
		for (int i = 0; i < field.length(); i++) {
			char letter=field.charAt(i);
			if (Character.isUpperCase(letter)){
				if (end!=0){
					builder.append(Sql.UNDERLINE);
				}
				builder.append(field.substring(0,i));
				end=i;
			}
		}
		if (end<field.length()){
			if (end!=0){
				builder.append(Sql.UNDERLINE);
			}
			builder.append(StringUtil.firstLowercase(field.substring(end)));
		}
		return builder.toString();
	}

	/**
	 * Convert underline to lower camel case
	 * @param column column name
	 * @return column name that name style is lower camel case
	 */
	public String underlineToLowerCamelCase(String column) {
		String[] strs=column.split(Sql.UNDERLINE);
		StringBuilder builder=new StringBuilder();
		for (int i = 0; i < strs.length; i++) {
			if (i==0){
				builder.append(strs[i].toLowerCase());
			}else {
				builder.append(StringUtil.firstUppercase(strs[i]));
			}
		}
		return builder.toString();
	}

	/**
	 * Convert table to LOWER_CAMEL_CASE entity.
	 * @param table table name
	 * @param ignore {@link Ignore}
	 * @return the string of entity name
	 */
	public String tableToEntity(String table,Ignore ignore){
		return removeTableFix(table,ignore);
	}

	/**
	 * Convert entity to table.
	 * @param entity entity name
	 * @param ignore {@link Ignore}
	 * @return table name
	 */
	public String entityToTable(String entity,Ignore ignore){
		return addTableFix(entity,ignore);
	}

	/**
	 * Convert column to LOWER_CAMEL_CASE field.
	 * @param column column name
	 * @param ignore {@link Ignore}
	 * @return field name
	 */
	public String columnToField(String column,Ignore ignore){
		return removeColumnFix(column,ignore);
	}

	/**
	 * Convert field to column.
	 * @param field field name
	 * @param ignore {@link Ignore}
 	 * @return column name
	 */
	public String fieldToColumn(String field,Ignore ignore){
		return addColumnFix(field,ignore);
	}

	private static String addTableFix(String name,Ignore ignore){
		if (ignore!=null){
			String prefixTable= ignore.getPrefixTable();
			prefixTable=prefixTable==null?"":prefixTable;
			String postfixTable= ignore.getPostfixTable();
			postfixTable=postfixTable==null?"":postfixTable;
			return prefixTable+name+postfixTable;
		}else {
			return name;
		}
	}

	private static String addColumnFix(String name,Ignore ignore){
		if (ignore!=null){
			String prefixColumn= ignore.getPrefixColumn();
			prefixColumn=prefixColumn==null?"":prefixColumn;
			String postfixColumn= ignore.getPostfixColumn();
			postfixColumn=postfixColumn==null?"":postfixColumn;
			return prefixColumn+name+postfixColumn;
		}else {
			return name;
		}
	}

	private static String removeTableFix(String name,Ignore ignore){
		if (ignore!=null){
			String prefixTable= ignore.getPrefixTable();
			String postfixTable= ignore.getPostfixTable();
			return StringUtil.removeFix(name,prefixTable,postfixTable);
		}else {
			return name;
		}
	}

	private static String removeColumnFix(String name,Ignore ignore){
		if (ignore!=null){
			String prefixColumn= ignore.getPrefixColumn();
			String postfixColumn= ignore.getPostfixColumn();
			return StringUtil.removeFix(name,prefixColumn,postfixColumn);
		}else {
			return name;
		}
	}

	public NameStyle getByName(String styleName){
		for (NameStyle value : NameStyle.values()) {
			if (value.styleName.equals(styleName)){
				return value;
			}
		}
		throw new ConfigException("name style name %s config error",styleName);
	}


	@Override
	public ConfigConst getDefault() {
		return LOWER_CAMEL_CASE;
	}
}
