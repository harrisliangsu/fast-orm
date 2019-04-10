package com.shark.feifei.container;

import com.shark.cache.cache.CacheAppType;
import com.shark.cache.consts.CacheConfigConst;
import com.shark.cache.container.CacheContainer;
import com.shark.container.Container;
import com.shark.container.constants.ContainerStatus;
import com.shark.feifei.consts.FeifeiConfigConst;
import com.shark.feifei.query.cache.fire.FireType;
import com.shark.feifei.query.cache.fire.firecache.FireCache;
import com.shark.feifei.query.config.QueryConfig;
import com.shark.feifei.query.consts.DataBaseType;
import com.shark.feifei.query.consts.NameStyle;
import com.shark.feifei.query.entity.Entity;
import com.shark.feifei.query.entity.EntityInfoGet;
import com.shark.job.container.TaskContainer;
import com.shark.job.job.ScheduleJob;
import com.shark.util.classes.ClassScannerUtil;
import com.shark.util.classes.ClassUtil;
import com.shark.util.util.FileUtil;
import com.shark.util.util.PropertyUtil;
import com.shark.util.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Modifier;
import java.util.Properties;
import java.util.Set;
import java.util.function.Predicate;

/**
 * A abstract feifei container class
 * @Author: Shark Chili
 * @Date: 2018/11/14 0014
 */
public abstract class FeiFeiContainer implements Container {

	private static final Logger LOGGER = LoggerFactory.getLogger(FeiFeiContainer.class);

	/**task container*/
	private TaskContainer taskContainer;
	/**cache container*/
	private CacheContainer cacheContainer;
	/**feifei container status*/
	private ContainerStatus containerStatus;
	/**query config*/
	private QueryConfig queryConfig;
	/**entity info*/
	private EntityInfoGet entityInfoGet;

	public FeiFeiContainer() {
		containerStatus = ContainerStatus.ZERO;
		entityInfoGet=new EntityInfoGet();
	}

	@Override
	public Container init() {
		LOGGER.info(this.getClass().getSimpleName() + " init ");
		taskContainer().init();
		cacheContainer().init();
		// scan entity
		configInit();
		// additional init
		containerInit();
		// 程序结束时调用钩子线程
		Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
		this.containerStatus = ContainerStatus.INITIALIZED;
		return this;
	}

	@Override
	public Container start() {
		LOGGER.info(this.getClass().getSimpleName() + " start ");
		taskContainer().start();
		cacheContainer().start();
		containerStart();
		this.containerStatus = ContainerStatus.RUNNING;
		return this;
	}

	@Override
	public Container stop() {
		LOGGER.info(this.getClass().getSimpleName() + " stop ");
		taskContainer().stop();
		cacheContainer().start();
		containerStop();
		this.containerStatus = ContainerStatus.STOPPED;
		return this;
	}

	/**
	 * Config init.
	 */
	private void configInit(){
		Properties properties= FileUtil.readProperties("/"+ FeifeiConfigConst.CONFIG_FILE_DEFAULT);
		// whether customer set config or not
		// config other properties
		configProperties(properties);
		// entity path
		scanEntityClass(properties);
		// scan fire type class
		scanFireTypeClass();
		// schedule init job
		schedule(queryConfig().getConnectionGet().initJobs().toArray(new ScheduleJob[]{}));
	}

	private void configProperties(Properties properties) {
		PropertyUtil.doIfNotNull(properties, FeifeiConfigConst.DATABASE_TYPE, o->queryConfig().setDataBaseType(DataBaseType.NULL.getByName(o.toString())));
		PropertyUtil.doIfNotNull(properties, FeifeiConfigConst.NAME_STYLE, o->queryConfig().setNameStyle(NameStyle.NULL.getByName(o.toString())));
		PropertyUtil.doIfNotNull(properties, FeifeiConfigConst.FIRE_TYPE, o->queryConfig().getCacheConfig().setFireType(FireType.NULL.getByName(o.toString())));
		PropertyUtil.doIfNotNull(properties, CacheConfigConst.CACHE_APP_TYPE, o->queryConfig().getCacheConfig().setCacheAppType(CacheAppType.NULL.getByName(o.toString())));
	}

