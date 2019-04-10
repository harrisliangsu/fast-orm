package com.shark.feifei.query.query;


import com.shark.feifei.query.base.BaseQueryTest;
import com.shark.feifei.query.condition.EntityCondition;
import com.shark.feifei.query.entity.Student;
import org.testng.annotations.Test;

/**
 * @Author: Shark Chili
 * @Email: sharkchili.su@gmail.com
 * @Date: 2018/10/25 0025
 */
public class DeleteTest extends BaseQueryTest {

	@Test
	public void testDelete() {
		query= EntityQuery.create().delete(Student.create().setName("Rose"));
		query.query();
	}

	@Test
	public void testDeleteWhere(){
		query= EntityQuery.create().delete(Student.create()).where(EntityCondition.create(Student.create().setName("Mark")));
		query.query();
	}
}
