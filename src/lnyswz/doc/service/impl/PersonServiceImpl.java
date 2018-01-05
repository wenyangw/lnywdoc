package lnyswz.doc.service.impl;

import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.Obj;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.common.util.DateUtil;
import lnyswz.doc.action.UploadAction;
import lnyswz.doc.bean.Person;
import lnyswz.doc.bean.PersonSp;
import lnyswz.doc.model.*;
import lnyswz.doc.service.PersonServiceI;
import lnyswz.doc.util.Constant;
import lnyswz.doc.util.Util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 人员实现表
 * @author 王文阳
 *
 */
@Service("personService")
public class PersonServiceImpl implements PersonServiceI {
	
	private BaseDaoI<TPerson> personDao;
	private BaseDaoI<TOperalog> operalogDao;
	private BaseDaoI<TDepartment> depDao;
	private BaseDaoI<TEntry> entryDao;
	private BaseDaoI<TImg> imgDao;
	private BaseDaoI<TPersonSp> personSpDao;

	/**
	 * 增加人员项
	 */
	@Override
	public Person add(Person person) {
		
		String timeStamp = DateUtil.dateToString(new Date(),"yyyyMMddHHmmss");
		person.setTimeStamp(timeStamp);
		person.setIsAudit("0");
		person.setStatus("1");	
		TPerson t = new TPerson();
		BeanUtils.copyProperties(person, t);

		TDepartment dep=depDao.load(TDepartment.class, person.getBmbh());
		t.setTDepartment(dep);
		personDao.save(t);

		person.setId(t.getId());

		TPersonSp tsp = assignmentPersonSp(person);		
    	personSpDao.save(tsp);
    	
		OperalogServiceImpl.addOperalog(person.getCreateId(), "", "", "" + t.getId(),
				"生成人员", operalogDao);
		return person;
	}
	
	public TPersonSp assignmentPersonSp(Person p){
		TPersonSp tsp = new TPersonSp(); 
		BeanUtils.copyProperties(p,tsp);
		tsp.setPersonId(p.getId());
		tsp.setCreateTime(new Date());
		tsp.setPersonName(p.getName());
		 tsp.setIsAudit("0");
		return tsp;
	}
	
	/**
	 * 修改人员
	 */
	@Override
	public void edit(Person person) {		
		
		ArrayList<PersonSp> personSpObjs = JSON.parseObject(person.getPersonSpCond(), new TypeReference<ArrayList<PersonSp>>(){});
		Person p= JSON.parseObject(person.getPersonCond(), new TypeReference<Person>(){});
		String timeStamp = DateUtil.dateToString(new Date(),"yyyyMMddHHmmss");
		
		TPerson t=personDao.load(TPerson.class, person.getId());
		Util.copyPropertiesIgnoreNull(p,t,person.getNoAuditField());
		
		
		
		//修改ename值时，改变对应img的路径及实际位置
		if(person.getEname() != null && !person.getEname().equals(t.getEname())){
			String oldDir = Constant.UPLOADFILE_PATH + "/" + t.getEname() + "/";

			String hql = "from TImg t where t.filePath like :dir";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("dir", "%" + oldDir + "%");

			List<TImg> imgLists = imgDao.find(hql, params);
			if (imgLists != null && imgLists.size() > 0){
				for (TImg tImg : imgLists) {
					String oldFilePath = tImg.getFilePath();
					String newFilePath =  oldFilePath.replace(oldDir, Constant.UPLOADFILE_PATH + "/" + person.getEname() + "/");

					if(UploadAction.moveFile(oldFilePath, newFilePath)){
						tImg.setFilePath(newFilePath);
						imgDao.save(tImg);
						UploadAction.deleteDir(oldDir);
					}
				}
			}

		}
		if(!(person.getBmbh().equals(t.getTDepartment().getId()))){
			TDepartment dep=depDao.load(TDepartment.class, person.getBmbh());
			t.setTDepartment(dep);
		}
		if(personSpObjs.size() > 0){
			
			t.setTimeStamp(timeStamp);
			person.setName(t.getName());
			person.setTimeStamp(timeStamp);
			 for (PersonSp obj : personSpObjs) {
				 
				 TPersonSp tsp = assignmentPersonSp(person);				
				 tsp.setOldValue(obj.getOldValue());
				 tsp.setNewValue(obj.getNewValue());
				 tsp.setField(obj.getField());
			     personSpDao.save(tsp);
			    	
			     
				OperalogServiceImpl.addOperalog(person.getCreateId(), "", "", "" + t.getId(),
				"生成人员", operalogDao);
			 }
			 t.setStatus(person.getStatus());	
		}
		personDao.update(t);
		
		
		
		

		
		
//		OperalogServiceImpl.addOperalog(person.getCreateId(), "", "", "" + t.getId(),
//				"修改人员", operalogDao);
	}

