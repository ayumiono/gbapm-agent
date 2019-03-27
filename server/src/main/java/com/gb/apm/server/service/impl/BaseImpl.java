package com.gb.apm.server.service.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import org.mongojack.DBCursor;
import org.mongojack.DBQuery;
import org.mongojack.DBQuery.Query;
import org.mongojack.JacksonDBCollection;
import org.mongojack.WriteResult;
import org.mongojack.internal.MongoJackModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gb.apm.server.GBMongodbException;
import com.gb.apm.server.MCollection;
import com.gb.apm.server.MID;
import com.gb.apm.server.compoent.MongoClientTemplate;
import com.google.inject.Inject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

public class BaseImpl<TDocument> {
	
	private static final Logger logger = LoggerFactory.getLogger(BaseImpl.class);
	
	private MongoClientTemplate mongoClientTemplate;
	
	public static final String APM_DB = "babysitter";
	
	ThreadLocal<JacksonDBCollection<TDocument,String>> _collection = new ThreadLocal<>();
	final Class<TDocument> clazz;
	final Method idGetter;
	final String idField;
	final String collectionName;
	
	private static final ObjectMapper mapper = new ObjectMapper().setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
	
	static{
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		MongoJackModule.configure(mapper);
	}
	
	protected BaseImpl(Class<TDocument> clazz, MongoClientTemplate mongoClientTemplate) throws Exception {
		this.clazz = clazz;
		Annotation annotation = findAnnotation(clazz,MCollection.class);
		if(annotation == null) {
			throw new GBMongodbException(clazz.getName()+" doesn't have the MCollection annotatioin");
		}
		String collectionName = ((MCollection) annotation).value();
		this.collectionName = collectionName;
		this.idField = findIdField(clazz);
		this.idGetter = findIdGetter(clazz,idField);
		this.mongoClientTemplate = mongoClientTemplate;
	}
	
	private String findIdField(Class<?> clazz) throws SecurityException {
		Field[] fields = clazz.getDeclaredFields();
		for(Field field : fields) {
			MID idAnnotation =  field.getAnnotation(MID.class);
			if(idAnnotation != null) {
				String fname = field.getName();
				return fname;
			}
		}
		return null;
	}
	
	private Method findIdGetter(Class<?> clazz,String idField) throws NoSuchMethodException, SecurityException {
		if(idField == null) return null;
		Method method = clazz.getMethod("get"+idField.substring(0, 1).toUpperCase()+idField.substring(1));
		return method;
	}
	
	@SuppressWarnings("rawtypes")
	private <A extends Annotation> A findAnnotation(Class<?> clazz, Class<A> annotationType) {
		A annotation = clazz.getAnnotation(annotationType);
		if (annotation != null) {
			return annotation;
		}
		for (Class<?> ifc : clazz.getInterfaces()) {
			annotation = findAnnotation(ifc, annotationType);
			if (annotation != null) {
				return annotation;
			}
		}
		Class superClass = clazz.getSuperclass();
		if (superClass == null || superClass == Object.class) {
			return null;
		}
		return findAnnotation(superClass, annotationType);
	}
	
	/**
	 * 切换数据源
	 * @param dataBase
	 */
	public BaseImpl<TDocument> use(String dataBase) {
		@SuppressWarnings("deprecation")
		DB db = mongoClientTemplate.client().getDB(dataBase);
		DBCollection collection = db.getCollection(collectionName);
		JacksonDBCollection<TDocument, String> jacksonCollection = JacksonDBCollection.wrap(collection,clazz,String.class,mapper);
		_collection.set(jacksonCollection);
		return this;
	}
	
	public JacksonDBCollection<TDocument,String> getCollection() {
		return _collection.get();
	}
	
	public int count() throws GBMongodbException{
		if(_collection.get() == null) {
			throw new GBMongodbException("please chose the right mongo database first");
		}
		try {
			return _collection.get().find().count();
		} catch (Exception e) {
			throw new GBMongodbException(String.format("%s count exception", this.collectionName), e);
		}
	}
	
