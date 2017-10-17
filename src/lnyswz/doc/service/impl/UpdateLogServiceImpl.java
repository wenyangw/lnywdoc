package lnyswz.doc.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lnyswz.common.bean.DataGrid;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.doc.bean.UpdateLog;
import lnyswz.doc.model.TUpdateLog;
import lnyswz.doc.service.UpdateLogServiceI;

@Service("updateLogService")
public class UpdateLogServiceImpl implements UpdateLogServiceI {
	private BaseDaoI<TUpdateLog> updateDao;

	/**
	 * 增加更新数据
	 */
	@Override
	public UpdateLog add(UpdateLog update) {
		TUpdateLog up = new TUpdateLog();
		BeanUtils.copyProperties(update, up);
		up.setUpdateTime(new Date());
		updateDao.save(up);
		update.setUpdateTime(up.getUpdateTime());
		return update;
	}

	/**
	 * 修改更新数据
	 */
	@Override
	public void edit(UpdateLog updete) {
		TUpdateLog up = updateDao.load(TUpdateLog.class, updete.getId());
		BeanUtils.copyProperties(updete, up);
		up.setUpdateTime(new Date());
	}

	/**
	 * 删除更新数据
	 */
	public void delete(String ids) {
		if (ids != null) {
			for (String id : ids.split(",")) {
				if (!id.trim().equals("0")) {
					TUpdateLog up = updateDao.load(TUpdateLog.class, id);
					updateDao.delete(up);
				}
			}
		}
	}

	/**
	 * 首页显示更新数据
	 */
	@Override
	public List<UpdateLog> listUpdateLog(UpdateLog update) {
		String hql = "from TUpdateLog t order by t.updateTime desc";
		List<TUpdateLog> list = updateDao.find(hql);
		List<UpdateLog> nl = new ArrayList<UpdateLog>();
		for (TUpdateLog l : list) {
			UpdateLog up = new UpdateLog();
			BeanUtils.copyProperties(l, up);
			nl.add(up);
		}
		return nl;
	}

	@Override
	public DataGrid datagrid(UpdateLog update) {
		DataGrid dg = new DataGrid();
		String hql = "from TUpdateLog t";
		String totalHql = "select count(*) " + hql;
		List<TUpdateLog> list = updateDao.find(hql, update.getPage(), update
				.getRows());
		dg.setTotal(updateDao.count(totalHql));
		dg.setRows(changeUpdateLog(list));
		return dg;
	}

	/**
	 * 表数据转换前台数据
	 * 
	 * @param list
	 * @return
	 */
	public List<UpdateLog> changeUpdateLog(List<TUpdateLog> list) {
		List<UpdateLog> nl = new ArrayList<UpdateLog>();
		for (TUpdateLog l : list) {
			UpdateLog n = new UpdateLog();
			BeanUtils.copyProperties(l, n);
			nl.add(n);
		}
		return nl;
	}

	@Autowired
	public void setUpdateDao(BaseDaoI<TUpdateLog> updateDao) {
		this.updateDao = updateDao;
	}

}