	private void scanEntityClass(Properties properties) {
		if (StringUtil.isEmpty(queryConfig().getEntityPackage())){
			// read properties
			Object entityPackageObj=properties.get(FeifeiConfigConst.ENTITY_PACKAGE);
			if (entityPackageObj!=null){
				String entityPackage=entityPackageObj.toString();
				LOGGER.debug("read entity package name {} from properties", entityPackage);
				entityInfoGet.storage(entityPackage, queryConfig().getNameStyle(), queryConfig().getIgnore());
			}else {
				LOGGER.debug("custom had`t config entity package name,so scan from class path");
				Predicate<Class> isEntity= c-> Entity.class.isAssignableFrom(c)&&!Modifier.isAbstract(c.getModifiers())&&c!=Entity.class;
				Set<Class> entity= ClassScannerUtil.scanClassFromClassPath(p->!p.endsWith(".jar"), isEntity);
				if (!entity.isEmpty()){
					this.entityInfoGet.storage(queryConfig().getNameStyle(), queryConfig().getIgnore(), entity.toArray(new Class[]{}));
				}
			}
		}else {
			LOGGER.debug("custom had set entity package name {}", queryConfig().getEntityPackage());
			entityInfoGet.storage(queryConfig().getEntityPackage(), queryConfig().getNameStyle(), queryConfig().getIgnore());
		}
	}

	private void scanFireTypeClass() {
		LOGGER.debug("scan fire type class start");
		Predicate<Class> isFireTypeClass= c-> FireType.class.isAssignableFrom(c)&&!Modifier.isAbstract(c.getModifiers())&&c!=FireType.class;
		Set<Class> fireTypeClass= ClassScannerUtil.scanClassFromClassPath(p->!p.endsWith(".jar"), isFireTypeClass);
		for (Class typeClass : fireTypeClass) {
			FireCache fireCache= ClassUtil.newInstance(typeClass);
			queryConfig().getCacheConfig().addFireCache(fireCache);
			LOGGER.debug("add fire cache {}",fireCache);
		}
		if (fireTypeClass.isEmpty()){
			LOGGER.debug("no fire cache costumed");
		}
	}

	/**
	 * Schedule a job
	 * @param jobs jobs
	 * @return this Container
	 */
	public Container schedule(ScheduleJob...jobs){
		for (ScheduleJob job : jobs) {
			taskContainer().schedule(job);
		}
		return this;
	}


	/**
	 * Get a task container,if you had`t set,return a FeiFeiTaskContainer.
	 * @return TaskContainer
	 */
	public TaskContainer taskContainer() {
		if (taskContainer==null){
			taskContainer=new FeiFeiTaskContainer();
		}
		return taskContainer;
	}

	/**
	 * Get a cache container,if you had`t set,return a FeifeiCacheContainer.
	 * @return CacheContainer
	 */
	public CacheContainer cacheContainer() {
		if (cacheContainer==null){
			cacheContainer= new FeifeiCacheContainer();
		}
		return cacheContainer;
	}

	public void setTaskContainer(TaskContainer taskContainer) {
		this.taskContainer = taskContainer;
	}

	/**
	 * Get a sql config,if you had`t set,return a default config.
	 * @return QueryConfig
	 */
	public QueryConfig queryConfig() {
		if (this.queryConfig ==null){
			this.queryConfig = QueryConfig.deFaultConfig();
		}
		return queryConfig;
	}

	public void setQueryConfig(QueryConfig queryConfig) {
		this.queryConfig = queryConfig;
	}

	public EntityInfoGet getEntityInfoGet() {
		return entityInfoGet;
	}
}
