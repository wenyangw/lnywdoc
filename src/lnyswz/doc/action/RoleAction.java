package lnyswz.doc.action;


import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.doc.bean.Role;
import lnyswz.doc.service.RoleServiceI;

import com.opensymphony.xwork2.ModelDriven;

/**
 * 角色Action
 * @author 王文阳
 *
 */
@Namespace("/admin")
@Action("roleAction")
public class RoleAction extends BaseAction implements ModelDriven<Role> {
	private static final long serialVersionUID = 1L;
	private Role role = new Role();
	private RoleServiceI roleService;
	
	/**
	 * 增加角色
	 */
	public void add(){
		Json j = new Json();
		try{
			Role r = roleService.add(role);
			j.setSuccess(true);
			j.setMsg("增加角色成功!");
			j.setObj(r);
		}catch(Exception e){
			j.setMsg("增加角色失败!");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	/**
	 * 编辑角色
	 */
	public void edit() {
		Json j = new Json();
		try {
			//将获得的前台内容传入Service
			roleService.edit(role);
			j.setSuccess(true);
			j.setMsg("修改角色成功！");
		} catch (Exception e) {
			j.setMsg("修改角色失败！");
			e.printStackTrace();
		}
		super.writeJson(j);
	}
	
	/**
	 * 删除角色
	 */
	public void delete(){
		Json j = new Json();
		try{
			roleService.delete(role.getIds());
			j.setSuccess(true);
			j.setMsg("删除角色成功！");
		}catch(Exception e){
			j.setMsg("删除角色失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	/**
	 * 角色授权
	 */
	public void auth(){
		Json j = new Json();
		roleService.auth(role);
		j.setSuccess(true);
		j.setMsg("角色授权成功！");
		writeJson(j);
	}
	
	/**
	 * 获得角色，供选择使用，无分页
	 */
	public void listRoles(){
		writeJson(roleService.listRoles());
	}
	
	/**
	 * 获得角色，供管理使用，有分页
	 */
	public void datagrid(){
		writeJson(roleService.datagrid(role));
	}
	
	/**
	 * 授权时，列出菜单、按钮树，已授权的选中
	 */
	public void tree(){
		writeJson(roleService.tree(role));
	}
	
	@Override
	public Role getModel() {
		return role;
	}
	
	@Autowired
	public void setRoleService(RoleServiceI roleService) {
		this.roleService = roleService;
	}
}
