package com.shark.feifei.query.condition;


import com.shark.feifei.query.base.BaseQueryTest;
import com.shark.feifei.query.entity.ObjectZero;
import com.shark.feifei.query.entity.Student;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

/**
 * @Author: Shark Chili
 * @Email: sharkchili.su@gmail.com
 * @Date: 2018/10/25 0025
 */
public class ConditionTest extends BaseQueryTest {

	private Condition condition;

	private Student originInt=Student.create().setAge(ObjectZero.INTEGER);
	private Student otherInt=Student.create().setAge(15);
	private Student originStr=Student.create().setName(ObjectZero.STRING);
	private Student otherStr=Student.create().setName("mike");

	@Test
	public void testLike() {
		condition= StringCondition.create().like("%su%");
	}

	@Test
	public void testNotLike() {
		condition= StringCondition.create().notLike("%su%");
	}

	@Test
	public void testUnequal() {
		condition= EntityCondition.create(originInt).unequal(otherInt);
	}

	@Test
	public void testGreater() {
		condition= EntityCondition.create(originInt).greater(otherInt);
	}

	@Test
	public void testGreaterEqual() {
		condition= EntityCondition.create(originInt).equal(otherInt);
	}

	@Test
	public void testLess() {
		condition= EntityCondition.create(originInt).less(otherInt);
	}

	@Test
	public void testLessEqual() {
		condition= EntityCondition.create(originInt).lessEqual(otherInt);
	}

	@Test
	public void testEqual() {
		condition= EntityCondition.create(originInt).equal(otherInt);
	}

	@Test
	public void testWhenThen() {
		//condition=EntityCondition.caseWhen(originStr).whenThen("jack","tom");
		condition= EntityCondition.caseWhen().whenThen("jack","tom");
	}

	@Test
	public void testWhenConditionThen() {
		Condition when= EntityCondition.create(originStr).equal(otherStr);
		condition= EntityCondition.caseWhen(originStr).whenConditionThen("tome",when);
	}

	@Test
	public void testWhenThenElse() {
		condition= EntityCondition.caseWhen(originStr).whenThenElse("tome","jack","jim");
	}

	@Test
	public void testWhenConditionThenElse() {
		Condition when= EntityCondition.create(originStr).equal(otherStr);
		condition= EntityCondition.caseWhen(originStr).whenConditionThenElse("jack","mike",when);
	}

	@Test
	public void testNextCondition() {
		condition= EntityCondition.create(originInt).equal(otherInt).finishOneCondition().nextCondition(originStr).equal(otherStr);
		//	==>
		//	condition=EntityCondition.create(otherInt).finishOneCondition().nextCondition(otherStr);
	}

	@AfterMethod
	public void printCondition(){
		LOGGER.info("{}",condition);
	}
}