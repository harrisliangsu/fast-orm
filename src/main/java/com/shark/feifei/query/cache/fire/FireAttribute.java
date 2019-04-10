package com.shark.feifei.query.cache.fire;

import com.shark.feifei.query.condition.Condition;

import java.util.List;
import java.util.Set;

/**
 * The attribute to fire cache
 * @Author: Shark Chili
 * @Date: 2018/11/15 0015
 */
public class FireAttribute {
	public static final FireAttribute SATISFY=new FireAttribute();

	/**exclude table names*/
	private Set<String> excludeTableNames;
	/**include table names*/
	private Set<String> includeTableNames;
	/**if condition is here,it will fire cache*/
	private Set<Condition> containConditions;
	/**condition must be equal to this list*/
	private List<Condition> equalConditions;
	/**if sql is here,it will fire cache*/
	private Set<String> containSqls;

	public Set<String> getExcludeTableNames() {
		return excludeTableNames;
	}

	public void setExcludeTableNames(Set<String> excludeTableNames) {
		this.excludeTableNames = excludeTableNames;
	}

	public Set<String> getIncludeTableNames() {
		return includeTableNames;
	}

	public void setIncludeTableNames(Set<String> includeTableNames) {
		this.includeTableNames = includeTableNames;
	}

	public Set<Condition> getContainConditions() {
		return containConditions;
	}

	public void setContainConditions(Set<Condition> containConditions) {
		this.containConditions = containConditions;
	}

	public List<Condition> getEqualConditions() {
		return equalConditions;
	}

	public void setEqualConditions(List<Condition> equalConditions) {
		this.equalConditions = equalConditions;
	}

	public Set<String> getContainSqls() {
		return containSqls;
	}

	public void setContainSqls(Set<String> containSqls) {
		this.containSqls = containSqls;
	}

	@Override
	public String toString() {
		return "FireAttribute{" +
				"excludeTableNames=" + excludeTableNames +
				", includeTableNames=" + includeTableNames +
				", containConditions=" + containConditions +
				", equalConditions=" + equalConditions +
				", containSqls=" + containSqls +
				'}';
	}
}