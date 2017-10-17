package lnyswz.doc.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.TreeNode;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.doc.bean.Role;
import lnyswz.doc.model.TButton;
import lnyswz.doc.model.TDepartment;
import lnyswz.doc.model.TMenu;
import lnyswz.doc.model.TRole;
import lnyswz.doc.service.RoleServiceI;
import lnyswz.doc.util.ButtonComparator;
import lnyswz.doc.util.MenuComparator;
/**
 * 角色实现表
 * @author 王文阳
 *
 */
@Service("roleService")
public class RoleServiceImpl implements RoleServiceI{
	private final static Logger logger = Logger.getLogger(RoleServiceImpl.class);
	
	private BaseDaoI<TRole> roleDao;
	private BaseDaoI<TMenu> menuDao;
	private BaseDaoI<TButton> btnDao;
	private BaseDaoI<TDepartment> depDao;
	
	
	/**
	 * 增加角色
	 */
	@Override
	public Role add(Role role) {
		TRole t = new TRole();
		BeanUtils.copyProperties(role, t);
		t.setId(UUID.randomUUID().toString());
		//关联部门的处理
		String did = role.getDid(); 
		if(did != null && did.trim().length() > 0){
			t.setTDepartment(depDao.get(TDepartment.class, did));
		}
		roleDao.save(t);
		Role r = new Role();
		BeanUtils.copyProperties(t, r);
		TDepartment td = t.getTDepartment();
		if(td != null){
			r.setDid(td.getId());
			r.setDname(td.getDepName());
		}
		return r;
	}
	
	/**
	 * 修改角色
	 */
	@Override
	public void edit(Role role) {
		TRole t = roleDao.get(TRole.class, role.getId());
		BeanUtils.copyProperties(role, t);
		//关联部门的处理
		String did = role.getDid(); 
		if(did != null && did.trim().length() > 0){
			t.setTDepartment(depDao.get(TDepartment.class, did));
		}else{
			t.setTDepartment(null);
		}
	}

	
	/**
	 * 删除角色
	 */
	@Override
	public void delete(String ids) {
		if (ids != null) {
			//拆分id
			for (String id : ids.split(",")) {
				if (!id.trim().equals("0")) {
					TRole t = roleDao.get(TRole.class, id.trim());
					if (t != null) {
						roleDao.delete(t);
					}
				}
			}
		}
		
	}
	/**
	 * 角色授权
	 */
	@Override
	public void auth(Role role) {
		TRole r = roleDao.get(TRole.class, role.getId());
		Set<TMenu> ms = new HashSet<TMenu>();
		for(String mid : role.getMenuIds().split(",")){
			ms.add(menuDao.get(TMenu.class, mid));
		}
		r.setTMenus(ms);
		//判断是否有功能按钮
		if(role.getBtnIds().trim().length() != 0){
			Set<TButton> bs = new HashSet<TButton>();
			for(String bid : role.getBtnIds().split(",")){
				bs.add(btnDao.get(TButton.class, bid));
			}
			r.setTButtons(bs);
		}
		
		
	}
	
	/**
	 * 返回角色信息，供选择使用，无分页
	 */
	@Override
	public List<Role> listRoles() {
		String hql = "from TRole t";
		String orderBy = " order by t.roleName";
		List<TRole> l = roleDao.find(hql + orderBy); 
		return changeRole(l);
	}
	
	/**
	 * 数据转换
	 * @param l
	 * @return
	 */
	private List<Role> changeRole(List<TRole> l) {
		List<Role> nl = new ArrayList<Role>();
		for(TRole t : l){
			Role r = new Role();
			BeanUtils.copyProperties(t, r);
			r.setText(t.getRoleName());
			nl.add(r);
		}
		return nl;
	}
	
