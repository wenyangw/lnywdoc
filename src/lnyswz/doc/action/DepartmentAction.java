package lnyswz.doc.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.doc.bean.Department;
import lnyswz.doc.service.DepartmentServiceI;

import com.opensymphony.xwork2.ModelDriven;
/**
 * 部门Action
 * @author 王文阳
 *
 */
@Namespace("/admin")
@Action("departmentAction")
public class DepartmentAction extends BaseAction implements
		ModelDriven<Department> {
	private static final long serialVersionUID = 1L;
	private Department department = new Department();
	private DepartmentServiceI departmentService;

	/**
	 * 增加部门
	 */
	public void add() {
		Json j = new Json();
		try{
			Department d = departmentService.add(department);
			j.setSuccess(true);
			j.setMsg("增加部门成功");
			j.setObj(d);
		}catch(Exception e){
			j.setMsg("增加部门失败");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 编辑部门
	 */
	public void edit() {
		Json j = new Json();
		try {
			// 将获得的前台内容传入Service
			departmentService.edit(department);
			j.setSuccess(true);
			j.setMsg("修改部门成功！");
		} catch (Exception e) {
			j.setMsg("修改部门失败！");
			e.printStackTrace();
		}
		super.writeJson(j);
	}

	/**
	 * 删除部门
	 */
	public void delete() {
		Json j = new Json();
		try{
			departmentService.delete(department.getIds());
			j.setSuccess(true);
			j.setMsg("删除部门成功！");
		}catch(Exception e){
			j.setMsg("删除部门失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 获得部门，供管理使用，有分页
	 */
	public void datagrid() {
		writeJson(departmentService.datagrid(department));
	}
	
	/**
	 * 获得部门，供选择使用，无分页
	 */
	public void listDeps() {
		writeJson(departmentService.listDeps());
	}
	
	/**
	 * 获得经营部门列表，供选择使用，无分页
	 */
	public void listYws() {
		writeJson(departmentService.listYws(department));
	}
	
	

	@Override
	public Department getModel() {
		return department;
	}

	@Autowired
	public void setDepartmentService(DepartmentServiceI departmentService) {
		this.departmentService = departmentService;
	}
}
