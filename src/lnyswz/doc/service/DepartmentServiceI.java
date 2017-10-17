package lnyswz.doc.service;

import java.util.List;

import lnyswz.common.bean.DataGrid;
import lnyswz.doc.bean.Department;

public interface DepartmentServiceI {
	public Department add(Department department);
	public void edit(Department department);
	public void delete(String ids);
	public DataGrid datagrid(Department department);
	public List<Department> listDeps();
	public List<Department> listYws(Department department);
}
