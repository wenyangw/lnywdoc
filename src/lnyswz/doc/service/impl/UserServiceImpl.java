package lnyswz.doc.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
import lnyswz.common.dao.BaseDaoI;
import lnyswz.common.util.Encrypt;
import lnyswz.doc.bean.User;
import lnyswz.doc.model.TDepartment;
import lnyswz.doc.model.TMenu;
import lnyswz.doc.model.TOperalog;
import lnyswz.doc.model.TPost;
import lnyswz.doc.model.TRole;
import lnyswz.doc.model.TUser;
import lnyswz.doc.service.UserServiceI;
import lnyswz.doc.util.UserComparator;

/**
 * 用户实现类
 * 
 * @author 王文阳
 * 
 */
@Service("userService")
public class UserServiceImpl implements UserServiceI {
	private final static Logger logger = Logger
			.getLogger(UserServiceImpl.class);
	private BaseDaoI<TUser> userDao;
	private BaseDaoI<TRole> roleDao;
	private BaseDaoI<TDepartment> departmentDao;
	private BaseDaoI<TPost> postDao;
	private BaseDaoI<TOperalog> operalogDao;

	/**
	 * 保存用户
	 */
	@Override
	public User add(User user) {
		TUser t = new TUser();
		BeanUtils.copyProperties(user, t);
		//t.setId(UUID.randomUUID().toString());
		t.setPassword(Encrypt.e(user.getPassword()));
		// 创建时间
		t.setCreateTime(new Date());
		// 设置部门
		TDepartment d = null;
		if (user.getDid() != null && user.getDid().trim().length() > 0) {
			d = departmentDao.load(TDepartment.class, user.getDid());
			t.setTDepartment(d);
		}
		// 设置职务
		TPost p = null;
		if (user.getPostId() != null && user.getPostId().trim().length() > 0) {
			p = postDao.load(TPost.class, user.getPostId());
			t.setTPost(p);
		}
		userDao.save(t);
		User u = new User();
		BeanUtils.copyProperties(t, u);
		// 返回数据处理
		if (t.getSex() != null && t.getSex().trim().length() > 0) {
			u.setCsex(getSex(t.getSex()));
		}
		if (d != null) {
			u.setDid(d.getId());
			u.setDname(d.getDepName());
		}
		if (p != null) {
			u.setPostId(p.getId());
			u.setPostName(p.getPostName());
		}
//		OperalogServiceImpl.addOperalog(user.getOperaId(), user.getOperaDepId(), user.getMenuId(), String.valueOf(u.getId()), "增加用户", operalogDao);
		return u;
	}

	/**
	 * 编辑用户
	 */
	@Override
	public void edit(User user) {
		TUser t = userDao.load(TUser.class, user.getId());
		//排除密码自动修改
		BeanUtils.copyProperties(user, t, new String[]{"password"});
		if(user.getPassword() != null && user.getPassword().trim().length() > 0){
			t.setPassword(Encrypt.e(user.getPassword()));
		}
		// 设置修改时间
		t.setModifyTime(new Date());
		t.setTDepartment(departmentDao.load(TDepartment.class, user.getDid()));
		t.setTPost(postDao.load(TPost.class, user.getPostId()));
		// 设置用户拥有角色
		if (user.getRoleIds() != null) {
			Set<TRole> roles = new HashSet<TRole>();
			for (String id : user.getRoleIds().split(",")) {
				TRole role = roleDao.load(TRole.class, id.trim());
				roles.add(role);
			}
			t.setTRoles(roles);
		}
//		OperalogServiceImpl.addOperalog(user.getOperaId(), user.getOperaDepId(), user.getMenuId(), String.valueOf(user.getId()), "修改用户", operalogDao);
	}
	
	/**
	 * 修改密码
	 */
	@Override
	public void editPassword(User user) {
		TUser t = userDao.load(TUser.class, user.getId());
		t.setPassword(Encrypt.e(user.getPassword()));
	}

	/**
	 * 验证原始密码
	 */
	@Override
	public boolean checkPassword(User user) {
		boolean isOK = false;
		TUser u = userDao.load(TUser.class, user.getId());
		if (u.getPassword().equals(Encrypt.e(user.getPassword()))) {
			isOK = true;
		}
		return isOK;
	}

	/**
	 * 删除用户
	 */
	@Override
	public void delete(User user) {
		TUser t = userDao.load(TUser.class, user.getId());
		if (t != null) {
			userDao.delete(t);
		}
//		OperalogServiceImpl.addOperalog(user.getOperaId(), user.getOperaDepId(), user.getMenuId(), String.valueOf(user.getId()), "删除用户", operalogDao);
	}

