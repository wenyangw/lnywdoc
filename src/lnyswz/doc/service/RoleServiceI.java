package lnyswz.doc.service;

import java.util.List;

import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.TreeNode;
import lnyswz.doc.bean.Role;

public interface RoleServiceI {
	public Role add(Role role);
	public void edit(Role rrole);
	public void delete(String ids);
	public DataGrid datagrid(Role role);
	public List<Role> listRoles();
	public List<TreeNode> tree(Role r);
	public void auth(Role role);
}
