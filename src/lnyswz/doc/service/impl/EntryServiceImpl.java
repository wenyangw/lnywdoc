package lnyswz.doc.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.Obj;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.doc.action.UploadAction;
import lnyswz.doc.bean.Entry;
import lnyswz.doc.bean.Level;
import lnyswz.doc.model.*;
import lnyswz.doc.service.EntryServiceI;
import lnyswz.doc.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 条目实现表
 * @author 王文阳
 *
 */
@Service("entryService")
public class EntryServiceImpl implements EntryServiceI {
	
	private BaseDaoI<TEntry> entryDao;
	private BaseDaoI<Entry> entryBeanDao;
	private BaseDaoI<TLevel> levelDao;
	private BaseDaoI<TImg> imgDao;
	private BaseDaoI<TPerson> personDao;
	private BaseDaoI<TOperalog> operalogDao;

	/**
	 * 增加条目项
	 */
	@Override
	public Entry add(Entry entry) {
		TEntry t = new TEntry();
		BeanUtils.copyProperties(entry, t);

		t.setCreateTime(new Date());
		TLevel tLevel = levelDao.load(TLevel.class, entry.getLevelId());
		t.setTLevel(tLevel);
		t.setTCat(tLevel.getTCat());
		t.setTPerson(personDao.load(TPerson.class, entry.getPersonId()));

		entryDao.save(t);

		entry.setId(t.getId() + Constant.ENTRY_ID_PLUS);
		entry.setCreateTime(t.getCreateTime());
		entry.setType("entry");

		OperalogServiceImpl.addOperalog(entry.getCreateId(), "", "", "" + t.getId(),
				"生成条目", operalogDao);

		return entry;
	}

	public static int getEntryId(int entryId, int extraInt, boolean flag){
		if(flag){
			return entryId + extraInt;
		}else{
			return entryId;
		}
	}

	/**
	 * 修改条目
	 */
	@Override
	public Entry edit(Entry entry) {
		TEntry t = null;
//		if(entry.getCatId() != null && entry.getCatId().equals(Constant.CATID_DOC)){
//			t = entryDao.get(TEntry.class, entry.getId());
//		}else {
//			t = entryDao.get(TEntry.class, entry.getId() - Constant.ENTRY_ID_PLUS);
//		}
		t = entryDao.get(TEntry.class, getEntryId(entry.getId(), - Constant.ENTRY_ID_PLUS, Constant.CATID_PERSON.equals(entry.getCatId())));

		BeanUtils.copyProperties(entry, t, new String[]{"id", "createTime", "dir"});

		boolean flag = false;

		//修改类别后，要改变文件位置
		if(entry.getLevelId() != t.getTLevel().getId()) {
			t.setTLevel(levelDao.load(TLevel.class, entry.getLevelId()));
			flag = true;
		}
		//修改条目路径后，要改变文件位置
		if(!entry.getDir().equals(t.getDir())) {
			t.setDir(entry.getDir());
			flag = true;
		}
		if(flag){
			String hql = "from TImg t where t.TEntry.id = :entryId";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("entryId", t.getId());

			List<TImg> imgLists = imgDao.find(hql, params);
			if (imgLists != null && imgLists.size() > 0){
				for (TImg tImg : imgLists) {
					String oldFilePath = tImg.getFilePath();
					String newFilePath = Constant.UPLOADFILE_PATH + "/" + getFilePath(t.getId(), Constant.CATID_PERSON.equals(entry.getCatId()) ? null : "documents") + oldFilePath.substring(oldFilePath.lastIndexOf("/")).toLowerCase();
					if(UploadAction.moveFile(oldFilePath, newFilePath)){
						tImg.setFilePath(newFilePath);
						imgDao.save(tImg);
					}
				}
			}
		}

		OperalogServiceImpl.addOperalog(entry.getCreateId(), "", "", "" + t.getId(),
				"修改条目", operalogDao);

		return entry;
	}
	
	/**
	 * 删除条目项
	 */
	@Override
	public void delete(Entry entry) {
//		if(Constant.CATID_PERSON.equals(entry.getCatId())) {
//			entryDao.delete(entryDao.get(TEntry.class, entry.getId() - Constant.ENTRY_ID_PLUS));
//		}else{
//			entryDao.delete(entryDao.get(TEntry.class, entry.getId()));
//		}
		entryDao.delete(entryDao.get(TEntry.class, getEntryId(entry.getId(), - Constant.ENTRY_ID_PLUS, Constant.CATID_PERSON.equals(entry.getCatId()))));

		OperalogServiceImpl.addOperalog(entry.getCreateId(), "", "", "" + entry.getId(),
				"删除条目", operalogDao);
	}

