package com.shark.feifei.query.query;


import com.shark.feifei.query.base.BaseQueryTest;
import com.shark.feifei.query.condition.Condition;
import com.shark.feifei.query.condition.EntityCondition;
import com.shark.feifei.query.consts.QueryOptions;
import com.shark.feifei.query.entity.ObjectZero;
import com.shark.feifei.query.entity.Student;
import org.testng.annotations.Test;

/**
 * @Author: Shark Chili
 * @Email: sharkchili.su@gmail.com
 * @Date: 2018/10/25 0025
 */
public class UpdateTest extends BaseQueryTest {

	@Test
	public void updateById(){
		query= EntityQuery.create().update(Student.create().setId(1).setName("MIKE"));
	}

	@Test
	public void updateByIdWhere(){
		query= EntityQuery.create().update(Student.create().setId(1).setName("mike"));
	}

	@Test
	public void updateWhere(){
		query= EntityQuery.create()
				.update(Student.create().setName("mary"))
				.where(EntityCondition.create(Student.create().setAge(10)));
		query.query();
	}

	@Test
	public void updateReturnId(){
		query= EntityQuery.create().addOption(QueryOptions.RETURN_ID)
				.update(Student.create().setName("MIKE"))
				.where(EntityCondition.create(Student.create().setAge(10)));
		query.query();
	}

	@Test
	public void updateReturnRecord(){
		query= EntityQuery.create()
				.addOption(QueryOptions.RETURN_RECORD)
				.update(Student.create().setName("MIKE"))
				.where(EntityCondition.create(Student.create().setAge(10)));
		query.query();
	}

	@Test
	public void updateCaseWhen(){
		Condition nameCaseWhen= EntityCondition.caseWhen(Student.create().setName(ObjectZero.STRING))
				.updateColumn(Student.create().setName(ObjectZero.STRING))
				.whenThen("MIKE","mike")
				.whenThen("TOM","tome")
				.whenThen("JACK","jack")
				.end();
		query= EntityQuery.create().update(Student.create().setName(ObjectZero.STRING)).caseWhen(nameCaseWhen);
		query.query();
	}

}
