package lnyswz.doc.action;


import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ModelDriven;

import lnyswz.common.action.BaseAction;
import lnyswz.common.bean.Json;
import lnyswz.doc.bean.Button;
import lnyswz.doc.bean.User;
import lnyswz.doc.service.ButtonServiceI;
/**
 * 功能按钮Action
 * @author 王文阳
 *
 */
@Namespace("/admin")
@Action("buttonAction")
public class ButtonAction extends BaseAction implements ModelDriven<Button>{
    private static final long serialVersionUID = 1L;
    //private Logger logger = Logger.getLogger(ButtonAction.class);
    private Button button = new Button();
    private ButtonServiceI buttonService;

    /**
     * 添加功能按钮
     */
    public void add(){
        Json j = new Json();
        try{
            //将获得的前台内容传入Service
            Button b = buttonService.add(button);
            //添加成功
            j.setSuccess(true);
            j.setMsg("增加功能按钮成功");
            j.setObj(b);
        }catch(Exception e){
            j.setMsg("增加功能按钮失败");
            e.printStackTrace();
        }
        writeJson(j);
    }

    /**
     * 编辑功能按钮
     */
    public void edit(){
        Json j = new Json();
        try {
            //将获得的前台内容传入Service
            buttonService.edit(button);
            j.setSuccess(true);
            j.setMsg("修改功能按钮成功！");
        } catch (Exception e) {
            j.setMsg("修改功能按钮失败！");
            e.printStackTrace();
        }
        super.writeJson(j);
    }

    /**
     * 删除功能按钮
     */
    public void delete(){
        Json j = new Json();
        try{
            buttonService.delete(button.getIds());
            j.setSuccess(true);
            j.setMsg("删除功能按钮成功!");
        }catch(Exception e){
            j.setMsg("删除功能按钮失败!");
            e.printStackTrace();
        }
        super.writeJson(j);
    }

    /**
     * 根据权限获得功能按钮
     */
    public void buttons(){
        User user = (User)session.get("user");
        //超级管理员-全部功能按钮
        if(user.getUserName().equals("admin")){
            writeJson(buttonService.noAuthBtns(button.getMid(), button.getTabId()));
        }else{
            writeJson(buttonService.authBtns(user, button.getMid(), button.getTabId(), button.getDid()));
        }
    }

    /**
     * 返回所有功能按钮，供管理用，有分页
     */
    public void datagrid(){
        writeJson(buttonService.datagrid(button.getMid()));
    }

    @Override
    public Button getModel() {
        return button;
    }

    @Autowired
    public void setButtonService(ButtonServiceI buttonService) {
        this.buttonService = buttonService;
    }
}
