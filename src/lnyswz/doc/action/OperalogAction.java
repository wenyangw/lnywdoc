package lnyswz.doc.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import com.opensymphony.xwork2.ModelDriven;
import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.doc.bean.Operalog;
import lnyswz.doc.service.OperalogServiceI;

@Namespace("/admin")
@Action("operalogAction")
public class OperalogAction extends BaseAction implements ModelDriven<Operalog> {
	private static final long serialVersionUID = 1L;
	private Operalog ope = new Operalog();
	private OperalogServiceI opeService;

	/**
	 * 删除操作日志
	 */
	public void delete() {
		Json j = new Json();
		try {
			opeService.delete(ope.getIds());
			j.setSuccess(true);
			j.setMsg("删除操作日志成功！");
		} catch (Exception e) {
			j.setMsg("删除操作日志失败！");
		}
		writeJson(j);
	}

	public void datagrid() {
		super.writeJson(opeService.datagrid(ope));
	}

	@Override
	public Operalog getModel() {
		return ope;
	}

	@Autowired
	public void setOpeService(OperalogServiceI opeService) {
		this.opeService = opeService;
	}

}
