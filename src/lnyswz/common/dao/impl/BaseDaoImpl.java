package lnyswz.common.dao.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lnyswz.common.dao.BaseDaoI;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository("baseDao")
public class BaseDaoImpl<T> implements BaseDaoI<T> {
	private final static Logger logger = Logger.getLogger("BaseDaoImpl");
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session getCurrentSession() {
		return this.sessionFactory.getCurrentSession();
	}

	@Override
	public Serializable save(T o) {
		return this.getCurrentSession().save(o);
	}

	@Override
	public T load(Class<T> c, Serializable id) {
		return (T) this.getCurrentSession().load(c, id);
	}
	
	@Override
	public T get(Class<T> c, Serializable id) {
		return (T) this.getCurrentSession().get(c, id);
	}

	@Override
	public T get(String hql) {
		Query q = this.getCurrentSession().createQuery(hql);
		List<T> l = q.list();
		if (l != null && l.size() > 0) {
			return l.get(0);
		}
		return null;
	}

	@Override
	public T get(String hql, Map<String, Object> params) {
		Query q = getQuery(hql, params);
		List<T> l = q.list();
		if (l != null && l.size() > 0) {
			return l.get(0);
		}
		return null;
	}

	@Override
	public void delete(T o) {
		this.getCurrentSession().delete(o);
	}

	@Override
	public void update(T o) {
		this.getCurrentSession().update(o);
	}

	@Override
	public void saveOrUpdate(T o) {
		this.getCurrentSession().saveOrUpdate(o);
	}

	@Override
	public List<T> find(String hql) {
		Query q = this.getCurrentSession().createQuery(hql);
		return q.list();
	}
	
	@Override
	public List<T> find(String hql, Map<String, Object> params) {
		Query q = getQuery(hql, params);
		return q.list();
	}

	@Override
	public List<T> find(String hql, Map<String, Object> params, int page, int rows) {
		Query q = getQuery(hql, params);
		return q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
	}

	private Query getQuery(String hql, Map<String, Object> params) {
		Query q = this.getCurrentSession().createQuery(hql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				q.setParameter(key, params.get(key));
			}
		}
		return q;
	}

	@Override
	public List<T> find(String hql, int page, int rows) {
		Query q = this.getCurrentSession().createQuery(hql);
		return q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
	}

	@Override
	public Long count(String hql) {
		Query q = this.getCurrentSession().createQuery(hql);
		return (Long) q.uniqueResult();
	}
	
	@Override
	public Long countBySQL(String sql) {
		SQLQuery query = this.getCurrentSession().createSQLQuery(sql);
		return  getLong(query.list().get(0));
	}
	
	@Override
	public Long count(String hql, Map<String, Object> params) {
		Query q = getQuery(hql, params);
		return (Long) q.uniqueResult();
	}

	@Override
	public int executeHql(String hql) {
		Query q = this.getCurrentSession().createQuery(hql);
		return q.executeUpdate();
	}

	@Override
	public int executeHql(String hql, Map<String, Object> params) {
		Query q = getQuery(hql, params);
		return q.executeUpdate();
	}
	
	@Override
	public int updateBySQL(String sql) {
		SQLQuery query = this.getCurrentSession().createSQLQuery(sql);
		return query.executeUpdate();
	}
	
	@Override
	public int updateBySQL(String sql, Map<String, Object> params) {
		SQLQuery query = getQueryBySQL(sql, params);
		return query.executeUpdate();
	}

	private SQLQuery getQueryBySQL(String sql, Map<String, Object> params) {
		SQLQuery query = this.getCurrentSession().createSQLQuery(sql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				query.setParameter(Integer.valueOf(key), params.get(key));
			}
		}
		return query;
	}
	
	@Override
	public Object[] getMBySQL(String sql, Map<String, Object> params) {
		SQLQuery query = getQueryBySQL(sql, params);
		List<Object[]> l = query.list();
		if(l != null && l.size() > 0){
			return l.get(0);
		}
		return null;
	}
	
	@Override
	public Object getBySQL(String sql, Map<String, Object> params) {
		SQLQuery query = getQueryBySQL(sql, params);
		List<Object[]> l = query.list();
		if(l != null && l.size() > 0){
			return l.get(0);
		}
		return null;

	}
	
	@Override
	public Object getBySQL(String sql, Map<String, Object> params, Map<String, Object> returns) {
		SQLQuery query = this.getCurrentSession().createSQLQuery(sql);
		if (params != null && !params.isEmpty()) {
			int i = 0;
			for (String key : params.keySet()) {
				query.setParameter(i++, params.get(key));
			}
		}
		if (returns != null && !returns.isEmpty()) {
			for (String key : returns.keySet()) {
				query.addScalar(key, (Type)returns.get(key));
			}
		}
		return query.list().get(0);

	}

	@Override
	public List<Object[]> findBySQL(String sql) {
		return this.getCurrentSession().createSQLQuery(sql)
				.list();
	}

	@Override
	public Long countSQL (String sql) {
		SQLQuery query = this.getCurrentSession().createSQLQuery(sql);	
		return  getLong(query.list().get(0));
	}
	
	@Override
	public Long countSQL (String sql, Map<String, Object> params) {
		SQLQuery query = getQueryBySQL(sql, params);
		return  getLong(query.list().get(0));
	}
	
	public static Long getLong(Object obj){ 
        if (obj==null || false == NumberUtils.isNumber(obj+"")) return 0L; 
        return Long.valueOf(obj+""); 
	}
	
	@Override
	public List<Object[]> findBySQL(String sql, Map<String, Object> params) {
		SQLQuery query = getQueryBySQL(sql, params);
		List<Object[]> q = query.list();
		if(q != null && q.size() > 0){
			return q;
		}
		return null;
	}
	
	@Override
	public List<Object[]> findBySQL(String sql, Map<String, Object> params, int page, int rows) {
		SQLQuery query = getQueryBySQL(sql, params);
		List<Object[]> q = query.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
		if(q != null){
			return q;
		}
		return null;
	}
	
	@Override
	public List<Object[]> findBySQL(String sql, int page, int rows) {
		return this.getCurrentSession().createSQLQuery(sql)
				.setFirstResult((page - 1) * rows)
				.setMaxResults(rows)
				.list();
	}
	
	@Override
	public List<T> findBySQL(String sql, T o) {
		return this.getCurrentSession().createSQLQuery(sql)
				.addEntity(o.getClass())
				.list();
	}
	
	@Override
	public List<T> findBySQL(String sql, T o, int page, int rows) {
		return this.getCurrentSession().createSQLQuery(sql)
				.addEntity(o.getClass())
				.setFirstResult((page - 1) * rows)
				.setMaxResults(rows)
				.list();
	}
	
