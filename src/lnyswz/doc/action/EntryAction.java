package lnyswz.doc.action;

import com.opensymphony.xwork2.ModelDriven;
import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.Json;
import lnyswz.doc.bean.Entry;
import lnyswz.doc.bean.User;
import lnyswz.doc.service.EntryServiceI;
import lnyswz.doc.util.Export;
import lnyswz.doc.util.Util;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 条目Action
 * @author 王文阳
 *
 */
@Namespace("/doc")
@Action("entryAction")
public class EntryAction extends BaseAction implements ModelDriven<Entry> {
	private Entry entry = new Entry();
	private EntryServiceI entryService;
	
	/**
	 * 增加条目
	 */
	public void add(){
		User user = (User)session.get("user");
		entry.setCreateId(user.getId());
		entry.setCreateName(user.getRealName());
		Json j = new Json();
		try{
			j.setObj(entryService.add(entry));
			j.setSuccess(true);
			j.setMsg("增加条目成功");
		}catch(Exception e){
			j.setMsg("增加条目失败");
			e.printStackTrace();
		}
		writeJson(j);
	}
	/**
	 * 修改条目
	 */
	public void edit() {
		User user = (User)session.get("user");
		entry.setCreateId(user.getId());
		entry.setCreateName(user.getRealName());
		Json j = new Json();
		try {
			//将获得的前台内容传入Service
			j.setObj(entryService.edit(entry));
			j.setSuccess(true);
			j.setMsg("修改条目成功！");
		} catch (Exception e) {
			j.setMsg("修改条目失败！");
			e.printStackTrace();
		}
		super.writeJson(j);
	}
	
	/**
	 * 删除条目
	 */
	public void delete(){
		User user = (User)session.get("user");
		entry.setCreateId(user.getId());
		entry.setCreateName(user.getRealName());
		Json j = new Json();
		try{
			entryService.delete(entry);
			j.setSuccess(true);
			j.setMsg("删除条目成功！");
		}catch(Exception e){
			j.setMsg("删除条目失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 获得条目
	 */
	public void getLevelEntries(){
		writeJson(entryService.getLevelEntries(entry));
	}

	public void checkEntryByLevel(){
		Json j = new Json();
		j.setSuccess(entryService.checkEntryByLevel(entry));
		writeJson(j);
	}

	public void searchLevel(){
		writeJson(entryService.searchLevel(entry));
	}

	public void searchEntry(){
		writeJson(entryService.searchEntry(entry));
	}

	public void checkDir(){
		Json j = new Json();
		j.setSuccess(entryService.checkDir(entry));
		writeJson(j);
	}

	public void docsDatagrid(){
		writeJson(entryService.docsDatagrid(entry));
	}

	public void printEntrys() {
		DataGrid dg = entryService.printEntrys(entry);

		Export.print(dg, Util.getReportName("00", "report_entrys.json"));
	}

	@Override
	public Entry getModel() {
		return entry;
	}
	
	@Autowired
	public void setEntryService(EntryServiceI entryService) {
		this.entryService = entryService;
	}
}
