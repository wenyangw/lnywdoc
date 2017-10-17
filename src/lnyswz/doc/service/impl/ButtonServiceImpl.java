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

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lnyswz.common.bean.DataGrid;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.doc.bean.Button;
import lnyswz.doc.bean.User;
import lnyswz.doc.model.TButton;
import lnyswz.doc.model.TDepartment;
import lnyswz.doc.model.TMenu;
import lnyswz.doc.model.TRole;
import lnyswz.doc.model.TUser;
import lnyswz.doc.service.ButtonServiceI;
import lnyswz.doc.util.ButtonComparator;

/**
 * 功能按钮实现类
 * @author 王文阳
 *
 */
@Service("buttonService")
public class ButtonServiceImpl implements ButtonServiceI {
	private BaseDaoI<TButton> buttonDao;
	private BaseDaoI<TMenu> menuDao;
	private BaseDaoI<TUser> userDao;
	
	/**
	 * 增加按钮
	 */
	@Override
	public Button add(Button b) {
		TButton t = new TButton();
		BeanUtils.copyProperties(b, t);
		//设置id
		t.setId(UUID.randomUUID().toString());
		//获得按钮所属菜单
		t.setTMenu(menuDao.get(TMenu.class, b.getMid()));
		//保存
		buttonDao.save(t);
		//将新增按钮传回前台
		Button btn = new Button();
		BeanUtils.copyProperties(t, btn);
		btn.setMid(t.getTMenu().getId());
		btn.setMname(t.getTMenu().getText());
		return btn;
	}
	/**
	 * 修改按钮
	 */
	@Override
	public void edit(Button b) {
		//通过传入的id获取功能按钮
		TButton t = buttonDao.get(TButton.class, b.getId());
		//设置功能按钮属性
		BeanUtils.copyProperties(b, t);
		t.setTMenu(menuDao.get(TMenu.class, b.getMid()));
	}
	/**
	 * 删除按钮
	 */
	@Override
	public void delete(String ids) {
		if (ids != null) {
			//拆分id
			TButton t = null;
			for (String id : ids.split(",")) {
				if (!id.trim().equals("0")) {
					t = buttonDao.get(TButton.class, id.trim());
					if (t != null) {
						buttonDao.delete(t);
					}
				}
			}
		}
	}
	
	/**
	 * 无需权限，获取功能按钮
	 * @param mid 按钮所属菜单id
	 */
	@Override
	public List<Object> noAuthBtns(String mid, int tabId) {
		//按照所属菜单id获取所有功能按钮
		String hql = "from TButton t where t.TMenu.id = :mid and t.tabId = :tabId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mid", mid);
		params.put("tabId", tabId);
		List<TButton> l = buttonDao.find(hql, params);
		if(l != null && l.size() > 0){
			//数据转换
			return changeButton(l, true);
		}
		return null;
	}

	/**
	 * 根据权限获取功能按钮
	 */
	@Override
	public List<Object> authBtns(User user, String mid, int tabId, String did){
		
//		String sql = "select btn.text, btn.iconCls, btn.handler from t_button btn"
//				+ " left join t_role_button rBtn on btn.id = rBtn.btnId"
//				+ " left join t_role r on rBtn.roleId = r.id"
//				+ " left join t_user_role ur on r.id = ur.roleId"
//				+ " where r.depId = ? and ur.userId = ? and btn.menuId = ? and btn.tabId = ?"
//				+ " order by btn.orderNum";
//		
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("0", did);
//		params.put("1", user.getId());
//		params.put("2", mid);
//		params.put("3", tabId);
//		
//		List<Object[]> lists = buttonDao.findBySQL(sql, params);
//		
//		if(lists != null && lists.size() > 0){
//			List<Object> bs = new ArrayList<Object>();
//			for(Object[] o : lists){
//				Button button = new Button();
//				button.setText(o[0].toString());
//				button.setIconCls(o[1].toString());
//				button.setHandler(o[2].toString());
//				bs.add(button);
//			}
//			return bs;
//		}
		
		TUser t = userDao.get(TUser.class, user.getId());
		//根据登录用户获取所拥有的角色
		Set<TRole> roles = t.getTRoles();
		
		if (roles != null && roles.size() > 0) {
			Set<TButton> bs = new HashSet<TButton>();
			TDepartment d = null;
			for (TRole role : roles) {
				//遍历所有角色，获取功能按钮，并删除重复的
				d = role.getTDepartment();
				if(d == null && user.getDid().equals(did)){
					bs.addAll(role.getTButtons());
				}
				if(d != null && d.getId().equals(did)){
					bs.addAll(role.getTButtons());
				}
			}
			//遍历获得的功能按钮，与传入的菜单id进行比对，删除多余的功能按钮
			if (bs != null) {
				Iterator<TButton> it = bs.iterator();
				while (it.hasNext()) {
					TButton ib = it.next();
					if (!ib.getTMenu().getId().equals(mid)) {
						it.remove();
					}else if(ib.getTabId() != tabId){
						it.remove();
					}
				}
			}
			//进行转换，并返回
			return changeButton(new ArrayList<TButton>(bs), true);
		}
		return null;
	}
	
	/**
	 * 获取功能按钮信息，供管理使用，有分页
	 */
	@Override
	public DataGrid datagrid(String mid) {
		String hql = "from TButton t";
		Map<String, Object> params = new HashMap<String, Object>();
		if(mid != null && mid.length() > 0){
			hql += " where t.TMenu.id = :mid";
			params.put("mid", mid);
		}
		String countHql = "select count(id) " + hql;
		hql = hql + " order by t.TMenu.id, t.tabId, t.orderNum";
		List<TButton> l = buttonDao.find(hql, params);
		DataGrid datagrid = new DataGrid();
		datagrid.setTotal(buttonDao.count(countHql, params));
		//进行转换，并赋值
		datagrid.setRows(changeButton(l, false));
		return datagrid;
	}
	
	/**
	 * 将表中取得的功能按钮转换为前台数据
	 * @param l
	 * @param isToolbar
	 * @return
	 */
	private List<Object> changeButton(List<TButton> l, boolean isToolbar) {
		List<Object> nl = new ArrayList<Object>();
		//排序
		Collections.sort(l, new ButtonComparator());
		for(TButton tb : l){
			Button b = new Button();
			BeanUtils.copyProperties(tb, b);
			//不是工具按钮，设置所属菜单信息
			if(!isToolbar){
				TMenu m = tb.getTMenu();
				if(m != null){
					b.setMid(m.getId());
					b.setMname(m.getText());
				}
			}
			nl.add(b);
			//工具按钮添加分隔符
//			if(isToolbar){
//				nl.add("-");
//			}
		}
		return nl;
	}
	
	@Autowired
	public void setButtonDao(BaseDaoI<TButton> buttonDao) {
		this.buttonDao = buttonDao;
	}
	@Autowired
	public void setMenuDao(BaseDaoI<TMenu> menuDao) {
		this.menuDao = menuDao;
	}
	@Autowired
	public void setUserDao(BaseDaoI<TUser> userDao) {
		this.userDao = userDao;
	}
}