	public List<TDocument> query() throws GBMongodbException{
		if(_collection.get() == null) {
			throw new GBMongodbException("please chose the right mongo database first");
		}
		try {
			List<TDocument> result = new ArrayList<>();
			DBCursor<TDocument> rs = _collection.get().find();
			rs.forEach(new Consumer<TDocument>() {
				@Override
				public void accept(TDocument t) {
					result.add(t);
				}
			});
			return result;
		} catch (Exception e) {
			throw new GBMongodbException(String.format("%s query exception", this.collectionName), e);
		}
	}
	
	public List<TDocument> query(Map<String, Object> params) throws GBMongodbException {
		if(_collection.get() == null) {
			throw new GBMongodbException("please chose the right mongo database first");
		}
		try {
			List<TDocument> result = new ArrayList<>();
			DBCursor<TDocument> rs = _collection.get().find(ormQuery(params));
			rs.forEach(new Consumer<TDocument>() {
				@Override
				public void accept(TDocument t) {
					result.add(t);
				}
			});
			return result;
		} catch (Exception e) {
			throw new GBMongodbException(String.format("%s query exception:%s", this.collectionName,params), e);
		}
	}
	
	public List<TDocument> query(TDocument document) throws GBMongodbException {
		if(_collection.get() == null) {
			throw new GBMongodbException("please chose the right mongo database first");
		}
		try {
			List<TDocument> result = new ArrayList<>();
			DBCursor<TDocument> rs = _collection.get().find(ormQuery(document));
			rs.forEach(new Consumer<TDocument>() {
				@Override
				public void accept(TDocument t) {
					result.add(t);
				}
			});
			return result;
		} catch (Exception e) {
			throw new GBMongodbException(String.format("%s query exception:%s", this.collectionName,document), e);
		}
	}
	
	public String insert(TDocument document) throws GBMongodbException {
		if(_collection.get() == null) {
			throw new GBMongodbException("please chose the right mongo database first");
		}
		try {
			WriteResult<TDocument, String> ir = _collection.get().insert(document);
//			return ir.getSavedId(); FIXME
			return null;
		} catch (Exception e) {
			throw new GBMongodbException(String.format("%s insert exception:%s", this.collectionName,document), e);
		}
	}
	
	public void updateById(TDocument t) throws GBMongodbException {
		if(_collection.get() == null) {
			throw new GBMongodbException("please chose the right mongo database first");
		}
		try {
			_collection.get().update(ormIdQuery(t), t);
		} catch (Exception e) {
			throw new GBMongodbException(String.format("%s update exception:%s", this.collectionName,t), e);
		}
	}
	
	private Query ormIdQuery(TDocument document) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, GBMongodbException {
		if(idGetter == null || idField == null) {
			throw new GBMongodbException("实体类"+clazz.getName()+"没有MID注解，不能使用updateById方法");
		}
		Query query = DBQuery.empty();
		Object id = this.idGetter.invoke(document);
		query.is(idField, id);
		return query;
	}
	
	private Query ormQuery(Map<String, Object> params) {
		Query query = DBQuery.empty();
		for(Entry<String, Object> entry : params.entrySet()) {
			query.is(entry.getKey(),entry.getValue());
		}
		return query;
	}
	
	private Query ormQuery(TDocument document) throws Exception {
		Query query = DBQuery.empty();
		Map<String, Object> debugmap = null;
		if(logger.isDebugEnabled()) {
			debugmap = new HashMap<>();
		}
		
		if(document == null) return query;
		try {
			Field[] fields = document.getClass().getDeclaredFields();
			for(Field f : fields) {
				f.setAccessible(true);
				String fname = f.getName();
				String getter = "get"+fname.substring(0, 1).toUpperCase()+fname.substring(1);
				try {
					Method _getter = document.getClass().getMethod(getter);
					_getter.setAccessible(true);
					Object value = _getter.invoke(document);
					if(value == null) continue;
					query.is(fname, value);
					if(logger.isDebugEnabled()) {
						debugmap.put(fname, value);
					}
				} catch (NoSuchMethodException e) {
//					logger.debug(this.clazz.getName() + " field 【{}】 doesn't have getter",fname);
					continue;
				} catch (Exception e) {
//					logger.error("ormQuery invoke error",e);
					continue;
				}
			}
			if(logger.isDebugEnabled()) {
				logger.debug(this.getClass().getName()+" ormQuery: "+debugmap.toString());
			}
			return query;
		} catch (Exception e) {
			throw e;
		}
	}
}
