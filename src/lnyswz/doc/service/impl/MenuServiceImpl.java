package lnyswz.doc.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import lnyswz.common.bean.TreeNode;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.doc.bean.Menu;
import lnyswz.doc.bean.User;
import lnyswz.doc.model.TCatalog;
import lnyswz.doc.model.TDepartment;
import lnyswz.doc.model.TMenu;
import lnyswz.doc.model.TRole;
import lnyswz.doc.model.TUser;
import lnyswz.doc.service.MenuServiceI;
import lnyswz.doc.util.Menu2Comparator;
import lnyswz.doc.util.MenuComparator;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
 * 菜单实现表
 * @author 王文阳
 *
 */
@Service("menuService")
public class MenuServiceImpl implements MenuServiceI {
	
	private BaseDaoI<TMenu> menuDao;
	private BaseDaoI<TUser> userDao;
	private BaseDaoI<TCatalog> catalogDao;
	
	/**
	 * 增加菜单项
	 */
	@Override
	public Menu add(Menu menu) {
		TMenu t = new TMenu();
		BeanUtils.copyProperties(menu, t);
		//设置id
		t.setId(UUID.randomUUID().toString());
		//是否有上级菜单
		if(menu.getPid() != null && menu.getPid().trim().length() > 0){
			TMenu parent = menuDao.get(TMenu.class, menu.getPid());
			//设置上级菜单
			t.setTMenu(parent);
			//根据上级菜单设置模块
			t.setTCatalog(parent.getTCatalog());
			
		}else{
			//无上级菜单根据选择设置模块
			t.setTCatalog(catalogDao.get(TCatalog.class, menu.getCid()));
			//一级菜单无链接地址
			t.setUrl("");
		}
		menuDao.save(t);
		Menu m = new Menu();
		BeanUtils.copyProperties(t, m);
		return m;
	}
	
	/**
	 * 修改菜单
	 */
	@Override
	public void edit(Menu menu) {
		TMenu t = menuDao.get(TMenu.class, menu.getId());
		BeanUtils.copyProperties(menu, t);

		//是否有上级菜单
		if(menu.getPid() != null && menu.getPid().trim().length() > 0){
			//获取上级菜单
			TMenu parent = menuDao.get(TMenu.class, menu.getPid());
			//设置上级菜单
			t.setTMenu(parent);
			//根据上级菜单设置模块
			t.setTCatalog(parent.getTCatalog());
			
		}else{
			//一级根据选择设置模块
			t.setTCatalog(catalogDao.get(TCatalog.class, menu.getCid()));
			//一级菜单无链接地址
			t.setUrl("");
		}
	}
	
	/**
	 * 删除菜单项
	 */
	@Override
	public void delete(String id) {
		menuDao.delete(menuDao.get(TMenu.class, id));
	}
	
	/**
	 * 根据用户权限，获取导航菜单
	 */
	@Override
	public List<Menu> authTree(User user, String cid) {
		TUser t = userDao.get(TUser.class, user.getId());
		Set<TRole> roles = t.getTRoles();
		if(roles != null && roles.size() > 0){
			List<Menu> nl = new ArrayList<Menu>();
			//同部门权限菜单
			Set<TMenu> ms = new HashSet<TMenu>();
			//不同部门权限
			Set<TMenu> oms = new HashSet<TMenu>();
			for(TRole role : roles){
				TDepartment d = role.getTDepartment();
				if(d != null && !d.getId().equals(user.getDid())){
					//oms.addAll(role.getTMenus());
					nl.addAll(
							changeTree(
									new ArrayList<TMenu>(role.getTMenus()),
									false,
									role.getTDepartment().getId(),
									user.getId(),
									true,
									role.getTDepartment().getDepName()));
					
				}else{
					//ms.addAll(role.getTMenus());
					nl.addAll(
							changeTree(
									new ArrayList<TMenu>(role.getTMenus()),
									false,
									user.getDid(),
									user.getId(),
									false,
									null));
				}
			}
			filterByCata(cid, nl);
			Set<Menu> s = new HashSet<Menu>(nl);
			List<Menu> l = new ArrayList<Menu>(s);
			//排序
			Collections.sort(l, new Menu2Comparator());
			return l;
		}
		return null;
	}
	/**
	 * 根据模块进行筛选
	 * @param cid
	 * @param ms
	 */
	private void filterByCata(String cid, List<Menu> ms) {
		if(ms != null){
			Iterator<Menu> mIter = ms.iterator();  
	        while (mIter.hasNext()) {  
	            Menu m = mIter.next();  
	            if (!m.getCid().equals(cid))  
	                mIter.remove();  
	        }  
		}
	}
	