	/**
	 * 删除人员项
	 */
	public void delete(Person person) {
		String timeStamp = DateUtil.dateToString(new Date(),"yyyyMMddHHmmss");
		TPerson t = personDao.get(TPerson.class, person.getId());
		t.setStatus(person.getStatus());
		t.setTimeStamp(timeStamp);
		personDao.save(t);
		
		BeanUtils.copyProperties(t,person);
		TPersonSp tsp = assignmentPersonSp(person);
    	personSpDao.save(tsp);
		
		OperalogServiceImpl.addOperalog(person.getCreateId(), "", "", "" + person.getId(),
			"删除人员", operalogDao);
	}
	/**
	 * 撤销审批
	 */
	public void cancelSp(Person person) {
		
		TPerson t = personDao.get(TPerson.class, person.getId());
		if(t.getStatus().equals("1")){
			personDao.delete(t);
		}else{
			t.setIsAudit("0");
			t.setStatus("0");
			personDao.update(t);
		}
		OperalogServiceImpl.addOperalog(person.getCreateId(), "", "", "" + person.getId(),
				"删除人员", operalogDao);
	}

	

	/**
	 * 获取人员
	 */
	@Override
	public List<Person> listByDep(Person person) {
		String hql = "from TPerson t where t.TDepartment.id = :bmbh order by orderNum";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bmbh", person.getBmbh());
		List<TPerson> l = personDao.find(hql, params);
		List<Person> nl = null;
		Person p = null;
		if (l != null && l.size() > 0) {
			nl = new ArrayList<Person>();
			for (TPerson tPerson : l) {
				p = new Person();
				p.setId(tPerson.getId());
				p.setName(tPerson.getName());
				p.setEname(tPerson.getEname());
				p.setOrderNum(tPerson.getOrderNum());
				nl.add(p);
			}
		}
		return nl;
	}

	@Override
	public Person getPerson(Person person) {
		
		TPerson t = personDao.load(TPerson.class, person.getId());
		Person p = new Person();

		BeanUtils.copyProperties(t, p);

		p.setBmbh(t.getTDepartment().getId());
		return p;
	}

	@Override
	public DataGrid personSearchDatagrid(Person person) {
		 DataGrid dg = new DataGrid();
		 String hql = "from TPerson t";
		 String formalTime = null ;
		 String jobTime = null ;
		 String jtJobTime = null;
		 String companyTime = null;
		 Map<String, Object> params = new HashMap<String, Object>();
	     ArrayList<Obj> objs = JSON.parseObject(person.getSearch(), new TypeReference<ArrayList<Obj>>(){});
	     String where = "";
	     for (Obj obj : objs) {
	            if(!where.equals("")){
	                where += " AND";
	            }
	            if(obj.getType().equals(Constant.SEARCH_VALUE_TYPE_COUNT)){
	            	 if(obj.getField().equals("formalTime")){
	            		 formalTime = obj.getValue().toString();
	            	 }else if(obj.getField().equals("jobTime")){
	            		 jobTime = obj.getValue().toString();
	            	 }else if(obj.getField().equals("jtJobTime")){
	            		 jtJobTime = obj.getValue().toString();
	            	 }else if(obj.getField().equals("companyTime")){
	            		 companyTime=obj.getValue().toString();
	            	 }
	            	 
	            	 where += " (dbo.countDay(" + obj.getField() + ",'" + obj.getValue() + "'," + obj.getRound() + ")) " + obj.getOperator() + " :" + obj.getField() + obj.getCount();
	            	 params.put(obj.getField() + obj.getCount(), Integer.parseInt(obj.getCount()));
	            	
	            }else{
		            where += " "+obj.getField() + " " + obj.getOperator() + "  :" + obj.getField() + obj.getCount();
		            if(obj.getType().equals(Constant.SEARCH_VALUE_TYPE_DATE)){
		            	 params.put(obj.getField() + obj.getCount(), (DateUtil.stringToDate(obj.getValue().toString())));
		            }else {
		            	params.put(obj.getField() + obj.getCount(), "%" + obj.getValue() + "%");
		            }
		         }
	            	
	       }
	
		 if(!where.equals("")){
			 hql += " where" + where;
		 }
		 String totalHql = "select count(*) " + hql;
		 hql += " order by t.TDepartment.orderNum,orderNum";
	     List<TPerson> li = personDao.find(hql,params);
	     List<Person> nl = new ArrayList<Person>();
		 for(TPerson t : li){
			 	Person d = new Person();
				BeanUtils.copyProperties(t, d);
				d.setBmbh(t.getTDepartment().getDepName());
			 	d.setWorkingYears(getCurrentJobTime(d.getJobTime(),jobTime,""));
				d.setFormalPartyCount(getCurrentJobTime(d.getFormalTime(), formalTime,""));
				d.setJtWorkingYears(getCurrentJobTime(d.getJtJobTime(), jtJobTime, ""));
				d.setCompanyYears(getCurrentJobTime(d.getCompanyTime(), companyTime, ""));
				nl.add(d);
		 }
		 dg.setTotal(personDao.count(totalHql, params));
		 dg.setRows(nl);
		 return dg;
	}
	
	
	@Override
	public List<Person> getSelect(Person person) {
		String sql="select distinct " + person.getParam() + " from t_person where " + person.getParam() + " is not null ";
		List li = personDao.findBySQL(sql);
		List<Person> nl = new ArrayList<Person>();
		for(int i = 0 ;i < li.size();i++){
			Person d = new Person();	
			d.setParam(li.get(i).toString());
			nl.add(d);
		}

		return nl;
	}
	