	/**
	 * 获取类别、条目
	 */
	@Override
	public List<Entry> getLevelEntries(Entry entry) {
		//获取类别
		Level level = new Level();
		level.setId(entry.getId());
		level.setCatId(entry.getCatId());

		List<TLevel> levelList = LevelServiceImpl.getTLevels(level, levelDao);

		List<Entry> nl = null;
		Entry e = null;
		if(levelList.size() > 0) {
			nl = new ArrayList<Entry>();
			for (TLevel tLevel : levelList) {
				e = new Entry();
				BeanUtils.copyProperties(tLevel, e);
				e.setEntryName(tLevel.getLevelName());
				e.setPageCount((int)getEntryCount(tLevel.getId(), entry.getPersonId()));
				if (tLevel.getTLevels().size() > 0) {
					e.setType("level");
					e.setState("closed");
				} else {
					e.setIconCls("icon-folder-closed");
					e.setPersonId(entry.getPersonId());
					e.setLevelId(tLevel.getId());
					e.setType("level_print");
					if (getEntries(e).size() > 0) {
						e.setState("closed");
					} else {
						e.setState("open");
					}
				}
				nl.add(e);
			}
		}else{
			return getEntries(entry);
		}
		return nl;
	}

	private List<Entry> getEntries(Entry entry) {
		String hql = "from TEntry t where t.TLevel.id = :levelId and t.TPerson.id = :personId order by orderNum";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("levelId", entry.getId());
		params.put("personId", entry.getPersonId());

		List<TEntry> l = entryDao.find(hql, params);
		List<Entry> nl = new ArrayList<Entry>();
		Entry e = null;
		if (l != null && l.size() > 0) {
			nl = new ArrayList<Entry>();
			for (TEntry tEntry : l) {
				e = new Entry();
				BeanUtils.copyProperties(tEntry, e);
				e.setId(tEntry.getId() + Constant.ENTRY_ID_PLUS);
				e.setType("entry");
				e.setPageCount((int)ImgServiceImpl.getImgCount(tEntry.getId(), entryDao));
				e.setLevelId(tEntry.getTLevel().getId());
				nl.add(e);
			}
		}
		return nl;
	}

	private String getFilePath(int entryId, String rootPath){
		TEntry tEntry = entryDao.get(TEntry.class, entryId);

		List<String> paths = new ArrayList<String>();

		if(rootPath == null) {
			paths.add(tEntry.getTPerson().getEname());
		}else{
			paths.add(rootPath);
		}
		TLevel tLevel = tEntry.getTLevel();
		if(tLevel.getTLevel() != null){
			paths.add(tLevel.getTLevel().getDir());
		}
		paths.add(tLevel.getDir());
		paths.add(tEntry.getDir());
		return StringUtils.join(paths.toArray(), "/");
	}

	@Override
	public long getEntryCount(int levelId, int personId){
		long count = 0;

		if(levelId != 0) {
			TLevel tLevel = levelDao.load(TLevel.class, levelId);
			if (tLevel.getTLevels() != null && tLevel.getTLevels().size() > 0) {
				for (TLevel level : tLevel.getTLevels()) {
					count += getEntryCount(level.getId(), personId);
				}
			} else {
				String hql = "from TEntry t where t.TLevel.id = :levelId and t.TPerson.id = :personId";
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("levelId", levelId);
				params.put("personId", personId);
				List<TEntry> el = entryDao.find(hql, params);

				if (el != null && el.size() > 0) {
					for (TEntry tEntry : el) {
						count += ImgServiceImpl.getImgCount(tEntry.getId(), entryDao);
					}
				}
			}
		}else{
			String hql = "from TEntry t where t.TPerson.id = :personId";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("personId", personId);
			List<TEntry> el = entryDao.find(hql, params);

			if (el != null && el.size() > 0) {
				for (TEntry tEntry : el) {
					count += ImgServiceImpl.getImgCount(tEntry.getId(), entryDao);
				}
			}
		}
		return count;
	}

	private long getEntryCountByLevel(int levelId, List<TEntry> entryList){
		long count = 0;

		TLevel tLevel = null;
		for (TEntry tEntry : entryList) {
			tLevel = tEntry.getTLevel();
			if(tLevel.getId() == levelId){
				count += ImgServiceImpl.getImgCount(tEntry.getId(), entryDao);
			}
			if(tLevel.getTLevel() != null && tLevel.getTLevel().getId() == levelId){
				count += ImgServiceImpl.getImgCount(tEntry.getId(), entryDao);
			}
		}
		return count;
	}

