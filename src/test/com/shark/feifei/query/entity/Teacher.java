package com.shark.feifei.query.entity;

import java.util.Date;

/**
 * @Author: SuLiang
 * @Date: 2018/10/14
 * @Description:
 */
public class Teacher extends AbstractEntity {
	private Integer id;
	private String name;
	private Integer age;
	private Date createTime;
	private Date updateTime;

	public Date getCreateTime() {
		return createTime;
	}

	public Teacher setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public Teacher setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	public Integer getId() {
		return id;
	}

	public Teacher setId(Integer id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public Teacher setName(String name) {
		this.name = name;
		return this;
	}

	public Integer getAge() {
		return age;
	}

	public Teacher setAge(Integer age) {
		this.age = age;
		return this;
	}

	@Override
	public String toString() {
		return "Teacher{" +
				"id=" + id +
				", name='" + name + '\'' +
				", age=" + age +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				'}';
	}

	public static Teacher create(){
		return new Teacher();
	}
}
