package com.shark.feifei.query.query;


import com.shark.feifei.query.base.BaseQueryTest;
import com.shark.feifei.query.entity.Student;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.List;

/**
 * @Author: Shark Chili
 * @Email: sharkchili.su@gmail.com
 * @Date: 2018/10/29 0029
 */
public class EntityTest extends BaseQueryTest {

	@Test
	public void testSelectSingle(){
		Student condition=Student.create().setName("mike").setAddress("China");
		Student student=condition.selectSingle();
	}

	@Test
	public void testSelect(){
		Student student=Student.create().setName("Nick").setAge(10).setAddress("America");
		List<Student> students=student.select();
	}

	@Test
	public void testInsert(){
		Student student=Student.create().setName("Nick").setAge(10).setAddress("America").setCreateTime(new Date());
		student.insert();
	}

	@Test
	public void testUpdate(){
		Student student=Student.create().setId(18).setName("Nick").setAge(10).setAddress("China").setCreateTime(new Date());
		student.update();
	}

	@Test
	public void testDelete(){
		Student student=Student.create().setId(19);
		student.delete();
	}

	@Test
	public void testCount(){
		Student student=Student.create().setAddress("America");
		System.out.println(student.count());
	}
}
