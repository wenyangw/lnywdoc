package lnyswz.doc.service.impl;

import lnyswz.common.bean.DataGrid;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.common.util.DateUtil;
import lnyswz.doc.bean.PersonSp;
import lnyswz.doc.model.*;
import lnyswz.doc.service.PersonSpServiceI;
import lnyswz.doc.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.lang.reflect.Field;
import java.util.*;


/**
 * 人员实现表
 * @author 王文阳
 *
 */
@Service("personSpService")
public class PersonSpServiceImpl implements PersonSpServiceI {

	private BaseDaoI<TPersonSp> personSpDao;
	private BaseDaoI<TPerson> personDao;
	private BaseDaoI<TCzsh> czshDao;
	private BaseDaoI<TOperalog> operalogDao;


	/**
	 * 增加人员项
	 */
	@Override
	public  PersonSp add(PersonSp personSp) {
		ArrayList<PersonSp> objs = JSON.parseObject(personSp.getCond(), new TypeReference<ArrayList<PersonSp>>(){});
	    String timeStamp = DateUtil.dateToString(new Date(),"yyyyMMddHHmmss");
	    for (PersonSp obj : objs) {
	    	TPersonSp t = new TPersonSp();
			t.setCreateId(personSp.getCreateId());
			t.setCreateName(personSp.getCreateName());
			t.setCreateTime(new Date());
			t.setPersonId(personSp.getPersonId());
			t.setPersonName(personSp.getPersonName());
			t.setNeedAudit(personSp.getNeedAudit());
			t.setIsAudit("0");
			t.setStatus(personSp.getStatus());
			t.setTimeStamp(timeStamp);
	    	t.setField(obj.getField());
	    	t.setOldValue(obj.getOldValue());
	    	t.setNewValue(obj.getNewValue());
	    	personSpDao.save(t);
	    	
			OperalogServiceImpl.addOperalog(personSp.getCreateId(), "", "", "" + t.getId(),
			"生成人员", operalogDao);
	    }
		
	
	    TPerson tp = personDao.load(TPerson.class, personSp.getPersonId());
	    if(objs.size() > 0){
	    	tp.setTimeStamp(timeStamp);
	 	    tp.setStatus(personSp.getStatus());	
	    }
	    personDao.update(tp);

		return personSp;
	}

	@Override
	public String getAuditLevel(PersonSp personSp) {
		String sql = "select auditLevel from t_audit where bmbh = ? and ywlxId = ?";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("0", personSp.getBmbh());
		params.put("1", personSp.getYwlxId());

		return personSpDao.getBySQL(sql, params).toString();
	}

	@Override
	public DataGrid listAudits(PersonSp personSp) {
		String sql = "select distinct personId, personName, status, timeStamp, createName, createTime, au.auditLevel needAudit, isAudit from t_person_sp ps\n" +
				",\n" +
				"(select tas.auditLevel from t_audit_set tas \n" +
				"left join t_audit a on tas.auditId = a.id and a.ywlxId = '01'\n" +
				"where tas.bmbh = ? and tas.userId = ?) au\n" +
				"where ps.status <> '0' and ps.needAudit <> ps.isAudit and ps.isAudit <> '9' and au.auditLevel = ps.isAudit + 1";

		Map<String, Object> sqlParams = new HashMap<String, Object>();
		sqlParams.put("0", personSp.getBmbh());
		sqlParams.put("1", personSp.getCreateId());

        long total = personDao.countSQL("select count(*) from (" + sql + ") co", sqlParams);
        long pages = total / personSp.getRows();
        if(total % personSp.getRows() != 0){
            pages++;
        }
        if(pages == 0){
            personSp.setPage(1);
        }else if(personSp.getPage() > pages){
            personSp.setPage((int)pages);
        }

		List<Object[]> personIds = personSpDao.findBySQL(sql, sqlParams, personSp.getPage(), personSp.getRows());

		DataGrid dg = null;
		if (personIds.size() > 0) {
			dg = new DataGrid();
			List<PersonSp> personSps = new ArrayList<PersonSp>();

			PersonSp p = null;
			for (Object[] o : personIds) {
				p = new PersonSp();
				p.setPersonId(Integer.parseInt(o[0].toString()));
				p.setPersonName(o[1].toString());
				p.setStatus(o[2].toString());
				p.setTimeStamp(o[3].toString());
				p.setCreateName(o[4].toString());
				p.setCreateTime(DateUtil.stringToDate(o[5].toString(), DateUtil.DATETIME_PATTERN));
				p.setNeedAudit(o[6].toString());
				p.setIsAudit(o[7].toString());

				personSps.add(p);
			}

			dg.setRows(personSps);

			dg.setTotal(personDao.countSQL("select count(*) from (" + sql + ") co", sqlParams));
		}

		return dg;
	}
	
