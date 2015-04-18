package service;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;

/**
 * @author wuliwei
 * 
 */
public class BasePersistenceService {
    private static final Logger logger = Logger.getLogger(BasePersistenceService.class);
	private SessionFactory sessionFactory;

	/**
	 * @param sessionFactory
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * @param o
	 */
	public void save(Object o) {
		sessionFactory.getCurrentSession().save(o);
		logger.info("save " + o.getClass().getName() + ", id = " + getIdFieldValue(o));
	}
	
	/**
	 * @param o
	 */
	public void delete(Object o) {
		sessionFactory.getCurrentSession().delete(o);
		logger.info("delete " + o.getClass().getName() + ", id = " + getIdFieldValue(o));
	}
	
	/**
	 * @param o
	 */
	public void update(Object o) {
		sessionFactory.getCurrentSession().update(o);
		logger.info("update " + o.getClass().getName() + ", id = " + getIdFieldValue(o));
	}
	
	/**
	 * @param objClass
	 * @param objId
	 * @return Object
	 */
	@SuppressWarnings("unchecked")
	public Object get(Class objClass, Serializable objId) {
		return sessionFactory.getCurrentSession().get(objClass, objId);
	}
	
	/**
	 * @param o
	 */
	public void saveOrUpdate(Object o) {
		sessionFactory.getCurrentSession().saveOrUpdate(o);
		logger.info("save or update " + o.getClass().getName() + ", id = " + getIdFieldValue(o));
	}
	
	/**
	 * @param o
	 * @return Object
	 */
	public Object getIdFieldValue(Object o) {
		try {
			for (Field f : o.getClass().getDeclaredFields()) {
				for (Annotation a : f.getDeclaredAnnotations()) {
					if ("Id".equals(a.annotationType().getSimpleName())) {
						return f.get(o);
					}
				}
			}
			for (Method m : o.getClass().getDeclaredMethods()) {
				for (Annotation a : m.getDeclaredAnnotations()) {
					if ("Id".equals(a.annotationType().getSimpleName()) && m.getName().startsWith("get")) {
						return m.invoke(o, new Object[0]);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
