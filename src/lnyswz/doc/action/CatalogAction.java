package lnyswz.doc.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.doc.bean.Catalog;
import lnyswz.doc.service.CatalogServiceI;

import com.opensymphony.xwork2.ModelDriven;

/**
 * 模块Action
 * @author 王文阳
 *
 */
@Namespace("/admin")
@Action("catalogAction")
public class CatalogAction extends BaseAction implements ModelDriven<Catalog> {
	private static final long serialVersionUID = 1L;
	//模型驱动获得传入对象
	private Catalog catalog = new Catalog();
	private CatalogServiceI catalogService;
	
	
	
	/**
	 * 添加模块
	 */
	public void add(){
		Json j = new Json();
		try{
			//将获得的前台内容传入Service
			Catalog c = catalogService.add(catalog);
			//添加成功
			j.setSuccess(true);
			j.setMsg("增加模块成功");
			j.setObj(c);
		}catch(Exception e){
			j.setMsg("增加模块失败");
			e.printStackTrace();
		}
		writeJson(j);
	}
	
	/**
	 * 编辑模块
	 */
	public void edit(){
		Json j = new Json();
		try {
			//将获得的前台内容传入Service
			catalogService.edit(catalog);
			j.setSuccess(true);
			j.setMsg("修改模块成功！");
		} catch (Exception e) {
			j.setMsg("修改模块失败！");
			e.printStackTrace();
		}
		super.writeJson(j);
	}
	
	/**
	 * 删除模块
	 */
	public void delete(){
		Json j = new Json();
		try{
			catalogService.delete(catalog.getIds());
			j.setSuccess(true);
			j.setMsg("删除模块成功!");
		}catch(Exception e){
			j.setMsg("删除模块失败!");
			e.printStackTrace();
		}
		super.writeJson(j);
	}
	
	/**
	 * 返回所有模块，供导航使用，无分页
	 */
	public void listCatas(){
		writeJson(catalogService.listCatas());
	}
	
	/**
	 * 返回所有模块，供管理使用，有分页
	 */
	public void datagrid(){
		writeJson(catalogService.datagrid(catalog));
	}

	@Override
	public Catalog getModel() {
		return catalog;
	}
	
	@Autowired
	public void setCatalogService(CatalogServiceI catalogService) {
		this.catalogService = catalogService;
	}
}
