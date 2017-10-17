package lnyswz.doc.service;

import java.util.List;

import lnyswz.common.bean.DataGrid;
import lnyswz.doc.bean.Catalog;

public interface CatalogServiceI {
	public Catalog add(Catalog c);
	public void edit(Catalog c);
	public void delete(String ids);
	public List<Catalog> listCatas();
	public DataGrid datagrid(Catalog c);
}