	@Override
	public DataGrid getPersonSps(PersonSp personSp) {
		String hql = "from TPersonSp t where t.personId = :personId and t.timeStamp = :timeStamp";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("personId", personSp.getPersonId());
		params.put("timeStamp", personSp.getTimeStamp());
		List<TPersonSp> tPersonSp = personSpDao.find(hql,params);
		
		DataGrid dg = new DataGrid();		
		dg.setRows(tPersonSp);
		return dg;
	}

	@Override
	public DataGrid listFields(PersonSp personSp) {
		DataGrid dg = null;
		List<PersonSp> personSps = null;
		if ("2".equals(personSp.getStatus())) {
			String hql = "from TPersonSp t where t.personId = :personId and t.timeStamp = :timeStamp";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("personId", personSp.getPersonId());
			params.put("timeStamp", personSp.getTimeStamp());

			List<TPersonSp> tPersonSps = personSpDao.find(hql, params);

			if (tPersonSps.size() > 0) {
				dg = new DataGrid();
				personSps = new ArrayList<PersonSp>();

				PersonSp p = null;
				for (TPersonSp tPersonSp : tPersonSps) {
					p = new PersonSp();
					p.setField(tPersonSp.getField());
					p.setOldValue(tPersonSp.getOldValue());
					p.setNewValue(tPersonSp.getNewValue());

					personSps.add(p);
				}

				dg.setRows(personSps);
			}
		} else {
			Field[] fields = getFields();

			TPerson tp = personDao.get(TPerson.class, personSp.getPersonId());
			dg = new DataGrid();
			personSps = new ArrayList<PersonSp>();
			PersonSp p = null;
			for (Field field : fields) {
				if (Arrays.asList(Constant.PERSON_SP_EXCLUDE_FIELDS).contains(field.getName())) {
					continue;
				}
				field.setAccessible(true);

				Object o = null;
				try {
					o = field.get(tp);
				}catch (IllegalAccessException e){
					e.printStackTrace();
				}
				if(o == null || o.toString().trim().length() == 0 || "0".equals(o.toString().trim())) {
					continue;
				}else{
					p = new PersonSp();
					p.setField(field.getName());
					if ("1".equals(personSp.getStatus())) {
						p.setNewValue(o.toString());
					}else{
						p.setOldValue(o.toString());
					}
					personSps.add(p);
				}
			}
			dg.setRows(personSps);
		}
		return dg;
	}

	@Override
	public void updateAudit(PersonSp personSp) {
		String hql = "from TPersonSp t where t.personId = :personId and t.timeStamp = :timeStamp";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("personId", personSp.getPersonId());
		params.put("timeStamp", personSp.getTimeStamp());

		List<TPersonSp> tps = personSpDao.find(hql, params);

		if(tps.size() > 0){
			if(Integer.parseInt(tps.get(0).getNeedAudit()) == Integer.parseInt(tps.get(0).getIsAudit()) + 1){
				//最后一级审批
				TPerson tPerson = personDao.get(TPerson.class, personSp.getPersonId());
				if("1".equals(personSp.getStatus())) {
					//新增
					tPerson.setStatus("0");
					tPerson.setTimeStamp("");
					personDao.save(tPerson);
				}else if("2".equals(personSp.getStatus())){
					//修改
					tPerson.setStatus("0");
					tPerson.setTimeStamp("");
					updatePerson(tPerson, tps);
                    personDao.save(tPerson);
				}else if("3".equals(personSp.getStatus())){
					//删除
					personDao.delete(tPerson);
				}
			}
			for (TPersonSp tp : tps) {
				tp.setIsAudit(String.valueOf(Integer.parseInt(tp.getIsAudit()) + 1));
				personSpDao.save(tp);
			}

			updateCzsh("1", personSp);
		}

		OperalogServiceImpl.addOperalog(personSp.getCreateId(), personSp.getBmbh(), personSp.getMenuId(),
				String.valueOf(personSp.getPersonId()) + ":" + personSp.getTimeStamp(),"人事信息审批通过", operalogDao);
	}

	@Override
	public void updateRefuse(PersonSp personSp) {
		String hql = "from TPersonSp t where t.personId = :personId and t.timeStamp = :timeStamp";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("personId", personSp.getPersonId());
		params.put("timeStamp", personSp.getTimeStamp());

		List<TPersonSp> tps = personSpDao.find(hql, params);

		if(tps.size() > 0){

			TPerson tPerson = personDao.get(TPerson.class, personSp.getPersonId());
			tPerson.setIsAudit("9");
            personDao.save(tPerson);

			for (TPersonSp tp : tps) {
				tp.setIsAudit("9");
				personSpDao.save(tp);
			}

			updateCzsh("9", personSp);

		}

		OperalogServiceImpl.addOperalog(personSp.getCreateId(), personSp.getBmbh(), personSp.getMenuId(),
				String.valueOf(personSp.getPersonId()) + ":" + personSp.getTimeStamp(),"人事信息审批通过", operalogDao);
	}

