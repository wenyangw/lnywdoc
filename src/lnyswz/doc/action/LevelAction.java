package lnyswz.doc.action;

import com.opensymphony.xwork2.ModelDriven;
import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.Json;
import lnyswz.doc.bean.Level;
import lnyswz.doc.bean.User;
import lnyswz.doc.service.LevelServiceI;
import lnyswz.doc.util.Export;
import lnyswz.doc.util.Util;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 类别Action
 * @author 王文阳
 *
 */
@Namespace("/doc")
@Action("levelAction")
public class LevelAction extends BaseAction implements ModelDriven<Level> {
	private Level level = new Level();
	private LevelServiceI levelService;
	
	/**
	 * 增加类别
	 */
	public void add(){
		User user = (User)session.get("user");
		level.setCreateId(user.getId());
		level.setCreateName(user.getRealName());
		Json j = new Json();
		try{
			levelService.add(level);
			j.setSuccess(true);
			j.setMsg("增加类别成功");
		}catch(Exception e){
			j.setMsg("增加类别失败");
			e.printStackTrace();
		}
		writeJson(j);
	}
	/**
	 * 修改类别
	 */
	public void edit() {
		User user = (User)session.get("user");
		level.setCreateId(user.getId());
		level.setCreateName(user.getRealName());
		Json j = new Json();
		try {
			//将获得的前台内容传入Service
			levelService.edit(level);
			j.setSuccess(true);
			j.setMsg("修改类别成功！");
		} catch (Exception e) {
			j.setMsg("修改类别失败！");
			e.printStackTrace();
		}
		super.writeJson(j);
	}
	
	/**
	 * 删除类别
	 */
	public void delete(){
		User user = (User)session.get("user");
		level.setCreateId(user.getId());
		level.setCreateName(user.getRealName());
		Json j = new Json();
		try{
			levelService.delete(level);
			j.setSuccess(true);
			j.setMsg("删除类别成功！");
		}catch(Exception e){
			j.setMsg("删除类别失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 获得类别，供管理使用，有分页
	 */
	public void treegrid(){
		writeJson(levelService.treegrid(level));
	}

	/**
	 * 类别树
	 */
	public void getLevelTree() {
		writeJson(levelService.getLevelTree(level, true));
	}


	public void checkDir(){
		Json j = new Json();
		j.setSuccess(levelService.checkDir(level));
		writeJson(j);
	}

	public void printLevel() {
		DataGrid dg = levelService.printLevel(level);

		Export.print(dg, Util.getReportName("00", "report_level.json"));
	}

	@Override
	public Level getModel() {
		return level;
	}
	
	@Autowired
	public void setLevelService(LevelServiceI levelService) {
		this.levelService = levelService;
	}
}
