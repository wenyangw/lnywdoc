package lnyswz.doc.action;

import com.opensymphony.xwork2.ModelDriven;
import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.doc.bean.Person;
import lnyswz.doc.bean.User;
import lnyswz.doc.service.PersonServiceI;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 人员Action
 * @author 王文阳
 *
 */
@Namespace("/doc")
@Action("personAction")
public class PersonAction extends BaseAction implements ModelDriven<Person> {
	private Person person = new Person();
	private PersonServiceI personService;
	
	/**
	 * 增加人员
	 */
	public void add(){
		User user = (User)session.get("user");
		person.setCreateId(user.getId());
		person.setCreateName(user.getRealName());
		Json j = new Json();
		try{
			j.setObj(personService.add(person));
			j.setSuccess(true);
			j.setMsg("增加人员成功");
		}catch(Exception e){
			j.setMsg("增加人员失败");
			e.printStackTrace();
		}
		writeJson(j);
	}
	/**
	 * 修改人员
	 */
	public void edit() {
		User user = (User)session.get("user");
		person.setCreateId(user.getId());
		person.setCreateName(user.getRealName());
		Json j = new Json();
		try {
			//将获得的前台内容传入Service
			personService.edit(person);
			j.setSuccess(true);
			j.setMsg("修改人员成功！");
		} catch (Exception e) {
			j.setMsg("修改人员失败！");
			e.printStackTrace();
		}
		super.writeJson(j);
	}
	
	/**
	 * 删除人员
	 */
	public void delete(){
		User user = (User)session.get("user");
		person.setCreateId(user.getId());
		person.setCreateName(user.getRealName());
		Json j = new Json();
		try{
			personService.delete(person);
			j.setSuccess(true);
			j.setMsg("删除人员成功!");
		}catch (Exception e){
			j.setMsg("删除人员失败!");
			e.printStackTrace();
		}
		writeJson(j);
	}

	
	/**
	 * 删除人员
	 */
	public void cancelSp(){
		User user = (User)session.get("user");
		person.setCreateId(user.getId());
		person.setCreateName(user.getRealName());
		Json j = new Json();
		try{
			personService.cancelSp(person);
			j.setSuccess(true);
			j.setMsg("撤销成功!");
		}catch (Exception e){
			j.setMsg("撤销失败!");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	
	
	
	/**
	 * 获得人员
	 */
	public void listByDep(){
		writeJson(personService.listByDep(person));
	}
	/**
	 * sungj
	 * 获取指定人员详细信息
	 */
	public void getPerson(){
		writeJson(personService.getPerson(person));
	}
	
	/*****/
	public void getSelect(){

		writeJson(personService.getSelect(person));
	}
	
	/*****/
	public void personSearchDatagrid(){

		writeJson(personService.personSearchDatagrid(person));
	}
	
	/*****/
	public void checkEname(){	
		writeJson(personService.checkEname(person));
	}
	/*****/
	public void checkEntry(){	
		writeJson(personService.checkEntry(person));
	}

	@Override
	public Person getModel() {
		return person;
	}
	
	@Autowired
	public void setPersonService(PersonServiceI personService) {
		this.personService = personService;
	}
}