//	@Override
//	public List<Object[]> findBySQL(String sql, Map<String, Object> returns) {
//		SQLQuery query = this.getCurrentSession().createSQLQuery(sql);
//		if (returns != null && !returns.isEmpty()) {
//			for (String key : returns.keySet()) {
//				if(returns.get(key) != null){
//					query.addScalar(key, (Type)returns.get(key));
//				}else{
//					query.addScalar(key);
//				}
//			}
//		}
//		return query.list();
//	}
	
//	@Override
//	public List<Object[]> findBySQL(String sql, Map<String, Object> returns, int page,
//			int rows) {
//		SQLQuery query = this.getCurrentSession().createSQLQuery(sql);
//		if (returns != null && !returns.isEmpty()) {
//			for (String key : returns.keySet()) {
//				if(returns.get(key) != null){
//					query.addScalar(key, (Type)returns.get(key));
//				}else{
//					query.addScalar(key);
//				}
//			}
//		}
//		return query.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
//	}
	
	@Override
	public List<T> findBySQL(String sql, Map<String, Object> returns, T o) {
		SQLQuery query = this.getCurrentSession().createSQLQuery(sql);
		if (returns != null && !returns.isEmpty()) {
			for (String key : returns.keySet()) {
				if(returns.get(key) != null){
					query.addScalar(key, (Type)returns.get(key));
				}else{
					query.addScalar(key);
				}
			}
		}
		query.addEntity(o.getClass());
		return query.list();
	}
	
	@Override
	public List<T> findBySQL(String sql, Map<String, Object> returns, T o,
			int page, int rows) {
		SQLQuery query = this.getCurrentSession().createSQLQuery(sql);
		if (returns != null && !returns.isEmpty()) {
			for (String key : returns.keySet()) {
				if(returns.get(key) != null){
					query.addScalar(key, (Type)returns.get(key));
				}else{
					query.addScalar(key);
				}
			}
		}
		query.addEntity(o.getClass());
		return query.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
	}
}
