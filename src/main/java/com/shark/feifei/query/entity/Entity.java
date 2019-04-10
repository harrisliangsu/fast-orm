package com.shark.feifei.query.entity;

import com.shark.feifei.annoation.PrimaryKey;
import com.shark.feifei.annoation.UnionPrimaryKey;

import java.io.Serializable;
import java.util.List;

/**
 * <p>Entity mapped to a table from database</p>
 *
 * <h3>Recommend 1: create set method with stream model.</h3>
 * <h3>Recommend 2: create a create static method to get instance</h3>
 * eg:
 * <pre>
 * public class Student implement Entity{
 *     private String name;
 *     private int gae;
 *
 *    public Student setName(String name){
 *         this.name=name;
 *         return this;
 *     }
 *
 *     public Student setAge(int age){
 *         this.age=age;
 *         return this;
 *     }
 *
 *     public static Student create(){
 *         return new Student();
 *     }
 *
 *     public static void main(String[] args){
 *         Student student=Student.create().setAge(11).setName("Mike");
 *     }
 *
 * }
 * </pre>
 *
 * <h3>Entity key</h3>
 * <ul>
 *     <li>id (const String)</li>
 *     <li>primary key (field annotated by {@link PrimaryKey})</li>
 *     <li>union primary key (field annotated by {@link UnionPrimaryKey})</li>
 * </ul>
 *
 * <h3>modify set method template</h3>
 * <pre>
 *		#set($paramName = $helper.getParamName($field, $project))
 * 		#if($field.modifierStatic)
 * 		static ##
 * 		#end
 * 		$classname set$StringUtil.capitalizeWithJavaBeanConvention($StringUtil.sanitizeJavaIdentifier($helper.getPropertyName($field, $project)))($field.type $paramName) {
 * 		#if ($field.name == $paramName)
 *     		#if (!$field.modifierStatic)
 *     			this.##
 *     		#else
 *         		$classname.##
 *     		#end
 * 		#end
 * 		$field.name = $paramName;
 * 		#if(!$field.modifierStatic)
 *     		return this;
 * 		#end
 * }
 * </pre>
 * @Author: Shark Chili
 * @Date: 2018/10/12 0012
 */
public interface Entity extends Serializable {
	/**
	 * execute select query,condition is equal condition(column=value),return records
	 * @param <T> type of records
	 * @return a list of records
	 */
	<T extends Entity> List<T> select();
	/**
	 * execute select query,condition is equal condition(column=value),return one record
	 * @param <T> type of record
	 * @return one record
	 */
	<T extends Entity> T selectSingle();

	/**
	 * execute update to database,condition is equal condition(column=value)
	 */
	void update();

	/**
	 * execute insert to database
	 */
	void insert();

	/**
	 * execute delete this entity from database
	 */
	void delete();
}