	@Override
	public boolean checkEname(Person person) {
		String hql = " from TPerson where ename = :ename";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ename", person.getEname());

		List<TPerson> li = personDao.find(hql, params);
		if(li.size() > 0){
			return true;
		}
		return false;
	}
	
	@Override
	public boolean checkEntry(Person person) {
		String hql = "from TEntry t where t.TPerson.id = :personId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("personId", person.getId());

		List<TEntry> li = entryDao.find(hql, params);

		if(li.size() > 0){
			return true;
		}else {
			return false;	
		}
		
	}

	/**
	 * 计算工龄
	 * @param
	 * @return
	 */
	public  int getCurrentJobTime(Date fieldTime,String varTime,String round ){
		int timeCount = 0;

		Calendar field = Calendar.getInstance();
		Calendar var = Calendar.getInstance();
		if (fieldTime != null) {
			if(varTime != null){
				var.setTime(DateUtil.stringToDate(varTime));
			}else{
				var.setTime(new Date());
			}
			field.setTime(fieldTime);
			timeCount = var.get(Calendar.YEAR) - field.get(Calendar.YEAR);
			int varMonth=var.get(Calendar.MONTH);
			int fieldMonth=field.get(Calendar.MONTH);
			int varMonthDay=var.get(Calendar.DAY_OF_MONTH);
			int fieldMonthDay=field.get(Calendar.DAY_OF_MONTH);
			if(!round.equals("")){
				if(timeCount == 0){
					if((varMonth-fieldMonth) >= 6){
						timeCount += 1;
					}
				}else{
					if((varMonth + 12 - fieldMonth) < 6){
						timeCount -= 1;
					}
				}
			}else{
				if(varMonth < fieldMonth){
					timeCount -= 1;
				}else if(varMonth == fieldMonth){
					if( varMonthDay < fieldMonthDay){
						timeCount -= 1;
					}
				}
			}
			

			
		}
		return timeCount;
	}

	@Autowired
	public void setPersonDao(BaseDaoI<TPerson> personDao) {
		this.personDao = personDao;
	}

	@Autowired
	public void setOperalogDao(BaseDaoI<TOperalog> operalogDao) {
		this.operalogDao = operalogDao;
	}
	
	@Autowired
	public void setDepDao(BaseDaoI<TDepartment> depDao) {
		this.depDao = depDao;
	}

	@Autowired
	public void setEntryDao(BaseDaoI<TEntry> entryDao) {
		this.entryDao = entryDao;
	}

	@Autowired
	public void setImgDao(BaseDaoI<TImg> imgDao) {
		this.imgDao = imgDao;
	}

	@Autowired
	public void setPersonSpDao(BaseDaoI<TPersonSp> personSpDao) {
		this.personSpDao = personSpDao;
	}
	
	
}