	/**
	 * 返回角色信息，供管理使用，有分页
	 */
	@Override
	public DataGrid datagrid(Role role) {
		DataGrid dg = new DataGrid();
		List<TRole> l = new ArrayList<TRole>();
		List<Role> nl = new ArrayList<Role>();
		String hql = "from TRole t";
		String countHql = "select count(id) " + hql;
		String orderBy = " order by t.roleName";
		l = roleDao.find(hql + orderBy, role.getPage(), role.getRows());
		for(TRole t : l){
			Role r = new Role();
			BeanUtils.copyProperties(t, r);
			TDepartment td = t.getTDepartment();
			if(td != null){
				r.setDid(td.getId());
				r.setDname(td.getDepName());
			}
			//处理角色拥有的菜单
			Set<TMenu> ms = t.getTMenus(); 
			if(ms != null && ms.size() > 0){
				boolean b = false;
				String menuIds = "";
				String menuNames = "";
				for(TMenu m : ms){
					if(b){
						menuIds += ",";
						menuNames += ",";
					}
					menuIds += m.getId();
					menuNames += m.getText();
					b = true;
				}
				r.setMenuIds(menuIds);
				r.setMenuNames(menuNames);
			}
			//处理角色拥有的功能按钮
			Set<TButton> bs = t.getTButtons(); 
			if(bs != null && bs.size() > 0){
				boolean b = false;
				String btnIds = "";
				for(TButton btn : bs){
					if(b){
						btnIds += ",";
					}
					btnIds += btn.getId();
					b = true;
				}
				r.setBtnIds(btnIds);
			}
			
			nl.add(r);
		}
		dg.setTotal(roleDao.count(countHql));
		dg.setRows(nl);
		return dg;
	}
	/**
	 * 获取授权页面的树
	 */
	@Override
	public List<TreeNode> tree(Role r) {
		String hql = "from TMenu t where t.TMenu is null";
		List<TMenu> l = menuDao.find(hql);
		Collections.sort(l, new MenuComparator());
		List<TreeNode> tree = new ArrayList<TreeNode>();
		for (TMenu t : l) {
			tree.add(this.tree(t, true));
		}
		return tree;
	}
	/**
	 * 获取授权页面的树的子节点
	 * @param t
	 * @param recursive
	 * @return
	 */
	private TreeNode tree(TMenu t, boolean recursive) {
		TreeNode node = new TreeNode();
		node.setId(t.getId());
		node.setText(t.getText());
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("type", "Menu");
		node.setAttributes(attributes);
		if (t.getIconCls() != null) {
			node.setIconCls(t.getIconCls());
		} else {
			node.setIconCls("");
		}
		//有子菜单继续获得子菜单，无子菜单获得对应页面的功能按钮
		if (t.getTMenus() != null && t.getTMenus().size() > 0) {
			node.setState("closed");
			if (recursive) {// 递归查询子节点
				List<TMenu> l = new ArrayList<TMenu>(t.getTMenus());
				Collections.sort(l, new MenuComparator());// 排序
				List<TreeNode> children = new ArrayList<TreeNode>();
				for (TMenu r : l) {
					TreeNode tn = tree(r, true);
					children.add(tn);
				}
				node.setChildren(children);
				node.setState("open");
			}
		}else{
			//node.setState("closed");
			String hql = "from TButton t where t.TMenu.id = :mid";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("mid", t.getId());
			List<TButton> bl = btnDao.find(hql, params);
			if (bl != null && bl.size() > 0) {
				Collections.sort(bl, new ButtonComparator());// 排序
				List<TreeNode> children = new ArrayList<TreeNode>();
				for (TButton b : bl) {
					TreeNode tn = new TreeNode();
					tn.setId(b.getId());
					tn.setText(b.getText());
					Map<String, Object> attributesBtn = new HashMap<String, Object>();
					attributesBtn.put("type", "Button");
					tn.setAttributes(attributesBtn);
					if (b.getIconCls() != null) {
						tn.setIconCls(b.getIconCls());
					} else {
						tn.setIconCls("");
					}
					children.add(tn);
				}
				node.setChildren(children);
				node.setState("open");
			}
		}
		return node;
	}
	
	@Autowired
	public void setRoleDao(BaseDaoI<TRole> roleDao) {
		this.roleDao = roleDao;
	}
	
	@Autowired
	public void setMenuDao(BaseDaoI<TMenu> menuDao) {
		this.menuDao = menuDao;
	}
	
	@Autowired
	public void setBtnDao(BaseDaoI<TButton> btnDao) {
		this.btnDao = btnDao;
	}

	@Autowired
	public void setDepDao(BaseDaoI<TDepartment> depDao) {
		this.depDao = depDao;
	}
	
}
