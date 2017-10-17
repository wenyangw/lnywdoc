package lnyswz.doc.service;

import lnyswz.common.bean.DataGrid;
import lnyswz.doc.bean.Person;


import java.util.List;

public interface PersonServiceI {
	public Person add(Person person);
	public void edit(Person person);
	public void delete(Person person);

	public List<Person> listByDep(Person person);
	
	public List<Person> getSelect(Person person);
	
	
	public DataGrid personSearchDatagrid(Person person);
	
	
	public Person getPerson(Person person);
	
	public boolean checkEname(Person person);
	
	public boolean checkEntry(Person person);
}