	private long getEntryCountByPerson(int personId, List<TEntry> entryList){
		long count = 0;

		for (TEntry tEntry : entryList) {
			if(tEntry.getTPerson().getId() == personId){
				count += ImgServiceImpl.getImgCount(tEntry.getId(), entryDao) ;
			}
		}
		return count;
	}

	@Override
	public boolean checkEntryByLevel(Entry entry){
		String hql = "from TEntry t where t.TLevel.id = :levelId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("levelId", entry.getLevelId());

		List<TEntry> tEntrys = entryDao.find(hql, params);
		if(tEntrys.size() > 0){
			return true;
		}
		return false;
	}

	/**
	 * 验证是否存在相同的dir，存在返回true
	 * @param entry
	 * @return
	 */
	@Override
	public boolean checkDir(Entry entry){
		String hql = "from TEntry t where t.TLevel.id = :levelId and t.TPerson.id = :personId and t.dir = :dir";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("levelId", entry.getLevelId());
		params.put("personId", entry.getPersonId());
		params.put("dir", entry.getDir());

		List<TEntry> tEntrys = entryDao.find(hql, params);
		if(tEntrys.size() > 0){
			return true;
		}
		return false;
	}

	@Override
	public List<Entry> searchLevel(Entry entry){
		String hql = "from TLevel t where t.TCat.id = :catId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("catId", entry.getCatId());
		ArrayList<Obj> objs = JSON.parseObject(entry.getSearch(), new TypeReference<ArrayList<Obj>>(){});
		String where = "";
		for (Obj obj : objs) {
			if(obj.getValue().indexOf(" ") > 0){
				String[] values = obj.getValue().split(" ");
				for (int i = 0; i < values.length; i++) {
					if(i != 0){
						where += " OR ";
					}
					where += obj.getField() + " like :" + obj.getField() + i;
					params.put(obj.getField() + i, "%" + values[i] + "%");
				}
			}else{
				where += obj.getField() + " like :" + obj.getField();
				params.put(obj.getField(), "%" + obj.getValue() + "%");
			}

		}
		if(!where.equals("")) {
			hql += " AND " + where;
		}

		//获取满足条件的level列表
		List<TLevel> levelList = levelDao.find(hql, params);
		if(levelList.size() > 0){
			//获取level下包含的entry
			List<TEntry> entryList = new ArrayList<TEntry>();
			for (TLevel tLevel : levelList) {
				if(tLevel.getTEntrys().size() > 0) {
					if(entry.getPersonId() != 0){
						for (TEntry tEntry : tLevel.getTEntrys()) {
							if(tEntry.getTPerson().getId() == entry.getPersonId()){
								entryList.add(tEntry);
							}
						}
					}else{
						entryList.addAll(tLevel.getTEntrys());
					}

				}
			}
			return getEntries(entry, entryList);
		}
		return new ArrayList<Entry>();
	}

	@Override
	public List<Entry> searchEntry(Entry entry){
		String hql = "from TEntry t where t.TCat.id = :catId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("catId", entry.getCatId());
        ArrayList<Obj> objs = JSON.parseObject(entry.getSearch(), new TypeReference<ArrayList<Obj>>(){});
        String where = "";
        for (Obj obj : objs) {
            if(!where.equals("")){
                if(entry.getLogicOr().equals("1")){
                    where += " OR ";
                }else {
                    where += " AND ";
                }
            }
            if(obj.getValue().indexOf(" ") > 0){
            	where += "(";
            	String[] values = obj.getValue().split(" ");
				for (int i = 0; i < values.length; i++) {
					if(i != 0){
						where += " OR ";
					}
					where += obj.getField() + " like :" + obj.getField() + i;
					params.put(obj.getField() + i, "%" + values[i] + "%");
				}
				where += ")";
			}else{
				where += obj.getField() + " like :" + obj.getField();
				params.put(obj.getField(), "%" + obj.getValue() + "%");
			}

        }
        if(!where.equals("")) {
			hql += " and (" + where + ")";
		}
		if(Constant.CATID_PERSON.equals(entry.getCatId())) {
			if (entry.getPersonId() != 0) {
				hql += " and t.TPerson.id = :personId";
				params.put("personId", entry.getPersonId());
			}
		}

		//获取满足条件的entry列表
		List<TEntry> entryList = entryDao.find(hql, params);

		if(entryList != null && entryList.size() > 0) {
			//if(Constant.CATID_PERSON.equals(entry.getCatId())) {
				return getEntries(entry, entryList);
//			}else{
//				return getDocuments(entry, entryList);
//			}
		}

		return new ArrayList<Entry>();
	}

