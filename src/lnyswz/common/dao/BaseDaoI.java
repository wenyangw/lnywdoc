package lnyswz.common.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;

public interface BaseDaoI<T> {

	public Serializable save(T o);

	public void delete(T o);

	public void update(T o);

	public void saveOrUpdate(T o);

	public T load(Class<T> c, Serializable id);
	
	public T get(Class<T> c, Serializable id);
	
	public T get(String hql);

	public T get(String hql, Map<String, Object> params);

	public List<T> find(String hql);
	
	public List<T> find(String hql, Map<String, Object> params);

	public List<T> find(String hql, int page, int rows);

	public List<T> find(String hql, Map<String, Object> params, int page, int rows);

	public Long count(String hql);
	
	public Long countBySQL(String sql);

	public Long count(String hql, Map<String, Object> params);
	
	public int executeHql(String hql);

	public int executeHql(String hql, Map<String, Object> params);
	
	public int updateBySQL(String sql);
	
	public int updateBySQL(String sql, Map<String, Object> params);
	
	public Object[] getMBySQL(String sql, Map<String, Object> params);
	public Object getBySQL(String sql, Map<String, Object> params);
	public Object getBySQL(String sql, Map<String, Object> params, Map<String, Object> returns);
	
	public List<Object[]> findBySQL(String sql);
	public List<Object[]> findBySQL(String sql, int page, int rows);
	
	public List<T> findBySQL(String sql, T o);
	public List<T> findBySQL(String sql, T o, int page, int rows);

	public List<Object[]> findBySQL(String sql, Map<String, Object> params);
	public List<Object[]> findBySQL(String sql, Map<String, Object> params, int page, int rows);
	
	public List<T> findBySQL(String sql, Map<String, Object> returns, T o);
	public List<T> findBySQL(String sql, Map<String, Object> returns, T o, int page, int rows);
	
	public Long countSQL(String sql);
	public Long countSQL(String sql, Map<String, Object> params);

}
