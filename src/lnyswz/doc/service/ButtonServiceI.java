package lnyswz.doc.service;

import java.util.List;

import lnyswz.common.bean.DataGrid;
import lnyswz.doc.bean.Button;
import lnyswz.doc.bean.User;

public interface ButtonServiceI {
	public Button add(Button b);
	public void edit(Button b);
	public void delete(String ids);
	public DataGrid datagrid(String mid);
	
	public List<Object> authBtns(User user, String mid, int tabId, String did);
	public List<Object> noAuthBtns(String mid, int tabId);
}
