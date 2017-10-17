package lnyswz.doc.bean;

/**
 * 角色类
 * @author wangwy
 *
 */
public class Role{

	private String id;
	private String roleName;
	private String description;
	private String did;
	private String dname;
	
	private int page;
	private int rows;
	private String ids;
	private String menuIds;
	private String menuNames;
	private String text;
	private String btnIds;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDid() {
		return did;
	}
	public void setDid(String did) {
		this.did = did;
	}
	public String getDname() {
		return dname;
	}
	public void setDname(String dname) {
		this.dname = dname;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public String getMenuIds() {
		return menuIds;
	}
	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}
	public String getMenuNames() {
		return menuNames;
	}
	public void setMenuNames(String menuNames) {
		this.menuNames = menuNames;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getBtnIds() {
		return btnIds;
	}
	public void setBtnIds(String btnIds) {
		this.btnIds = btnIds;
	}

}
