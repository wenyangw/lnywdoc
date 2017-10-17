package lnyswz.doc.action;

import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.doc.bean.UpdateLog;
import lnyswz.doc.bean.User;
import lnyswz.doc.service.UpdateLogServiceI;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import com.opensymphony.xwork2.ModelDriven;

@Namespace("/admin")
@Action("updateLogAction")
public class UpdateLogAction extends BaseAction implements
		ModelDriven<UpdateLog> {
	private static final long serialVersionUID = 1L;
	private UpdateLogServiceI updateService;
	public UpdateLog update = new UpdateLog();

	/**
	 * 增加更新数据
	 */
	public void add() {
		Json j = new Json();
		User u = (User) session.get("user");
		update.setUpdateUser(u.getRealName());
		try {
			UpdateLog r = updateService.add(update);
			j.setSuccess(true);
			j.setMsg("增加更新成功");
			j.setObj(r);
		} catch (Exception e) {
			j.setMsg("增加更新数据失败");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 修改更新数据
	 */
	public void edit() {
		Json j = new Json();
		User u = (User) session.get("user");
		update.setUpdateUser(u.getRealName());
		try {
			updateService.edit(update);
			j.setMsg("修改成功");
			j.setSuccess(true);
		} catch (Exception e) {
			j.setMsg("修改失败");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 删除更新数据
	 */
	public void delete() {
		Json j = new Json();
		try {
			updateService.delete(update.getIds());
			j.setSuccess(true);
			j.setMsg("删除成功");
		} catch (Exception e) {
			j.setMsg("删除失败");
		}
		writeJson(j);
	}

	public void datagrid() {
		super.writeJson(updateService.datagrid(update));
	}

	/**
	 * 首页显示更新数据
	 */
	public void listUpdateLog() {
		writeJson(updateService.listUpdateLog(update));
	}

	@Autowired
	public void setUpdateService(UpdateLogServiceI updateService) {
		this.updateService = updateService;
	}

	@Override
	public UpdateLog getModel() {
		return update;
	}
}
