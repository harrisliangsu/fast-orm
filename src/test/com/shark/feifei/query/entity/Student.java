package com.shark.feifei.query.entity;

import java.util.Date;

/**
 * @Author: SuLiang
 * @Date: 2018/10/15 0015
 * @Description:
 */
public class Student extends AbstractEntity {
	private Integer id;
	private Integer teacherId;
	private String name;
	private Integer age;
	private String address;
	private Date createTime;
	private Date updateTime;

	public Integer getTeacherId() {
		return teacherId;
	}

	public Student setTeacherId(Integer teacherId) {
		this.teacherId = teacherId;
		return this;
	}

	public String getName() {
		return name;
	}

	public Student setName(String name) {
		this.name = name;
		return this;
	}

	public Integer getAge() {
		return age;
	}

	public Student setAge(Integer age) {
		this.age = age;
		return this;
	}

	public String getAddress() {
		return address;
	}

	public Student setAddress(String address) {
		this.address = address;
		return this;
	}

	public static Student create(){
		return new Student();
	}

	public Integer getId() {
		return id;
	}

	public Student setId(Integer id) {
		this.id = id;
		return this;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public Student setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public Student setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	@Override
	public String toString() {
		return "Student{" +
				"id=" + id +
				", teacherId=" + teacherId +
				", name='" + name + '\'' +
				", age=" + age +
				", address='" + address + '\'' +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				'}';
	}
}
