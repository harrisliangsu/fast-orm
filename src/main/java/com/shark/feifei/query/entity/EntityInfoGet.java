package com.shark.feifei.query.entity;

import com.google.common.collect.Maps;
import com.shark.feifei.Exception.DatasourceException;
import com.shark.feifei.query.config.Ignore;
import com.shark.feifei.query.consts.NameStyle;
import com.shark.util.classes.ClassScannerUtil;
import com.shark.util.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Get entity info from program
 * @Author: Shark Chili
 * @Date: 2018/10/12 0012
 */
public class EntityInfoGet {
	private static final Logger LOGGER= LoggerFactory.getLogger(EntityInfoGet.class);

	/**EntityInfo map: key->class name*/
	private final Map<String,EntityInfo> entityInfoMap =Maps.newHashMap();

	public EntityInfo get(Entity entity){
		return entityInfoMap.get(StringUtil.classLowerHump(entity));
	}

	public EntityInfo get(Class entityClass){
		return entityInfoMap.get(StringUtil.classLowerHump(entityClass));
	}

	public EntityInfo get(String entityName){
		return entityInfoMap.get(entityName);
	}

	/**
	 * Storage a entity info to entityInfoMap.
	 * @param entity type implement {@link Entity}
	 * @param nameStyle {@link NameStyle}
	 * @param ignore {@link Ignore}
	 */
	public void storage(Class entity, NameStyle nameStyle, Ignore ignore){
		EntityInfo info=new EntityInfo(entity,nameStyle,ignore);
		this.entityInfoMap.put(StringUtil.classLowerHump(entity),info);
		LOGGER.debug("story entity class {}", entity);
	}

	/**
	 * Storage a entity info to entityInfoMap.
	 * @param nameStyle {@link NameStyle}
	 * @param ignore {@link Ignore}
	 * @param entities {@link Entity}
	 */
	public void storage(NameStyle nameStyle, Ignore ignore,Class...entities){
		for (Class entity : entities) {
			storage(entity, nameStyle, ignore);
		}
	}

	/**
	 * Storage entities from the path.
	 * @param packageName entity package name
	 * @param nameStyle {@link NameStyle}
	 * @param ignore {@link Ignore}
	 */
	public void storage(String packageName, NameStyle nameStyle,Ignore ignore){
		if (StringUtil.isEmpty(packageName)){
			throw new DatasourceException("entity package name had`t set");
		}
		Predicate<Class<?>> isEntity= aClass -> Entity.class.isAssignableFrom(aClass)&&!Modifier.isAbstract(aClass.getModifiers());
		Set<Class<?>> entityClasses= ClassScannerUtil.scanClassWithPredicate(packageName,isEntity);
		for (Class<?> entityClass : entityClasses) {
			storage(entityClass,nameStyle,ignore);
		}
	}
}
