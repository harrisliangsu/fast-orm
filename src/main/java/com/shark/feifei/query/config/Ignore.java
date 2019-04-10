package com.shark.feifei.query.config;

/**
 * The string ignored when table name is converted to entity name or column name is converted to field name.
 * @Author: Shark Chili
 * @Date: 2018/10/30 0030
 */
public class Ignore {
	/**prefix table name string ignore*/
	private String prefixTable;
	/**postfix table name string ignore*/
	private String postfixTable;
	/**prefix column name string ignore*/
	private String prefixColumn;
	/**post column name string ignore*/
	private String postfixColumn;

	/**
	 * Set ignore prefix in table name and column name
	 * @param prefix prefix sting
	 */
	public void setTableAndColumnPrefix(String prefix){
		this.prefixTable=prefix;
		this.prefixColumn=prefix;
	}

	/**
	 * Set ignore postfix in table name and column name
	 * @param postfix postfix string
	 */
	public void setTableAndColumnPostfix(String postfix){
		this.postfixTable=postfix;
		this.postfixColumn=postfix;
	}

	public String getPrefixTable() {
		return prefixTable;
	}

	public void setPrefixTable(String prefixTable) {
		this.prefixTable = prefixTable;
	}

	public String getPostfixTable() {
		return postfixTable;
	}

	public void setPostfixTable(String postfixTable) {
		this.postfixTable = postfixTable;
	}

	public String getPrefixColumn() {
		return prefixColumn;
	}

	public void setPrefixColumn(String prefixColumn) {
		this.prefixColumn = prefixColumn;
	}

	public String getPostfixColumn() {
		return postfixColumn;
	}

	public void setPostfixColumn(String postfixColumn) {
		this.postfixColumn = postfixColumn;
	}

	public static Ignore defaultIgnore(){
		return new Ignore();
	}

	@Override
	public String toString() {
		return "Ignore{" +
				"prefixTable='" + prefixTable + '\'' +
				", postfixTable='" + postfixTable + '\'' +
				", prefixColumn='" + prefixColumn + '\'' +
				", postfixColumn='" + postfixColumn + '\'' +
				'}';
	}
}
