package lnyswz.doc.action;

import com.opensymphony.xwork2.ModelDriven;
import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.doc.bean.PersonSp;
import lnyswz.doc.bean.User;
import lnyswz.doc.service.PersonSpServiceI;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 人员Action
 * @author 王文阳
 *
 */
@Namespace("/doc")
@Action("personSpAction")
public class PersonSpAction extends BaseAction implements ModelDriven<PersonSp> {
	private PersonSp personSp = new PersonSp();
	private PersonSpServiceI personSpService;
	
	/**
	 * 增加人员
	 */
	public void add(){
		User user = (User)session.get("user");
		personSp.setCreateId(user.getId());
		personSp.setCreateName(user.getRealName());
		Json j = new Json();
		try{
			j.setObj(personSpService.add(personSp));
			j.setSuccess(true);
			j.setMsg("增加人员审批成功");
		}catch(Exception e){
			j.setMsg("增加人员审批失败");
			e.printStackTrace();
		}
		writeJson(j);
	}

	public void getAuditLevel(){
		writeJson(personSpService.getAuditLevel(personSp));
	}

	/**
	 * 获取正在需要审批的操作列表
	 */
	public void listAudits() {
		User user = (User)session.get("user");
		personSp.setCreateId(user.getId());
		writeJson(personSpService.listAudits(personSp));
	}

	/**
	 * 根据 时间戳和Id 获取审批信息
	 */
	public void getPersonSps() {
		writeJson(personSpService.getPersonSps(personSp));
	}
	/**
	 * 获取每笔审批对应的修改列表
	 */
	public void listFields() {
		writeJson(personSpService.listFields(personSp));
	}

	public void audit(){
		User user = (User) session.get("user");
		personSp.setCreateId(user.getId());
		personSp.setCreateName(user.getRealName());
		Json j = new Json();
		try {
			personSpService.updateAudit(personSp);
			// 添加成功
			j.setSuccess(true);
			j.setMsg("操作审批通过成功！");
		} catch (Exception e) {
			j.setMsg("操作审批通过失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}

	public void refuse(){
		User user = (User) session.get("user");
		personSp.setCreateId(user.getId());
		personSp.setCreateName(user.getRealName());
		Json j = new Json();
		try {
			personSpService.updateRefuse(personSp);
			// 添加成功
			j.setSuccess(true);
			j.setMsg("拒绝审批通过成功！");
		} catch (Exception e) {
			j.setMsg("拒绝审批通过失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}

	public void getPersonSp(){
		writeJson(personSpService.getPersonSp(personSp));
	}

	@Override
	public PersonSp getModel() {
		return personSp;
	}
	
	@Autowired
	public void setPersonSpService(PersonSpServiceI personSpService) {
		this.personSpService = personSpService;
	}
	
}
