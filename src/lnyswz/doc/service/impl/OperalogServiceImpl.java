package lnyswz.doc.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lnyswz.common.bean.DataGrid;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.common.util.DateUtil;
import lnyswz.doc.bean.Operalog;
import lnyswz.doc.model.TDepartment;
import lnyswz.doc.model.TMenu;
import lnyswz.doc.model.TOperalog;
import lnyswz.doc.model.TUser;
import lnyswz.doc.service.OperalogServiceI;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("operalogService")
public class OperalogServiceImpl implements OperalogServiceI {
	private BaseDaoI<TOperalog> opeDao;
	private BaseDaoI<TUser> userDao;
	private BaseDaoI<TDepartment> depDao;
	private BaseDaoI<TMenu> menuDao;

	/**
	 * 添加日志方法
	 * 
	 * @param userId
	 *            用户id
	 * @param bmbh
	 *            部门编号
	 * @param menuId
	 *            操作菜单id
	 * @param keyId
	 *            操作关键字
	 * @param operation
	 *            操作内容
	 * @param baseDao
	 *            操作Dao
	 */
	public static void addOperalog(int userId, String bmbh, String menuId,
			String keyId, String operation, BaseDaoI<TOperalog> baseDao) {
		TOperalog ope = new TOperalog();
		ope.setBmbh(bmbh);
		ope.setKeyId(keyId);
		ope.setUserId(userId);
		ope.setLogTime(new Date());
		ope.setMenuId(menuId);
		ope.setOperation(operation);
		baseDao.save(ope);
	}

	/**
	 * 删除操作日子（批量删除）
	 */
	@Override
	public void delete(String ids) {
		if (ids != null) {
			// 拆分id
			for (String id : ids.split(",")) {
				if (!id.trim().equals("0")) {
					TOperalog ope = opeDao.load(TOperalog.class, id);
					opeDao.delete(ope);
				}
			}
		}
	}

	public DataGrid datagrid(Operalog ope) {
		DataGrid dg = new DataGrid();
		// 拼写hql语句
		String hql = "from TOperalog t";
		Map<String, Object> params = new HashMap<String, Object>();
		// 将logTime 作为筛选条件 初始化为查询当月信息
		hql += " where t.logTime >= :logTime and t.logTime <= :logTimeEnd ";
		if (ope.getLogTime() != null && ope.getLogTimeEnd() != null) {
			params.put("logTime", ope.getLogTime());
			params.put("logTimeEnd", DateUtil.dateIncreaseByDay(ope
					.getLogTimeEnd(), 1));
		} else {
			params.put("logTimeEnd", DateUtil.dateIncreaseByDay(new Date(), 1));
			params.put("logTime", DateUtil.stringToDate(DateUtil
					.getFirstDateInMonth(new Date())));
		}
		// 拼写查询到的数据条数语句
		String totalHql = "select count(*) " + hql;
		List<Operalog> nl = new ArrayList<Operalog>();
		// 查询数据
		List<TOperalog> l = opeDao.find(hql, params, ope.getPage(), ope
				.getRows());
		// 遍历查询到的数据 将数据进行处理
		for (TOperalog t : l) {
			Operalog o = new Operalog();
			BeanUtils.copyProperties(t, o);
			// 查询此条数据相关的用户信息
			TUser user = userDao.load(TUser.class, t.getUserId());
			o.setRealName(user.getRealName());
			// 查询此条数据相关的菜单信息
			if (!t.getMenuId().equals("")) {
				TMenu menu = menuDao.load(TMenu.class, t.getMenuId());
				o.setMenuName(menu.getText());
			}
			// 查询此条数据相关的部门信息
			TDepartment dep = depDao.load(TDepartment.class, t.getBmbh());
			o.setBmmc(dep.getDepName());
			nl.add(o);
		}
		dg.setTotal(opeDao.count(totalHql, params));
		dg.setRows(nl);
		return dg;
	}

	@Autowired
	public void setOpeDao(BaseDaoI<TOperalog> opeDao) {
		this.opeDao = opeDao;
	}

	@Autowired
	public void setUserDao(BaseDaoI<TUser> userDao) {
		this.userDao = userDao;
	}

	@Autowired
	public void setDepDao(BaseDaoI<TDepartment> depDao) {
		this.depDao = depDao;
	}

	@Autowired
	public void setMenuDao(BaseDaoI<TMenu> menuDao) {
		this.menuDao = menuDao;
	}
}
