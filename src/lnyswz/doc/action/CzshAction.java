package lnyswz.doc.action;

import com.opensymphony.xwork2.ModelDriven;
import lnyswz.common.action.BaseAction;
import lnyswz.doc.bean.Cat;
import lnyswz.doc.bean.Czsh;
import lnyswz.doc.service.CatServiceI;
import lnyswz.doc.service.CzshServiceI;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 操作审批Action
 * @author 王文阳
 *
 */
@Namespace("/doc")
@Action("czshAction")
public class CzshAction extends BaseAction implements ModelDriven<Czsh> {
	private Czsh czsh = new Czsh();
	private CzshServiceI czshService;

	public void datagrid(){
		writeJson(czshService.datagrid(czsh));
	}
	public void getAuditBz(){
		writeJson(czshService.getAuditBz(czsh));
	}

	@Override
	public Czsh getModel() {
		return czsh;
	}
	
	@Autowired
	public void setCzshService(CzshServiceI czshService) {
		this.czshService = czshService;
	}
}
