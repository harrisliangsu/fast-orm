package com.shark.feifei.query.query;


import com.shark.feifei.query.base.BaseQueryTest;
import com.shark.feifei.query.consts.QueryOptions;
import com.shark.feifei.query.entity.Student;
import org.testng.annotations.Test;

import java.util.Date;

/**
 * @Author: Shark Chili
 * @Email: sharkchili.su@gmail.com
 * @Date: 2018/10/25 0025
 */
public class InsertTest extends BaseQueryTest {

	@Test
	public void testInsert(){
		query= EntityQuery.create().insert(Student.create().setName("Jenny").setAge(16).setAddress("America").setCreateTime(new Date()));
		query.query();
	}

	@Test
	public void testInsertReturnId(){
		query= EntityQuery.create()
				.addOption(QueryOptions.RETURN_ID)
				.insert(Student.create().setName("Kevin").setAge(18).setAddress("England").setCreateTime(new Date()));
		query.query();
	}

	@Test
	public void testInsertReturnRecord(){
		query= EntityQuery.create()
				.addOption(QueryOptions.RETURN_RECORD)
				.insert(Student.create().setName("Steve").setAge(17).setAddress("France").setCreateTime(new Date()));
		query.query();
	}

	@Test
	public void testInsertBatch(){
		Student student1=Student.create().setName("Mark").setAge(14).setCreateTime(new Date());
		Student student2=Student.create().setName("David").setAge(15).setCreateTime(new Date());
		Student student3=Student.create().setName("Charles").setAge(16).setCreateTime(new Date());
		Student student4=Student.create().setName("Rose").setAge(17).setCreateTime(new Date());
		query= EntityQuery.create()
				.addOption(QueryOptions.BATCH_OPERATION)
				.insert(student1,student2,student3,student4);
		query.query();
	}

	@Test
	public void testInsertMulti(){
		Student student1=Student.create().setName("Mark").setAge(14).setCreateTime(new Date());
		Student student2=Student.create().setName("David").setAge(15).setCreateTime(new Date());
		Student student3=Student.create().setName("Charles").setAge(16).setCreateTime(new Date());
		Student student4=Student.create().setName("Rose").setAge(17).setCreateTime(new Date());
		query= EntityQuery.create().insert(student1,student2,student3,student4);
		query.query();
	}
}
