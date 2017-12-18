package lnyswz.doc.action;

import com.opensymphony.xwork2.ModelDriven;
import lnyswz.common.action.BaseAction;
import lnyswz.doc.bean.Cat;
import lnyswz.doc.service.CatServiceI;
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
public class CzshAction extends BaseAction implements ModelDriven<Person> {
	private Cat cat = new Cat();
	private CatServiceI catService;

	/**
	 * 分类
	 */
	public void getCats() {
		writeJson(catService.getCats(cat));
	}

	@Override
	public Cat getModel() {
		return cat;
	}
	
	@Autowired
	public void setCatService(CatServiceI catService) {
		this.catService = catService;
	}
}
