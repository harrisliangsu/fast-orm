package com.shark.feifei.query.cache.fire;

import com.shark.container.common.ConfigConst;
import com.shark.feifei.Exception.ConfigException;

/**
 * Fire cache type
 * @Author: Shark Chili
 * @Date: 2018/11/15 0015
 */
public enum  FireType implements ConfigConst {
	NULL("null"),
	/**any query will fire cache,mapped to {@link com.shark.feifei.query.cache.fire.firecache.AnyFireCache}*/
	ANY("any"),
	/**same thread share cache,mapped to {@link com.shark.feifei.query.cache.fire.firecache.ThreadFireCache}*/
	THREAD("session"),
	/**condition fire,you need to config {@link FireAttribute}*/
	CONDITION_FIRE("customer");

	String typeName;

	FireType(String typeName) {
		this.typeName = typeName;
	}

	public String getTypeName() {
		return typeName;
	}

	public FireType getByName(String typeName){
		for (FireType value : FireType.values()) {
			if (value.typeName.equals(typeName)){
				return value;
			}
		}
		throw new ConfigException("fire type name %s config error",typeName);
	}


	@Override
	public ConfigConst getDefault() {
		return CONDITION_FIRE;
	}
}
