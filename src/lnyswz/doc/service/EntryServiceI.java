package lnyswz.doc.service;

import lnyswz.common.bean.DataGrid;
import lnyswz.doc.bean.Entry;

import java.util.List;

public interface EntryServiceI {
	public Entry add(Entry entry);
	public Entry edit(Entry entry);
	public void delete(Entry entry);

	public List<Entry> getLevelEntries(Entry entry);
	public long getEntryCount(int levelId, int personId);
	public List<Entry> searchEntry(Entry entry);
	public List<Entry> searchDoc(Entry entry);
	public List<Entry> searchLevel(Entry entry);
	public boolean checkEntryByLevel(Entry entry);
	public boolean checkDir(Entry entry);

	public DataGrid docsDatagrid(Entry entry);
	public DataGrid printEntrys(Entry entry);

}