	/**
	 * 处理查询结果
	 * @param entry 前台传入的对象，包含查询的内容，行id，人员id
	 * @param entryList 满足查询条件的entry列表
	 * @return 返回treegrid格式
	 */
	private List<Entry> getEntries(Entry entry,  List<TEntry> entryList) {
		List<Entry> entrys = new ArrayList<Entry>();
		Entry e;

		//满足条件的查询，返回人员
		if (null == entry.getType()) {
			Set<TPerson> personSet = new HashSet<TPerson>();

			for (TEntry tEntry : entryList) {
				personSet.add(tEntry.getTPerson());
			}
			List<TPerson> persons = new ArrayList<TPerson>(personSet);
			Collections.sort(persons, new PersonComparator());
			for (TPerson tPerson : persons) {
				e = new Entry();
				e.setId(tPerson.getId());
				e.setEntryName(tPerson.getName());
				e.setPersonId(tPerson.getId());
				e.setState("closed");
				e.setType("person");
				e.setPageCount((int)getEntryCountByPerson(tPerson.getId(), entryList));
				entrys.add(e);
			}
		}
		//人员下的查询，返回一级类别
		if ("person".equals(entry.getType())) {
			TLevel tLevel = null;
			Set<TLevel> levelSet = new HashSet<TLevel>();
			for (TEntry tEntry : entryList) {
				tLevel = tEntry.getTLevel();
				if (tLevel.getTLevel() != null) {
					levelSet.add(tLevel.getTLevel());
				} else {
					levelSet.add(tLevel);
				}
			}

			List<TLevel> levels = new ArrayList<TLevel>(levelSet);
			Collections.sort(levels, new LevelComparator());
			for (TLevel level : levels) {
				e = new Entry();
				e.setId(level.getId() + getExtraIdOfLevel1(entry.getId()));
				e.setEntryName(level.getLevelName());
				e.setState("closed");
				e.setType("level1");
				e.setPersonId(entry.getId());
				e.setPageCount((int)getEntryCountByLevel(level.getId(), entryList));
				entrys.add(e);
			}
		}
		//一级类别下的查询，返回二级类或条目
		if ("level1".equals(entry.getType())) {
			TLevel tLevel = null;
			Set<TLevel> levelSet = new HashSet<TLevel>();
			for (TEntry tEntry : entryList) {
				//获取entry的类别
				tLevel = tEntry.getTLevel();
				e = new Entry();
				//类别与点击查询的类别一致
				if (tLevel.getId() == (entry.getId() - getExtraIdOfLevel1(entry.getPersonId()))) {
					e.setId(tEntry.getId() + getExtraIdOfEntry(entry.getPersonId()));
					e.setEntryName(tEntry.getEntryName());
					e.setEntryId(tEntry.getId() + Constant.ENTRY_ID_PLUS);
					e.setType("entry");
					e.setPageCount((int) ImgServiceImpl.getImgCount(tEntry.getId(), entryDao));
					entrys.add(e);
				}
				//类别有父类，并且父类id与查询的类别一致
				if (tLevel.getTLevel() != null && tLevel.getTLevel().getId() == (entry.getId() - getExtraIdOfLevel1(entry.getPersonId()))) {
					levelSet.add(tLevel);
				}
			}
			if(levelSet.size() > 0) {
				List<TLevel> levels = new ArrayList<TLevel>(levelSet);
				Collections.sort(levels, new LevelComparator());
				for (TLevel level : levels) {
					e = new Entry();
					e.setId(level.getId() + getExtraIdOfLevel2(entry.getPersonId()));
					e.setEntryName(level.getLevelName());
					e.setType("level2");
					e.setState("closed");
					e.setPersonId(entry.getPersonId());
					e.setPageCount((int)getEntryCountByLevel(level.getId(), entryList));
					entrys.add(e);
				}
			}

		}
		//一级类别下的查询，返回二级类或条目
		if ("level2".equals(entry.getType())) {
			TLevel tLevel = null;

			Collections.sort(entryList, new EntryComparator());
			for (TEntry tEntry : entryList) {
				//获取entry的类别
				tLevel = tEntry.getTLevel();
				e = new Entry();
				//类别与点击查询的类别一致
				if (tLevel.getId() == (entry.getId() - getExtraIdOfLevel2(entry.getPersonId()))) {
					e.setId(tEntry.getId() + getExtraIdOfEntry(entry.getPersonId()));
					e.setEntryName(tEntry.getEntryName());
					e.setEntryId(tEntry.getId() + Constant.ENTRY_ID_PLUS);
					e.setType("entry");
					e.setPageCount((int)ImgServiceImpl.getImgCount(tEntry.getId(), entryDao));
					entrys.add(e);
				}
			}
		}
		//return new ArrayList<Entry>(entrySets);
		return entrys;
	}

