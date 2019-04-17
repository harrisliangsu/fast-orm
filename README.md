# sharkchili-feifei
Feifei is a slight orm frame,almost zero configuration but could config any one include custom container,
running in container based on original sql,supporting query based on object and string sql query.

## Why developed feifei
I want to query without any string include table,column name,because i check the spelling of letters always,
and want to query with object directly but could query with sql string when i want,so i just do it.
Now,feifei could`t support all sql(like call desc(column)),you can query based on original sql when you experience it.

## Links
* [github](https://github.com/mmflys/sharkchili-feifei)
* [sonatype](https://search.maven.org/search?q=a:sharkchili-feifei)
* [maven](https://mvnrepository.com/artifact/com.fishbyby.sharkchili/sharkchili-feifei)

## Update version
* [1.0.3](https://github.com/mmflys/sharkchili-feifei/blob/master/update/1.0.3-RELEASE.md)
* [1.0.4](https://github.com/mmflys/sharkchili-feifei/blob/master/update/1.0.4-RELEASE.md)
* [1.0.5](https://github.com/mmflys/sharkchili-feifei/blob/master/update/1.0.5-RELEASE.md)
* [1.0.7](https://github.com/mmflys/sharkchili-feifei/blob/master/update/1.0.7-RELEASE.md)
* [2.0.1]()
## How to build

* Latest stable [Oracle JDK 8](http://www.oracle.com/technetwork/java/)
* Latest stable [Apache Maven](http://maven.apache.org/)

Note that this is build-time requirement.  JDK 8  is enough to run your feifei-based application.

## How to start

Add this dependency to you pom.
```xml
    <dependency>
        <groupId>com.fishbyby.sharkchili</groupId>
        <artifactId>sharkchili-feifei</artifactId>
        <version>2.0.1</version>
    </dependency>
    <!-- For latest version find maven address above -->
```

## Usages

### SELECT

* Query based on original sql.
```java
    Query query = StudentQuery.create("SELECT * FROM student")
    List<Student> students = query.query();
```
* Update based on EntityQuery.
```java
    Query query = StudentQuery.create().select(Student.create().setName("mike").setAge(13));
    List<Student> students=query.query();
```
* Query based on entity.
```java
    Student condition = Student.create().setName("mike").setAddress("China");
    Student student = condition.selectSingle();
```

### INSERT
* Insert based on original sql.
```java
    Query query = StudentQuery.create("INSERT INTO student(id,name,age) VALUES(1,'mike',15)");
    query.query();
```
* Insert based on EntityQuery.
```java
    Query query = EntityQuery.create().insert(Student.create().setName("Jenny").setAge(16).setAddress("America").setCreateTime(new Date()));
    query.query();
```
* Insert based on Entity.
```java
    Student student = Student.create().setName("Nick").setAge(10).setAddress("America").setCreateTime(new Date());
    student.insert();
```

### UPDATE

* Update based on original sql.
```java
    Query query = StudentQuery.create("UPDATE student SET name='mike'");
    query.query();
```
* Update based on EntityQuery.
```java
    Query query = EntityQuery.create().update(Student.create().setId(1).setName("MIKE"));
    query.query();
```
* Update based on Entity.
```java
    Student student = Student.create().setId(18).setName("Nick").setAge(10).setAddress("China").setCreateTime(new Date());
    student.update();
```

### DELETE
* Delete based on original sql.
```java
    Query query = StudentQuery.create("DELETE FROM student WHERE id=1");
    query.query();
```
* Delete based on EntityQuery.
```java
    Query query = EntityQuery.create().delete(Student.create().setName("Rose"));
    query.query();
```
* Delete based on Entity.
```java
    Student student = Student.create().setId(19);
    student.delete();
```

## Config

Feifei have all default config instead of entity path.

### Query config

* `entityPackage`: path of your entity(it`s necessary).
* `queryOptions`: provide option that whether return record,return id after querying or not,auto add from phrase,auto print query result and so on.
* `dataBaseType`: type of your database,mysql supported temporarily.
* `connectionGet`: how to get a connection,you can implement this interface to set how to get connection,use FeiFeiPoolDatasource by default.
* `ignore`: ignore string when database table map to entity,eg table name 't_test_student' -> 'test'
* `nameStyle`: the name style of database table and column.
* `cacheConfig`: config of cache,if you need to use cache.

### Cache config

* `openCache`: if false,will query from database
* `fireType`: type of fire cache,provide ANY(any query),THREAD(same thread),CONDITION_FIRE(config by yourself)
* `fireCaches`: is corresponding fire type CONDITION_FIRE,implement FireCache class if you want config a condition to fire cache.
* `cacheStoryLoad`: type of story cache,provide app cache now,will support redis cache later.
* `fireCacheManager`: this need to ben config

## Type of query supported

* `SINGLE_SQL`: execute one sql per querying.
* `MULTI_SQL`: execute multi sql per querying.
* `BATCH_SQL`: execute multi sql that have same structure per querying on batch.

Detailed by class OperationType,set by call Query.queryData().setOperationType(OperationType operationType).

## Paging querying supported conveniently

```java
    Query<Student> queryPage = EntityQuery.<Student>create().select(Student.create());
    queryPage.pageSize(3).firstPage();
    // You need to save this queryPage object in your session for next querying,
    // It isn`t necessary call pageSize when next paging query.
```

## Cache

Feifei close cache by default,but provide abundant cache firing condition,you can config it by yourself.

* `ANY`: any query will fire cache,and query from cache firstly,query from database secondly.
* `SESSION`: query from same thread will fire cache.
* `CONDITION_FIRE`: you can implement interface FireCache to define a condition to fire cache.

Email me,if you have any idea.
`Email: sharkchili.su@gmail.com`
