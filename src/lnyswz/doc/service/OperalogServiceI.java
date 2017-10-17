package lnyswz.doc.service;

import lnyswz.common.bean.DataGrid;
import lnyswz.doc.bean.Operalog;

public interface OperalogServiceI {
	public void delete(String ids);

	public DataGrid datagrid(Operalog ope);

}
