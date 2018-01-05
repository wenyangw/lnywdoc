package lnyswz.doc.service;

import lnyswz.common.bean.DataGrid;
import lnyswz.doc.bean.PersonSp;

public interface PersonSpServiceI {
	public PersonSp add(PersonSp person);

	public String getAuditLevel(PersonSp personSp);
	public DataGrid listAudits(PersonSp personSp);
	public DataGrid listFields(PersonSp personSp);

	public void updateAudit(PersonSp personSp);
	public void updateRefuse(PersonSp personSp);

	public PersonSp getPersonSp(PersonSp personSp);
	public DataGrid getPersonSps(PersonSp personSp);
}