	/**
	 * 无权限，获得菜单
	 */
	@Override
	public List<Menu> noAuthTree(String cid) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from TMenu t";
		//cid为传入的模块id
		if(cid != null && cid.length() > 0){
			hql += " where t.TCatalog.id = :cid";
			params.put("cid", cid);
		}
		List<TMenu> l = menuDao.find(hql, params);
		return changeTree(l, false, null, 0, false, null);
	}
	
	/**
	 * 获取菜单，管理使用，异步
	 */
	@Override
	public List<Menu> treegrid(Menu menu) {
		String hql = "";
		Map<String, Object> params = new HashMap<String, Object>();
		//有传入菜单项，为二级菜单
		if(menu != null && menu.getId() != null){
			hql = "from TMenu t where t.TMenu.id = :pid";
			params.put("pid", menu.getId());
		}else {
			//获得一级菜单
			hql = "from TMenu t where t.TMenu is null";
		}
		List<TMenu> l = menuDao.find(hql, params);
		return changeTree(l, false, null, 0, false, null);
	}
	
	/**
	 * 只获得一级菜单
	 */
	@Override
	public List<Menu> allTopTree() {
		String hql = "from TMenu t where t.TMenu is null";
		return changeTree(menuDao.find(hql), true, null, 0, false, null);
	}
	
	@Override
	public List<TreeNode> tree(Menu menu, Boolean b) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from TMenu t where t.TMenu is null";
		if (menu != null && menu.getId() != null && !menu.getId().trim().equals("")) {
			hql = "from TMenu t where t.TMenu.id = :id";
			params.put("id", menu.getId());
		}
		List<TMenu> l = menuDao.find(hql, params);
		Collections.sort(l, new MenuComparator());
		List<TreeNode> tree = new ArrayList<TreeNode>();
		for (TMenu t : l) {
			tree.add(this.tree(t, b));
		}
		return tree;
	}
	
	
	private TreeNode tree(TMenu t, boolean recursive) {
		TreeNode node = new TreeNode();
		node.setId(t.getId());
		node.setText(t.getText());
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("url", t.getUrl());
		attributes.put("lx", t.getLx());
		node.setAttributes(attributes);
		if (t.getIconCls() != null) {
			node.setIconCls(t.getIconCls());
		} else {
			node.setIconCls("");
		}
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
		}
		return node;
	}
	
	
	/**
	 * 将数据库中获得的菜单项转换为传入页面的菜单项
	 * @param l
	 * @param isTop
	 * @return
	 */
	private List<Menu> changeTree(List<TMenu> l, boolean isTop, String did, int userId, boolean isOther, String odname){
		//排序
		//Collections.sort(l, new Menu2Comparator());
		List<Menu> nl = new ArrayList<Menu>();
		for(TMenu t : l){
			Menu m = new Menu();
			BeanUtils.copyProperties(t, m);
			//设置所属模块id
			m.setCid(t.getTCatalog().getId());
			//设置所属模块名称
			m.setCname(t.getTCatalog().getCatName());
			//获得上级菜单
			TMenu pm = t.getTMenu();
			//有上级菜单
			if(pm != null){
				//菜单属性
				Map<String, Object> attributes = new HashMap<String, Object>();
				//设置url
				attributes.put("url", t.getUrl());
				attributes.put("lx", t.getLx());
				attributes.put("did", did);
				attributes.put("userId", userId);
				attributes.put("query", t.getQuery());
				if(isOther){
					m.setText(m.getText() + "(" + odname + ")");
				}
				//设置上级菜单id
				m.setPid(pm.getId());
				//设置上级菜单名称
				m.setPname(pm.getText());
				//设置菜单属性
				m.setAttributes(attributes);
				//m.setState("open");
			}
			//是否有子菜单
			if(t.getTMenus().size() > 0 && !isTop){
				//设置页面菜单可展开
				m.setState("closed");
			}
			nl.add(m);
		}
		Collections.sort(nl, new Menu2Comparator());
		return nl;
	}
	
	
	
	@Autowired
	public void setMenuDao(BaseDaoI<TMenu> menuDao) {
		this.menuDao = menuDao;
	}
	
	@Autowired
	public void setUserDao(BaseDaoI<TUser> userDao) {
		this.userDao = userDao;
	}

	@Autowired
	public void setCatalogDao(BaseDaoI<TCatalog> catalogDao) {
		this.catalogDao = catalogDao;
	}
}