	private int getExtraIdOfLevel1(int personId){
		return Constant.Level1_ID_PLUS + personId * Constant.ID_PLUS;
	}

	private int getExtraIdOfLevel2(int personId){
		return Constant.Level2_ID_PLUS + personId * Constant.ID_PLUS;
	}

	private int getExtraIdOfEntry(int personId){
		return Constant.ENTRY_ID_PLUS + personId * Constant.ID_PLUS;
	}


	@Override
	public DataGrid docsDatagrid(Entry entry) {
		String hql = "from TEntry t where t.TLevel.id = :levelId and t.TCat.id = :catId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("levelId", entry.getLevelId());
		params.put("catId", entry.getCatId());

		List<TEntry> tEntrys = entryDao.find(hql, params, entry.getPage(), entry.getRows());

		List<Entry> entrys = new ArrayList<Entry>();
		Entry e = null;
		for (TEntry tEntry : tEntrys) {
			e = new Entry();
			BeanUtils.copyProperties(tEntry, e);

			e.setLevelId(tEntry.getTLevel().getId());

			e.setPageCount((int)ImgServiceImpl.getImgCount(tEntry.getId(), imgDao));
			//TODO


			entrys.add(e);
		}
		String countHql = "select count(*) " + hql;

		DataGrid dg = new DataGrid();
		dg.setRows(entrys);
		dg.setTotal(entryDao.count(countHql, params));
		return dg;
	}

	@Override
	public DataGrid	printEntrys(Entry entry) {
		DataGrid datagrid = new DataGrid();
		List<Entry> nl = new ArrayList<Entry>();
		Entry e = null;

		String[] ids = entry.getIds().split(",");

		TEntry tEntry = null;
		for (String id : ids) {
			e = new Entry();
			tEntry = entryDao.load(TEntry.class, Integer.parseInt(id));
			e.setOrderNum(tEntry.getOrderNum());
			e.setOwner(tEntry.getOwner());
			e.setFileno(tEntry.getFileno());
			e.setEntryName(tEntry.getEntryName());
			e.setRecordTime(tEntry.getRecordTime());
			e.setPageCount((int)ImgServiceImpl.getImgCount(tEntry.getId(), imgDao));
			e.setBz(tEntry.getBz());

			nl.add(e);
		}

		Collections.sort(nl, new Entry2Comparator());

		int num = nl.size();
		if (num < Constant.PAGE_DOCUMENT_SIZE) {
			for (int i = 0; i < (Constant.PAGE_DOCUMENT_SIZE - num); i++) {
				nl.add(new Entry());
			}
		}

		datagrid.setRows(nl);
		return datagrid;
	}