    /**
     * 更新审批操作记录
     * @param result
     * @param personSp
     */
	private void updateCzsh(String result, PersonSp personSp){
        TCzsh tCzsh = new TCzsh();
        tCzsh.setPersonId(personSp.getPersonId());
        tCzsh.setTimeStamp(personSp.getTimeStamp());
        tCzsh.setCreateId(personSp.getCreateId());
        tCzsh.setCreateName(personSp.getCreateName());
        tCzsh.setCreateTime(new Date());
        tCzsh.setBmbh(personSp.getBmbh());
        tCzsh.setNeedAudit(personSp.getNeedAudit());
        tCzsh.setIsAudit(result);
        tCzsh.setBz(personSp.getBz());
        czshDao.save(tCzsh);
    }

	@Override
	public PersonSp getPersonSp(PersonSp personSp){
		String hql = "from TPersonSp t where t.personId = :personId and t.timeStamp = :timeStamp";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("personId", personSp.getPersonId());
		params.put("timeStamp", personSp.getTimeStamp());

		TPersonSp tsp = personSpDao.get(hql, params);

		personSp.setIsAudit(tsp.getIsAudit());
		return personSp;
	}

    /**
     * 获取TPerson类的fields
     */
	public static Field[] getFields(){
		Class oClass = null;
		try {
			oClass = Class.forName(TPerson.class.getName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return oClass.getDeclaredFields();
	}

    /**
     * 修改审批通过后更新对应的TPerson内容
     * @param tPerson
     * @param tps
     */
	public static void updatePerson(TPerson tPerson, List<TPersonSp> tps){
		Field[] fields = getFields();
		for(TPersonSp tp : tps) {
			for (Field field : fields) {
				if(field.getName().equals(tp.getField())){
                    field.setAccessible(true);
					try{
                        if(field.getType().getName().equals(java.lang.String.class.getName())){
							field.set(tPerson, tp.getNewValue());
						}
						if(field.getType().getName().equals(java.lang.Integer.class.getName()) || field.getType().getName().equals("int")){
							field.set(tPerson, Integer.parseInt(tp.getNewValue()));
						}
						if(field.getType().getName().equals(java.util.Date.class.getName())){
							field.set(tPerson, DateUtil.stringToDate(tp.getNewValue()));
						}
					}catch (IllegalAccessException e){
						e.printStackTrace();
					}
					break;
				}
			}
		}
	}

	/**
	 * 获取字段内容，当用于判断不同类型时需要处理，
	 * 暂时用不上
	 * @param field
	 * @param obj
	 * @return
	 */
	private String getFieldValue(Field field, Object obj) {
		if (field.getType().getName().equals(
				java.lang.String.class.getName())) {
			// String type
			try {
				return field.get(obj).toString();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (field.getType().getName().equals(
				java.lang.Integer.class.getName())
				|| field.getType().getName().equals("int")) {
			// Integer type
			try {
				return String.valueOf(field.getInt(obj));
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public static TPersonSp getTPersonSp(int personId, String timeStamp, BaseDaoI<TPersonSp> baseDao){
        String hql = "from TPersonSp t where t.personId = :personId and t.timeStamp = :timeStamp";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("personId", personId);
        params.put("timeStamp", timeStamp);

        return baseDao.get(hql, params);
    }

    public static List<TPersonSp> getTPersonSps(int personId, String timeStamp, BaseDaoI<TPersonSp> baseDao){
        String hql = "from TPersonSp t where t.personId = :personId and t.timeStamp = :timeStamp";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("personId", personId);
        params.put("timeStamp", timeStamp);

        return baseDao.find(hql, params);
    }

	@Autowired
	public void setPersonSpDao(BaseDaoI<TPersonSp> personSpDao) {
		this.personSpDao = personSpDao;
	}

	@Autowired
	public void setPersonDao(BaseDaoI<TPerson> personDao) {
		this.personDao = personDao;
	}

	@Autowired
	public void setCzshDao(BaseDaoI<TCzsh> czshDao) {
		this.czshDao = czshDao;
	}

	@Autowired
	public void setOperalogDao(BaseDaoI<TOperalog> operalogDao) {
		this.operalogDao = operalogDao;
	}
	


	

	
	
	
}
