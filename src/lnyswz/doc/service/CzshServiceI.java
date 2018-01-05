package lnyswz.doc.service;

import lnyswz.common.bean.DataGrid;
import lnyswz.doc.bean.Czsh;

public interface CzshServiceI {
    public DataGrid datagrid(Czsh czsh);
    public String getAuditBz(Czsh czsh);
}
