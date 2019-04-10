package com.shark.feifei.query.query;


import com.shark.feifei.query.base.BaseQueryTest;
import com.shark.feifei.query.condition.Condition;
import com.shark.feifei.query.condition.EntityCondition;
import com.shark.feifei.query.consts.QueryOptions;
import com.shark.feifei.query.dao.StudentQuery;
import com.shark.feifei.query.entity.ObjectZero;
import com.shark.feifei.query.entity.Student;
import com.shark.feifei.query.entity.Teacher;
import org.testng.annotations.Test;

/**
 * @Author: Shark Chili
 * @Email: sharkchili.su@gmail.com
 * @Date: 2018/10/25 0025
 */
public class SelectTest extends BaseQueryTest {

	private Student conditionStudent =Student.create().setName("mike").setAge(13);
	private Student columnsStudent =Student.create().setName(ObjectZero.STRING).setAge(ObjectZero.INTEGER);
	private Student columnsConditionStudent =Student.create().setId(1).setName(ObjectZero.STRING).setAge(ObjectZero.INTEGER);

	private Student originInt=Student.create().setAge(ObjectZero.INTEGER);
	private Student otherInt=Student.create().setAge(15);
	private Student originStr=Student.create().setName(ObjectZero.STRING);
	private Student otherStr=Student.create().setName("mike");

	@Test
	public void testSelect(){
		query= StudentQuery.create().addOption(QueryOptions.AUTO_FROM).select(conditionStudent);
		query.query();
	}

	@Test
	public void testSelectColumns(){
		query= StudentQuery.create().selectColumns(columnsConditionStudent).from(Student.class);
		query.query();
	}

	@Test
	public void testWhere(){
		query=StudentQuery.create().selectColumns(columnsStudent)
				.from(Student.class)
				.where(EntityCondition.create(originInt).equal(otherInt));
		query.query();
	}

	@Test
	public void testOr(){
		query=StudentQuery.create().select(Student.create())
				.from(Student.class)
				.where(EntityCondition.create(Student.create().setAge(100)))
				.or(EntityCondition.create(Student.create().setAge(10)));
		query.query();
	}

	@Test
	public void testAnd(){
		query=StudentQuery.create().selectColumns(columnsStudent)
				.from(Student.class)
				.where(EntityCondition.create(originInt).equal(otherInt))
				.and(EntityCondition.create(Student.create().setName("jack")));
		query.query();
	}

	@Test
	public void testInnerJoin(){
		query=StudentQuery.create().select(Student.create())
				.from(Student.class)
				.innerJoin(Teacher.class)
				.on(
						EntityCondition.create(Student.create().setTeacherId(ObjectZero.INTEGER))
						.equal(Teacher.create().setId(ObjectZero.INTEGER))
				);
		query.query();
	}

	@Test
	public void testLeftJoin(){
		query=StudentQuery.create().select(Student.create())
				.from(Student.class)
				.leftJoin(Teacher.class)
				.on(
						EntityCondition.create(Student.create().setTeacherId(ObjectZero.INTEGER))
								.equal(Teacher.create().setId(ObjectZero.INTEGER))
				);
		query.query();
	}

	@Test
	public void testRightJoin(){
		query=StudentQuery.create().select(Student.create())
				.from(Student.class)
				.rightJoin(Teacher.class)
				.on(
						EntityCondition.create(Student.create().setTeacherId(ObjectZero.INTEGER))
								.equal(Teacher.create().setId(ObjectZero.INTEGER))
				);
		query.query();
	}

	@Test
	public void caseWhen(){
		Condition nameCaseWhen= EntityCondition.caseWhen(Student.create().setName(ObjectZero.STRING))
				.updateColumn(Student.create().setName(ObjectZero.STRING))
				.whenThen("mike","MIKE")
				.whenThen("tom","TOME")
				.whenThen("jack","JACK")
				.end();

		query=StudentQuery.create()
				.select(Student.create())
				.from(Student.class)
				.caseWhen(nameCaseWhen);
		query.query();
	}

	@Test
	public void testPage(){
		Query<Student> queryPage= EntityQuery.<Student>create().addOption(QueryOptions.AUTO_FROM).select(Student.create());
		queryPage.pageSize(3).page(1);
	}

	@Test
	public void testPageNext(){
		Query<Student> queryPage= EntityQuery.<Student>create().addOption(QueryOptions.AUTO_FROM).select(Student.create());
		queryPage.pageSize(3).nextPage();
	}

	@Test
	public void testPagePrevious(){
		Query<Student> queryPage= EntityQuery.<Student>create().addOption(QueryOptions.AUTO_FROM).select(Student.create());
		queryPage.pageSize(3).nextPage();
		queryPage.previousPage();
	}

	@Test
	public void testFirstPage(){
		Query<Student> queryPage= EntityQuery.<Student>create().addOption(QueryOptions.AUTO_FROM).select(Student.create());
		queryPage.pageSize(3).firstPage();
	}

	@Test
	public void testLastPage(){
		Query<Student> queryPage= EntityQuery.<Student>create().addOption(QueryOptions.AUTO_FROM).select(Student.create());
		queryPage.pageSize(3).lastPage();
	}

	@Test
	public void TestPageScrollPrevious(){
		Query<Student> queryPage= EntityQuery.<Student>create().addOption(QueryOptions.QUERY_PAGE_SCROLL, QueryOptions.AUTO_FROM).select(Student.create());
		queryPage.pageSize(3).previousPage();
	}

	@Test
	public void TestPageScrollNext(){
		Query<Student> queryPage= EntityQuery.<Student>create().addOption(QueryOptions.QUERY_PAGE_SCROLL, QueryOptions.AUTO_FROM).select(Student.create());
		queryPage.pageSize(3).page(10);
		queryPage.nextPage();
	}

	@Test
	public void testCount(){
		query= EntityQuery.create().count(Student.create().setName(ObjectZero.STRING)).from(Student.class);
		query.singleQuery();
	}

	@Test
	public void testSum(){
		query= EntityQuery.create().sum(Student.create().setAge(ObjectZero.INTEGER)).from(Student.class);
		query.singleQuery();
	}

	@Test
	public void testMax(){
		query= EntityQuery.create().max(Student.create().setAge(ObjectZero.INTEGER)).from(Student.class);
		query.singleQuery();
	}

	@Test
	public void testMin(){
		query= EntityQuery.create().min(Student.create().setAge(ObjectZero.INTEGER)).from(Student.class);
		query.singleQuery();
	}

	@Test
	public void testSql1(){
		query=StudentQuery.create("select * from student").where(EntityCondition.create(Student.create().setId(1)));
		query.query();
	}

	@Test
	public void testSql2(){
		query=StudentQuery.create("select student.* from student inner join teacher on student.teacherId=teacher.id");
		query.query();
	}

	@Test
	public void testSql3(){
		query=StudentQuery.create("select student.* from student inner join teacher on student.teacherId=teacher.id where student.age>13");
		query.query();
	}

	@Test
	public void testSql4(){
		query= EntityQuery.create("select student.* from student inner join teacher on student.teacherId=teacher.id where student.age>13");
		query.query();
	}
}