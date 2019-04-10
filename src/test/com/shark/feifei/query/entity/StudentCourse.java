package com.shark.feifei.query.entity;


import java.util.Date;

/**
 * @Author: SuLiang
 * @Date: 2018/10/31 0031
 * @Description:
 */
public class StudentCourse extends AbstractEntity {
	private Integer id;
	private String courseName;
	private String courseClass;
	private String studyTime;
	private Date createTime;
	private Date updateTime;

	public Integer getId() {
		return id;
	}

	public StudentCourse setId(Integer id) {
		this.id = id;
		return this;
	}

	public String getCourseName() {
		return courseName;
	}

	public StudentCourse setCourseName(String courseName) {
		this.courseName = courseName;
		return this;
	}

	public String getCourseClass() {
		return courseClass;
	}

	public StudentCourse setCourseClass(String courseClass) {
		this.courseClass = courseClass;
		return this;
	}

	public String getStudyTime() {
		return studyTime;
	}

	public StudentCourse setStudyTime(String studyTime) {
		this.studyTime = studyTime;
		return this;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public StudentCourse setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public StudentCourse setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	@Override
	public String toString() {
		return "StudentCourse{" +
				"id=" + id +
				", courseName='" + courseName + '\'' +
				", courseClass='" + courseClass + '\'' +
				", studyTime='" + studyTime + '\'' +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				'}';
	}

	public static StudentCourse create(){
		return new StudentCourse();
	}
}
