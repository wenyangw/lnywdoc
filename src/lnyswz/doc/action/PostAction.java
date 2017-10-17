package lnyswz.doc.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.doc.bean.Post;
import lnyswz.doc.service.PostServiceI;
import com.opensymphony.xwork2.ModelDriven;

@Namespace("/admin")
@Action("postAction")
public class PostAction extends BaseAction implements ModelDriven<Post> {
	private static final long serialVersionUID = 1L;
	private Post post = new Post();
	private PostServiceI postService;

	/**
	 * 增加计量单位
	 */
	public void add() {
		Json j = new Json();
		try {
			Post r = postService.add(post);
			j.setSuccess(true);
			j.setMsg("增加人员类型成功");
			j.setObj(r);
		} catch (Exception e) {
			j.setMsg("增加人员类型失败");
			e.printStackTrace();
		}
		writeJson(j);
	}

	/**
	 * 修改人员类型
	 */
	public void edit() {
		Json j = new Json();
		try {
			postService.edit(post);
			j.setSuccess(true);
			j.setMsg("编辑人员类型成功！");
		} catch (Exception e) {
			j.setMsg(e.getStackTrace().toString());
		}
		writeJson(j);
	}

	/**
	 * 删除人员类型信息
	 */
	public void delete() {
		Json j = new Json();
		try {
			postService.delete(post);
			j.setSuccess(true);
			j.setMsg("删除人员类型成功！");
		} catch (Exception e) {
			j.setMsg(e.getStackTrace().toString());
		}
		writeJson(j);
	}

	public void listPosts() {
		super.writeJson(postService.listPosts(post));
	}

	public void datagrid() {

		super.writeJson(postService.datagrid(post));
	}

	public Post getModel() {
		return post;
	}

	@Autowired
	public void setPostService(PostServiceI postService) {
		this.postService = postService;
	}
}