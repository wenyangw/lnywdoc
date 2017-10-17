package lnyswz.doc.service;

import java.util.List;

import lnyswz.common.bean.TreeNode;
import lnyswz.doc.bean.Menu;
import lnyswz.doc.bean.User;

public interface MenuServiceI {
	public List<Menu> treegrid(Menu menu);
	public List<Menu> allTopTree();
	public Menu add(Menu menu);
	public void edit(Menu menu);
	public void delete(String id);
	public List<TreeNode> tree(Menu menu, Boolean b);
	public List<Menu> authTree(User user, String cid);
	public List<Menu> noAuthTree(String cid);
}
