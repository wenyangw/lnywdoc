package lnyswz.doc.action;

import java.util.List;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import com.opensymphony.xwork2.ModelDriven;
import lnyswz.common.action.BaseAction;
import lnyswz.doc.bean.Catalog;
import lnyswz.doc.bean.User;
import lnyswz.doc.service.CatalogServiceI;
import lnyswz.doc.service.UserServiceI;

/**
 * 登录操作Action
 * 
 * @author 王文阳
 * 
 */
public class LoginAction extends BaseAction implements ModelDriven<User> {

	private static final long serialVersionUID = 1L;

	private UserServiceI userService;
	private CatalogServiceI catalogService;
	// 模型驱动获得传入的对象
	private User user = new User();

	@Action(value = "login", results = {
			@Result(name = SUCCESS, location = "/layout/index.jsp"),
			@Result(name = "SUCCESS_M", location = "/m/index.jsp"),
			@Result(name = LOGIN, location = "/login.jsp"),
			@Result(name = "LOGIN_M", location = "/m/login.jsp") })
	
	public String login() {
		String source = "";
		if(user.getSource() != null){
			source = user.getSource();
		}
		// 获得登录用户信息
		if (!user.getPassword().equals("")) {
			
			User u = userService.login(user);
			// 获得模块信息
			List<Catalog> top = catalogService.listCatas();
			// 登录成功
			if (u != null) {
				session.put("user", u);
				session.put("menutop", top);
				request.put("msg", "登录成功！");
				if(source.equals("mobile")){
					return "SUCCESS_M";
				}
				return SUCCESS;
			}
		}

		// 登录失败
		request.put("msg", "账号或密码错误，请重新输入！");
		if(source.equals("mobile")){
			return "LOGIN_M";
		}
		return LOGIN;
	}

	/**
	 * 用户退出
	 * 
	 * @return
	 */
	@Action(value = "logout", results = { @Result(name = SUCCESS, location = "/login.jsp") })
	public String logout() {
		// 清空session
		session.clear();
		// 返回登录页面
		return SUCCESS;
	}

	@Override
	public User getModel() {
		return user;
	}

	@Autowired
	public void setUserService(UserServiceI userService) {
		this.userService = userService;
	}

	@Autowired
	public void setCatalogService(CatalogServiceI catalogService) {
		this.catalogService = catalogService;
	}

}