	/**
	 * 返回用户信息，供管理使用
	 */
	@Override
	public DataGrid datagrid(User user) {
		DataGrid dg = new DataGrid();
		List<TUser> l = new ArrayList<TUser>();
		List<User> nl = new ArrayList<User>();
		String hql = "from TUser t";
		String countHql = "select count(id) " + hql;
		hql += " order by t.TDepartment.orderNum, t.TPost.orderNum, t.orderNum";
		l = userDao.find(hql, user.getPage(), user.getRows());
		if (l != null && l.size() > 0) {
			//Collections.sort(l, new UserComparator());

			for (TUser t : l) {
				User u = new User();
				BeanUtils.copyProperties(t, u);
				// 用户所属部门
				if (t.getTDepartment() != null) {
					u.setDid(t.getTDepartment().getId());
					u.setDname(t.getTDepartment().getDepName());
				}
				if (t.getTPost() != null) {
					u.setPostId(t.getTPost().getId());
					u.setPostName(t.getTPost().getPostName());
				}
				if (t.getSex() != null && t.getSex().trim().length() > 0) {
					u.setCsex(getSex(t.getSex()));
				}
				// 用户拥有角色
				Set<TRole> roles = t.getTRoles();
				if (roles != null && roles.size() > 0) {
					String roleIds = "";
					String roleNames = "";
					boolean b = false;
					for (TRole role : roles) {
						if (role != null) {
							if (b) {
								roleIds += ",";
								roleNames += ",";
							}
							roleIds += role.getId();
							roleNames += role.getRoleName();
							b = true;
						}

					}
					u.setRoleIds(roleIds);
					u.setRoleNames(roleNames);
				}

				nl.add(u);
			}
		}
		dg.setTotal(userDao.count(countHql));
		dg.setRows(nl);
		return dg;
	}

	/**
	 * 用户登录
	 */
	@Override
	public User login(User user) {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("userName", user.getUserName());
		m.put("password", Encrypt.e(user.getPassword()));
		String hql = "from TUser t where t.userName = :userName and t.password = :password";
		TUser t = userDao.get(hql, m);
		
		if (t != null) {
			// 设置登录时间
			t.setLastTime(new Date());
			User u = new User();
			BeanUtils.copyProperties(t, u);
			if (t.getTDepartment() != null) {
				u.setDid(t.getTDepartment().getId());
				u.setDname(t.getTDepartment().getDepName());
				
			}
			if (null != t.getTPost()) {
				u.setPostId(t.getTPost().getId());
				u.setPostName(t.getTPost().getPostName());
			}
			OperalogServiceImpl.addOperalog(u.getId(), u.getDid(), "", String.valueOf(user.getId()), "登录系统", operalogDao);
			return u;
		}
	
		return null;
	}

	/**
	 * 返回业务员列表
	 */
	@Override
	public List<User> listYwys(String depId) {
		String hql = "from TUser t where t.isYwy = '1' and t.TDepartment.id = :depId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("depId", depId);
		List<TUser> l = userDao.find(hql, params);
		return changeUsers(l, false);
	}
	
	/**
	 * 返回保管员列表
	 */
	@Override
	public List<User> listBgys(User user) {
		String hql = "from TUser t where t.isBgy = '1' and t.TDepartment.id = :depId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("depId", user.getDid());
		List<TUser> l = userDao.find(hql, params);
		return changeUsers(l, false);
	}
	
	public List<User> changeUsers(List<TUser> l, boolean isAuth){
		List<User> nl = new ArrayList<User>();
		Collections.sort(l, new UserComparator());

		for (TUser t : l) {
			User u = new User();
			BeanUtils.copyProperties(t, u);
			// 用户所属部门
			if (t.getTDepartment() != null) {
				u.setDid(t.getTDepartment().getId());
				u.setDname(t.getTDepartment().getDepName());
			}
			if (t.getTPost() != null) {
				u.setPostId(t.getTPost().getId());
				u.setPostName(t.getTPost().getPostName());
			}
			if (t.getSex() != null && t.getSex().trim().length() > 0) {
				u.setCsex(getSex(t.getSex()));
			}
			if(isAuth){
				// 用户拥有角色
				Set<TRole> roles = t.getTRoles();
				if (roles != null && roles.size() > 0) {
					String roleIds = "";
					String roleNames = "";
					boolean b = false;
					for (TRole role : roles) {
						if (role != null) {
							if (b) {
								roleIds += ",";
								roleNames += ",";
							}
							roleIds += role.getId();
							roleNames += role.getRoleName();
							b = true;
						}
	
					}
					u.setRoleIds(roleIds);
					u.setRoleNames(roleNames);
				}
			}

			nl.add(u);
		}
		return nl;

	}
	
	/**
	 * 处理性别
	 * 
	 * @param sex
	 * @return
	 */
	private String getSex(String sex) {
		if (sex.equals("1")) {
			return "男";
		} else if (sex.equals("0")) {
			return "女";
		}
		return null;
	}

	@Autowired
	public void setUserDao(BaseDaoI<TUser> userDao) {
		this.userDao = userDao;
	}

	@Autowired
	public void setRoleDao(BaseDaoI<TRole> roleDao) {
		this.roleDao = roleDao;
	}

	@Autowired
	public void setDepartmentDao(BaseDaoI<TDepartment> departmentDao) {
		this.departmentDao = departmentDao;
	}

	@Autowired
	public void setPostDao(BaseDaoI<TPost> postDao) {
		this.postDao = postDao;
	}

	@Autowired
	public void setOperalogDao(BaseDaoI<TOperalog> operalogDao) {
		this.operalogDao = operalogDao;
	}
}
