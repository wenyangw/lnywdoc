package lnyswz.common.bean;

import java.util.List;

/**
 * datagrid模型
 * 
 * @author 王文阳
 * 
 */
public class DataGrid implements java.io.Serializable {

	private Long total;// 总记录数
	private Object obj;
	private List rows;// 每行记录
	private List footer;

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public List getRows() {
		return rows;
	}

	public void setRows(List rows) {
		this.rows = rows;
	}

	public List getFooter() {
		return footer;
	}

	public void setFooter(List footer) {
		this.footer = footer;
	}

}
