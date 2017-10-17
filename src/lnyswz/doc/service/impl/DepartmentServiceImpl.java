package lnyswz.doc.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lnyswz.common.bean.DataGrid;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.doc.bean.Department;
import lnyswz.doc.model.TDepartment;
import lnyswz.doc.service.DepartmentServiceI;
import lnyswz.doc.util.DepartmentComparator;
/**
 * 部门实现类
 * @author 王文阳
 *
 */
@Service("departmentService")
public class DepartmentServiceImpl implements DepartmentServiceI {
	private BaseDaoI<TDepartment> departmentDao;
	
	/**
	 * 增加部门
	 */
	@Override
	public Department add(Department department) {
		TDepartment t = new TDepartment();
		BeanUtils.copyProperties(department, t);
		departmentDao.save(t);
		Department d = new Department();
		BeanUtils.copyProperties(t, d);
		return d;
	}
	
	/**
	 * 编辑部门
	 */
	@Override
	public void edit(Department department) {
		TDepartment t = departmentDao.get(TDepartment.class, department.getId());
		BeanUtils.copyProperties(department, t);
	}
	
	/**
	 * 删除部门
	 */
	@Override
	public void delete(String ids) {
		if (ids != null) {
			//传入多条删除id,拆分id
			for (String id : ids.split(",")) {
				if (!id.trim().equals("0")) {
					TDepartment t = departmentDao.get(TDepartment.class, id.trim());
					if (t != null) {
						departmentDao.delete(t);
					}
				}
			}
		}
	}
	
	/**
	 * 获得部门列表，供管理用，有分页
	 */
	@Override
	public DataGrid datagrid(Department department) {
		DataGrid dg = new DataGrid();
		List<TDepartment> l = new ArrayList<TDepartment>();
		String hql = "from TDepartment t";
		//记录总数
		String countHql = "select count(id) " + hql;
		l = departmentDao.find(hql, department.getPage(), department.getRows());
		dg.setTotal(departmentDao.count(countHql));
		dg.setRows(changeDep(l));
		return dg;
	}
	
	/**
	 * 获得所有部门，供选择用，无分页
	 */
	@Override
	public List<Department> listDeps() {
		String hql = "from TDepartment t";
		List<TDepartment> l = departmentDao.find(hql);
		if(l != null && l.size() > 0){
			return changeDep(l);
		}
		return null;
	}
	
	/**
	 * 获得经营部门列表，供选择用，无分页
	 */
	@Override
	public List<Department> listYws(Department department) {
		String hql = "from TDepartment t";
		Map<String, Object> params = new HashMap<String, Object>();
		if(department.getId().compareTo("10") < 0){
			hql += " where t.id = :id";
			params.put("id", department.getId());
		}else{
			hql += " where t.id < '09'";
		}
		
		List<TDepartment> l = departmentDao.find(hql, params);
		if(l != null && l.size() > 0){
			return changeDep(l);
		}
		return null;
	}
	/**
	 * 数据转换
	 * @param l
	 * @return
	 */
	private List<Department> changeDep(List<TDepartment> l) {
		Collections.sort(l, new DepartmentComparator());
		List<Department> nl = new ArrayList<Department>();
		for(TDepartment t : l){
			Department d = new Department();
			BeanUtils.copyProperties(t, d);
			nl.add(d);
		}
		return nl;
	}

	@Autowired
	public void setDepartmentDao(BaseDaoI<TDepartment> departmentDao) {
		this.departmentDao = departmentDao;
	}
}
