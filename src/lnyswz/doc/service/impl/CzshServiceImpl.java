package lnyswz.doc.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lnyswz.common.bean.DataGrid;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.common.util.DateUtil;
import lnyswz.doc.bean.Czsh;
import lnyswz.doc.bean.PersonSp;
import lnyswz.doc.model.TCzsh;
import lnyswz.doc.model.TOperalog;
import lnyswz.doc.model.TPerson;
import lnyswz.doc.model.TPersonSp;
import lnyswz.doc.service.CzshServiceI;
import lnyswz.doc.service.PersonSpServiceI;
import lnyswz.doc.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;


/**
 * 审批操作实现表
 * @author 王文阳
 *
 */
@Service("czshService")
public class CzshServiceImpl implements CzshServiceI {

	private BaseDaoI<TCzsh> czshDao;
	private BaseDaoI<TPersonSp> personSpDao;
	private BaseDaoI<TPerson> personDao;

	@Override
	public DataGrid datagrid(Czsh czsh) {
		String hql = "from TCzsh t";
		List<TCzsh> czshs = czshDao.find(hql + " order by t.createTime desc", czsh.getPage(), czsh.getRows());

		List<Czsh> rows = null;
		if(czshs.size() > 0){
			rows = new ArrayList<Czsh>();
			Czsh c = null;
			for (TCzsh tCzsh : czshs) {
				c = new Czsh();
				TPersonSp tPersonSp = PersonSpServiceImpl.getTPersonSp(tCzsh.getPersonId(), tCzsh.getTimeStamp(), personSpDao);
				c.setPersonId(tCzsh.getPersonId());
				c.setPersonName(tPersonSp.getPersonName());
				c.setTimeStamp(tCzsh.getTimeStamp());
				c.setCreateName(tPersonSp.getCreateName());
				c.setCreateTime(tPersonSp.getCreateTime());
				c.setAuditName(tCzsh.getCreateName());
				c.setAuditTime(tCzsh.getCreateTime());
				c.setStatus(tPersonSp.getStatus());
				c.setNeedAudit(tCzsh.getNeedAudit());
				c.setIsAudit(tCzsh.getIsAudit());
				c.setBz(tCzsh.getBz());

				rows.add(c);
			}
		}

		DataGrid dg = new DataGrid();
		dg.setRows(rows);
		dg.setTotal(czshDao.count("select count(*) " + hql));
		return dg;
	}
	@Override
	public String getAuditBz(Czsh czsh) {
		TPerson tp = personDao.load(TPerson.class, czsh.getPersonId());
		
		String hql = "from TCzsh t where t.personId = :personId and t.timeStamp = :timeStamp and t.isAudit = :isAudit ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("personId", czsh.getPersonId());
		params.put("timeStamp", tp.getTimeStamp());
		params.put("isAudit", "9");
		
		TCzsh t = czshDao.get(hql, params);
		
		return t.getBz();
	}

	@Autowired
	public void setCzshDao(BaseDaoI<TCzsh> czshDao) {
		this.czshDao = czshDao;
	}

	@Autowired
	public void setPersonDao(BaseDaoI<TPerson> personDao) {
		this.personDao = personDao;
	}
	@Autowired
	public void setPersonSpDao(BaseDaoI<TPersonSp> personSpDao) {
		this.personSpDao = personSpDao;
	}
}
