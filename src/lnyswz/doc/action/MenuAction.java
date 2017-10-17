package lnyswz.doc.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.doc.bean.Menu;
import lnyswz.doc.bean.User;
import lnyswz.doc.service.MenuServiceI;

import com.opensymphony.xwork2.ModelDriven;

/**
 * 菜单Action
 * @author 王文阳
 *
 */
@Namespace("/admin")
@Action("menuAction")
public class MenuAction extends BaseAction implements ModelDriven<Menu> {
	private static final long serialVersionUID = 1L;
	private Menu menu = new Menu();
	private MenuServiceI menuService;
	
	/**
	 * 增加菜单
	 */
	public void add(){
		Json j = new Json();
		try{
			Menu m = menuService.add(menu);
			j.setSuccess(true);
			j.setMsg("增加菜单成功");
			j.setObj(m);
		}catch(Exception e){
			j.setMsg("增加菜单失败");
			e.printStackTrace();
		}
		writeJson(j);
	}
	/**
	 * 修改菜单
	 */
	public void edit() {
		Json j = new Json();
		try {
			//将获得的前台内容传入Service
			menuService.edit(menu);
			j.setSuccess(true);
			j.setMsg("修改菜单成功！");
		} catch (Exception e) {
			j.setMsg("修改菜单失败！");
			e.printStackTrace();
		}
		super.writeJson(j);
	}
	
	/**
	 * 删除菜单
	 */
	public void delete(){
		Json j = new Json();
		try{
			menuService.delete(menu.getId());
			j.setSuccess(true);
			j.setMsg("删除菜单成功！");
		}catch(Exception e){
			j.setMsg("删除菜单失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	/**
	 * 主页菜单
	 */
	public void menuTree(){
		List<Menu> l = new ArrayList<Menu>();
		User u = (User)session.get("user");
		//超级管理员获得全部菜单
		if(u.getUserName().equals("admin")){
			l = menuService.noAuthTree(menu.getCid());
		}else{
			l = menuService.authTree(u, menu.getCid());
			
		}
		if(l != null && l.size() > 0){
			writeJson(l);
		}
	}
	
	/**
	 * 功能按钮管理页面左侧列表
	 * 增加按钮时选择菜单
	 */
	public void doNotNeedSession_tree() {
		super.writeJson(menuService.tree(menu, false));
	}
	
	/**
	 * 编辑按钮时菜单选择列表，已展开，并选中
	 */
	public void doNotNeedSession_treeRecursive() {
		super.writeJson(menuService.tree(menu, true));
	}
	
	/**
	 * 获得菜单，供管理使用，有分页
	 */
	public void treegrid(){
		writeJson(menuService.treegrid(menu));
	}
	
	/**
	 * 只获得一级菜单
	 * 增加、修改菜单时，选择上级菜单
	 */
	public void allTopTree(){
		writeJson(menuService.allTopTree());
	}
	
	@Override
	public Menu getModel() {
		return menu;
	}
	
	@Autowired
	public void setMenuService(MenuServiceI menuService) {
		this.menuService = menuService;
	}
}
