package lnyswz.doc.action;

import com.opensymphony.xwork2.ModelDriven;
import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.doc.bean.Img;
import lnyswz.doc.bean.User;
import lnyswz.doc.service.ImgServiceI;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 图片Action
 * @author 王文阳
 *
 */
@Namespace("/doc")
@Action("imgAction")
public class ImgAction extends BaseAction implements ModelDriven<Img> {
	private Img img = new Img();
	private ImgServiceI imgService;
	
	/**
	 * 增加图片
	 */
	public void add(){
		User user = (User)session.get("user");
		img.setCreateId(user.getId());
		img.setCreateName(user.getRealName());
		Json j = new Json();
		try{
			j.setObj(imgService.add(img));
			j.setSuccess(true);
			j.setMsg("增加图片成功");
		}catch(Exception e){
			j.setMsg("增加图片失败");
			e.printStackTrace();
		}
		writeJson(j);
	}
	/**
	 * 修改图片
	 */
	public void edit() {
		User user = (User)session.get("user");
		img.setCreateId(user.getId());
		img.setCreateName(user.getRealName());
		Json j = new Json();
		try {
			imgService.edit(img);
			j.setSuccess(true);
			j.setMsg("修改图片成功！");
		} catch (Exception e) {
			j.setMsg("修改图片失败！");
			e.printStackTrace();
		}
		super.writeJson(j);
	}
	
	/**
	 * 删除图片
	 */
	public void delete(){
		User user = (User)session.get("user");
		img.setCreateId(user.getId());
		img.setCreateName(user.getRealName());
		Json j = new Json();
		try{
			imgService.delete(img);
			j.setSuccess(true);
			j.setMsg("删除图片成功！");
		}catch(Exception e){
			j.setMsg("删除图片失败！");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 根据Img的Id获取图片
	 */
	public void getImg(){
		Json j = new Json();
		j.setObj(imgService.getImg(img));
		writeJson(j);
	}


	/**
	 * 根据Entry的Id获取图片
	 */
	public void getImgsByEntry(){
		writeJson(imgService.getImgsByEntry(img));
	}

	@Override
	public Img getModel() {
		return img;
	}
	
	@Autowired
	public void setImgService(ImgServiceI imgService) {
		this.imgService = imgService;
	}
}
