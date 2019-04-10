package com.shark.feifei.query.cache;


import com.shark.feifei.query.dao.StudentQuery;
import com.shark.feifei.query.entity.Student;
import org.testng.annotations.Test;

/**
 * @Author: Shark Chili
 * @Email: sharkchili.su@gmail.com
 * @Date: 2018/11/20 0020
 */
public class CacheTest extends BaseCacheQueryTest {
	private Student conditionStudent =Student.create().setName("mike").setAge(13);

	@Test
	public void testSelect(){
		query= StudentQuery.create().select(conditionStudent);
		query.query();
	}

	public static void sameThread(){
		CacheTest cacheTest=new CacheTest();
		cacheTest.runBefore();
		cacheTest.testSelect();
		cacheTest.testSelect();
	}

	private static void differentThread(){
		Thread t1=new Thread(new Runnable() {
			@Override
			public void run() {
				CacheTest cacheTest=new CacheTest();
				cacheTest.runBefore();
				cacheTest.testSelect();
			}
		});
		Thread t2=new Thread(new Runnable() {
			@Override
			public void run() {
				CacheTest cacheTest=new CacheTest();
				cacheTest.testSelect();
			}
		});
		try {
			t1.start();
			t1.join();
			t2.start();
			t2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		//differentThread();
		sameThread();
	}
}