	/**
	 * 处理查询结果
	 * @param entry 前台传入的对象，包含查询的内容，行id，人员id
	 * @param entryList 满足查询条件的entry列表
	 * @return 返回treegrid格式
	 */
	private List<Entry> getDocuments(Entry entry,  List<TEntry> entryList) {
		List<Entry> entrys = new ArrayList<Entry>();
		Entry e;

		//满足条件的查询，返回人员
		if (null == entry.getType()) {
			Set<TPerson> personSet = new HashSet<TPerson>();

			for (TEntry tEntry : entryList) {
				personSet.add(tEntry.getTPerson());
			}
			List<TPerson> persons = new ArrayList<TPerson>(personSet);
			Collections.sort(persons, new PersonComparator());
			for (TPerson tPerson : persons) {
				e = new Entry();
				e.setId(tPerson.getId());
				e.setEntryName(tPerson.getName());
				e.setPersonId(tPerson.getId());
				e.setState("closed");
				e.setType("person");
				e.setPageCount((int)getEntryCountByPerson(tPerson.getId(), entryList));
				entrys.add(e);
			}
		}
		//人员下的查询，返回一级类别
		if ("person".equals(entry.getType())) {
			TLevel tLevel = null;
			Set<TLevel> levelSet = new HashSet<TLevel>();
			for (TEntry tEntry : entryList) {
				tLevel = tEntry.getTLevel();
				if (tLevel.getTLevel() != null) {
					levelSet.add(tLevel.getTLevel());
				} else {
					levelSet.add(tLevel);
				}
			}

			List<TLevel> levels = new ArrayList<TLevel>(levelSet);
			Collections.sort(levels, new LevelComparator());
			for (TLevel level : levels) {
				e = new Entry();
				e.setId(level.getId() + getExtraIdOfLevel1(entry.getId()));
				e.setEntryName(level.getLevelName());
				e.setState("closed");
				e.setType("level1");
				e.setPersonId(entry.getId());
				e.setPageCount((int)getEntryCountByLevel(level.getId(), entryList));
				entrys.add(e);
			}
		}
		//一级类别下的查询，返回二级类或条目
		if ("level1".equals(entry.getType())) {
			TLevel tLevel = null;
			Set<TLevel> levelSet = new HashSet<TLevel>();
			for (TEntry tEntry : entryList) {
				//获取entry的类别
				tLevel = tEntry.getTLevel();
				e = new Entry();
				//类别与点击查询的类别一致
				if (tLevel.getId() == (entry.getId() - getExtraIdOfLevel1(entry.getPersonId()))) {
					e.setId(tEntry.getId() + getExtraIdOfEntry(entry.getPersonId()));
					e.setEntryName(tEntry.getEntryName());
					e.setEntryId(tEntry.getId() + Constant.ENTRY_ID_PLUS);
					e.setType("entry");
					e.setPageCount((int) ImgServiceImpl.getImgCount(tEntry.getId(), entryDao));
					entrys.add(e);
				}
				//类别有父类，并且父类id与查询的类别一致
				if (tLevel.getTLevel() != null && tLevel.getTLevel().getId() == (entry.getId() - getExtraIdOfLevel1(entry.getPersonId()))) {
					levelSet.add(tLevel);
				}
			}
			if(levelSet.size() > 0) {
				List<TLevel> levels = new ArrayList<TLevel>(levelSet);
				Collections.sort(levels, new LevelComparator());
				for (TLevel level : levels) {
					e = new Entry();
					e.setId(level.getId() + getExtraIdOfLevel2(entry.getPersonId()));
					e.setEntryName(level.getLevelName());
					e.setType("level2");
					e.setState("closed");
					e.setPersonId(entry.getPersonId());
					e.setPageCount((int)getEntryCountByLevel(level.getId(), entryList));
					entrys.add(e);
				}
			}

		}
		//一级类别下的查询，返回二级类或条目
		if ("level2".equals(entry.getType())) {
			TLevel tLevel = null;

			Collections.sort(entryList, new EntryComparator());
			for (TEntry tEntry : entryList) {
				//获取entry的类别
				tLevel = tEntry.getTLevel();
				e = new Entry();
				//类别与点击查询的类别一致
				if (tLevel.getId() == (entry.getId() - getExtraIdOfLevel2(entry.getPersonId()))) {
					e.setId(tEntry.getId() + getExtraIdOfEntry(entry.getPersonId()));
					e.setEntryName(tEntry.getEntryName());
					e.setEntryId(tEntry.getId() + Constant.ENTRY_ID_PLUS);
					e.setType("entry");
					e.setPageCount((int)ImgServiceImpl.getImgCount(tEntry.getId(), entryDao));
					entrys.add(e);
				}
			}
		}
		//return new ArrayList<Entry>(entrySets);
		return entrys;
	}


	@Autowired
	public void setEntryDao(BaseDaoI<TEntry> entryDao) {
		this.entryDao = entryDao;
	}

	@Autowired
	public void setEntryBeanDao(BaseDaoI<Entry> entryBeanDao) {
		this.entryBeanDao = entryBeanDao;
	}

	@Autowired
	public void setLevelDao(BaseDaoI<TLevel> levelDao) {
		this.levelDao = levelDao;
	}

	@Autowired
	public void setImgDao(BaseDaoI<TImg> imgDao) {
		this.imgDao = imgDao;
	}

	@Autowired
	public void setPersonDao(BaseDaoI<TPerson> personDao) {
		this.personDao = personDao;
	}

	@Autowired
	public void setOperalogDao(BaseDaoI<TOperalog> operalogDao) {
		this.operalogDao = operalogDao;
	}
}
