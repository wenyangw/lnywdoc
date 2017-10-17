package lnyswz.doc.service.impl;

import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.TreeNode;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.common.util.DateUtil;
import lnyswz.doc.action.UploadAction;
import lnyswz.doc.bean.Entry;
import lnyswz.doc.bean.Level;
import lnyswz.doc.model.*;
import lnyswz.doc.service.LevelServiceI;
import lnyswz.doc.util.Constant;
import lnyswz.doc.util.LevelComparator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 类别实现表
 * @author 王文阳
 *
 */
@Service("levelService")
public class LevelServiceImpl implements LevelServiceI {
	
	private BaseDaoI<TLevel> levelDao;
	private BaseDaoI<TEntry> entryDao;
	private BaseDaoI<TImg> imgDao;
	private BaseDaoI<TCat> catDao;
	private BaseDaoI<TOperalog> operalogDao;

	/**
	 * 增加类别项
	 */
	@Override
	public void add(Level level) {
		TLevel t = new TLevel();
		BeanUtils.copyProperties(level, t);
		TCat tCat = catDao.load(TCat.class, level.getCatId());
		t.setTCat(tCat);

		//是否有上级菜单
		if(level.getPid() > 0){
			TLevel parent = levelDao.get(TLevel.class, level.getPid());
			//设置上级菜单
			t.setTLevel(parent);
		}
		levelDao.save(t);

		OperalogServiceImpl.addOperalog(level.getCreateId(), "", "", "" + t.getId(),
				"生成类别", operalogDao);
	}
	
	/**
	 * 修改类别
	 */
	@Override
	public void edit(Level level) {
		TLevel t = levelDao.get(TLevel.class, level.getId());



		boolean flag = false;

		String oldDir = Constant.UPLOADFILE_PATH + (t.getTLevel() != null ? t.getTLevel().getDir() : "") + "/" + t.getDir();

		//修改父类别后，要改变设置父类并更改文件位置
		if(t.getTLevel() != null && level.getPid() != t.getTLevel().getId()) {
			t.setTLevel(levelDao.get(TLevel.class, level.getPid()));
			flag = true;
		}
		//修改类别路径后，要改变文件位置
		if(!level.getDir().equals(t.getDir())) {
			flag = true;
		}
		if(flag){
			String hql = "from TImg t where t.filePath like :levelDir";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("levelDir", "%" + oldDir + "%");

			List<TImg> imgLists = imgDao.find(hql, params);
			if (imgLists != null && imgLists.size() > 0){
				for (TImg tImg : imgLists) {
					String oldFilePath = tImg.getFilePath();
					String newFilePath = Constant.UPLOADFILE_PATH + "/" + getFilePath(tImg.getTEntry().getId()) + oldFilePath.substring(oldFilePath.lastIndexOf("/")).toLowerCase();
					if(UploadAction.moveFile(oldFilePath, newFilePath)){
						tImg.setFilePath(newFilePath);
						imgDao.save(tImg);
					}
				}
			}
		}

		BeanUtils.copyProperties(level, t);

		if(!level.getCatId().equals(t.getTCat().getId())){
			TCat tCat = catDao.load(TCat.class, level.getCatId());
			t.setTCat(tCat);
		}

		OperalogServiceImpl.addOperalog(level.getCreateId(), "", "", "" + t.getId(),
				"修改类别", operalogDao);
	}

	private String getFilePath(int entryId){
		TEntry tEntry = entryDao.get(TEntry.class, entryId);

		List<String> paths = new ArrayList<String>();
		paths.add(tEntry.getTPerson().getEname());
		TLevel tLevel = tEntry.getTLevel();
		if(tLevel.getTLevel() != null){
			paths.add(tLevel.getTLevel().getDir());
		}
		paths.add(tLevel.getDir());
		paths.add(tEntry.getDir());
		return StringUtils.join(paths.toArray(), "/");
	}
	
	/**
	 * 删除类别项
	 */
	@Override
	public void delete(Level level) {
		levelDao.delete(levelDao.get(TLevel.class, level.getId()));

		OperalogServiceImpl.addOperalog(level.getCreateId(), "", "", "" + level.getId(),
				"删除类别", operalogDao);
	}

	/**
	 * 获取类别，管理使用，异步
	 */
	@Override
	public List<Level> treegrid(Level level) {
		return changeTree(getTLevels(level, levelDao), false);

	}

	@Override
	public List<TreeNode> getLevelTree(Level level, Boolean b) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from TLevel t where t.TLevel is null";
		if (level.getPid() > 0) {
			hql = "from TLevel t where t.TLevel.id = :pid";
			params.put("id", level.getPid());
		}
		if(level.getCatId() != null){
			hql += " and t.TCat.id = :catId";
			params.put("catId", level.getCatId());
		}

		List<TLevel> l = levelDao.find(hql, params);
		Collections.sort(l, new LevelComparator());
		List<TreeNode> tree = new ArrayList<TreeNode>();
		for (TLevel t : l) {
			tree.add(this.tree(t, true));
		}
		return tree;
	}

	private TreeNode tree(TLevel t, boolean recursive) {
		TreeNode node = new TreeNode();
		node.setId("" + t.getId());
		node.setText(t.getLevelName());

		if (t.getTLevels() != null && t.getTLevels().size() > 0) {
			node.setState("closed");
			if (recursive) {// 递归查询子节点
				List<TLevel> l = new ArrayList<TLevel>(t.getTLevels());
				Collections.sort(l, new LevelComparator());// 排序
				List<TreeNode> children = new ArrayList<TreeNode>();
				for (TLevel r : l) {
					TreeNode tn = tree(r, true);
					children.add(tn);
				}
				node.setChildren(children);
				node.setState("open");
			}
		}
		return node;
	}

	/**
	 * 只获得一级菜单
	 */
	@Override
	public List<Level> allTopTree() {
		String hql = "from TLevel t where t.TLevel is null";
		return changeTree(levelDao.find(hql), true);
	}

	/**
	 * 将数据库中获得的类别项转换为传入页面的类别项
	 * @param l
	 * @param isTop
	 * @return
	 */
	private static List<Level> changeTree(List<TLevel> l, boolean isTop){
		//排序
		//Collections.sort(l, new Level2Comparator());
		List<Level> nl = new ArrayList<Level>();
		for(TLevel t : l){
			Level m = new Level();
			BeanUtils.copyProperties(t, m);

			m.setCatId(t.getTCat().getId());
			m.setCatName(t.getTCat().getCatName());

			//获得上级菜单
			TLevel pm = t.getTLevel();

			//有上级菜单
			if(pm != null){

				//设置上级菜单id
				m.setPid(pm.getId());
				//设置上级菜单名称
				m.setPname(pm.getLevelName());

				//m.setState("open");
			}
			//是否有子菜单
			if(t.getTLevels().size() > 0 && !isTop){
				//设置页面菜单可展开
				m.setState("closed");
			}
			nl.add(m);
		}
		//Collections.sort(nl, new Level2Comparator());
		return nl;
	}

	public static List<TLevel> getTLevels(Level level, BaseDaoI<TLevel> levelDao) {
		String hql = "";
		Map<String, Object> params = new HashMap<String, Object>();
		//有传入类别项，为二级类别
		if(level != null && level.getId() != 0){
			hql = "from TLevel t where t.TLevel.id = :pid";
			params.put("pid", level.getId());
		}else {
			//获得一级菜单
			hql = "from TLevel t where t.TLevel is null";
		}
		if(level.getCatId() != null){
		    hql += " and t.TCat.id = :catId";
		    params.put("catId", level.getCatId());
        }
		hql += " order by t.TCat.id, t.orderNum";
		return levelDao.find(hql, params);
	}

	public List<Level> searchLevel(Level level){
		//返回列表
		List<Entry> results = new ArrayList<Entry>();
		//类别层级
		Map<String, List<Entry>> levelMap = new HashMap<String, List<Entry>>();
		//人员、条目
		Map<String, List<Entry>> entryMap = new HashMap<String, List<Entry>>();

		//收集查询到的Level,按层级放入Map,保证无论搜索的是一级类还是二级类，都要全部内容
		String hql = "from TLevel t where t.levelName like :search";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("search", "%" + level.getSearch() + "%");
		List<TLevel> levels = levelDao.find(hql, params);

		if(levels != null && levels.size() > 0 ) {
			for (TLevel tLevel : levels) {
				if (tLevel.getTLevel() == null) {
					//一级类别
					//levelMap中未包含一级类，扫描全部二级类并添加到levelMap中
					//levelMap中已包含一级类，不再处理
					if (!levelMap.containsKey(tLevel.getLevelName())) {
						levelMap.putAll(getLevelMap(tLevel));
					}
				} else {
					//二级类别
					if (!levelMap.containsKey(tLevel.getTLevel().getLevelName())) {
						levelMap.putAll(getLevelMap(tLevel.getTLevel()));
					}
				}
			}

			//循环level
			for (String key : levelMap.keySet()) {
				if(levelMap.get(key) != null){
					for (Entry entry : levelMap.get(key)) {
						String entryHql = "from TEntry t where t.TLevel.id = :levelId";
						Map<String, Object> entryParams = new HashMap<String, Object>();
						entryParams.put("levelId", entry.getId());
						List<TEntry> entryList = entryDao.find(entryHql, entryParams);
						if(entryList != null && entryList.size() > 0){
							List<Entry> entrys = null;
							Entry ee = null;
							for (TEntry tEntry : entryList) {
								if(entryMap.containsKey(tEntry.getTPerson().getName())){
									ee = new Entry();
									ee.setId(tEntry.getId());
									ee.setEntryName(tEntry.getEntryName());
									entryMap.get(tEntry.getTPerson().getName()).add(ee);
								}else{
									entrys = new ArrayList<Entry>();
									ee = new Entry();
									ee.setId(tEntry.getId());
									ee.setEntryName(tEntry.getEntryName());
									entrys.add(ee);
									entryMap.put(tEntry.getTPerson().getName(), entrys);
								}
							}
						}
					}
				}
			}
		}

		return null;
	}

	private Map<String, List<Entry>> getLevelMap(TLevel t){
		Map<String, List<Entry>> result = new HashMap<String, List<Entry>>();
		List<Entry> levelList = null;
		Entry e = null;
		if(t.getTLevels() != null && t.getTLevels().size() > 0){
			levelList = new ArrayList<Entry>();
			for (TLevel tLevel : t.getTLevels()) {
				e = new Entry();
				e.setId(tLevel.getId());
				e.setEntryName(tLevel.getLevelName());
				levelList.add(e);
			}
		}
		result.put(t.getLevelName(), levelList);
		return result;
	}

	public static List<TreeNode> getLevelNodes(int levelId, BaseDaoI<TLevel> baseDao){
		List<TreeNode> result = new ArrayList<TreeNode>();

		TLevel tLevel = baseDao.load(TLevel.class, levelId);

		TreeNode tr = new TreeNode();

		if(tLevel.getTLevel() == null){
			tr.setId(new Date().toString());
			tr.setText(tLevel.getLevelName());
			tr.setState("closed");
			result.add(tr);
		}else{
			tr.setId(new Date().toString());
			tr.setText(tLevel.getTLevel().getLevelName());
			tr.setState("closed");
			result.add(tr);
			tr = new TreeNode();
			tr.setId(new Date().toString());
			tr.setText(tLevel.getLevelName());
			tr.setState("closed");
			result.add(tr);
		}

		return result;
	}

	/**
	 * 验证是否存在相同的dir，存在返回true
	 * @param level
	 * @return
	 */
	@Override
	public boolean checkDir(Level level){
		String hql;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dir", level.getDir());
		if(level.getPid() == 0){
			hql = "from TLevel t where t.TLevel = null and t.dir = :dir";
		}else{
			hql = "from TLevel t where t.TLevel.id = :pid and t.dir = :dir";
			params.put("pid", level.getPid());
		}

		List<TLevel> tLevels = levelDao.find(hql, params);
		if(tLevels.size() > 0){
			return true;
		}
		return false;
	}

	@Override
	public DataGrid printLevel(Level level) {
		DataGrid datagrid = new DataGrid();
		List<Entry> nl = new ArrayList<Entry>();
		Entry entry = null;

		TLevel tLevel = levelDao.load(TLevel.class, level.getId());
		//如有父类别先添加父类别
		if(tLevel.getTLevel() != null){
			entry = new Entry();
			entry.setEntryName(tLevel.getTLevel().getLevelName());
			entry.setState(getOrderForPrint(tLevel.getTLevel().getOrderNum(), Constant.TYPE_LEVEL1));
			nl.add(entry);
		}
		entry = new Entry();
		entry.setEntryName(tLevel.getLevelName());
		//有父类时，序号要处理
		if(nl.size() > 0){
			entry.setState(getOrderForPrint(tLevel.getOrderNum(), Constant.TYPE_LEVEL2));
		}else{
			entry.setState(getOrderForPrint(tLevel.getOrderNum(), Constant.TYPE_LEVEL1));
		}
		nl.add(entry);

		String hql = "from TEntry t where t.TLevel.id = :levelId and t.TPerson.id = :personId order by t.orderNum";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("levelId", level.getId());
		params.put("personId", level.getPersonId());

		List<TEntry> tEntrys = entryDao.find(hql, params);

		for (TEntry t : tEntrys) {
			entry = new Entry();
			entry.setEntryName(t.getEntryName());
			entry.setRecordTime(t.getRecordTime());
			entry.setState(getOrderForPrint(t.getOrderNum(), Constant.TYPE_ENTRY));
			entry.setPageCount((int)ImgServiceImpl.getImgCount(t.getId(), imgDao));
			nl.add(entry);

		}
		int num = nl.size();

		if (num % Constant.PAGE_SIZE > 0) {
			for (int i = 0; i < (Constant.PAGE_SIZE - num % Constant.PAGE_SIZE ); i++) {
				nl.add(new Entry());
			}
		}

		//Map<String, Object> map = new HashMap<String, Object>();
		//datagrid.setObj(map);
		datagrid.setRows(nl);
		return datagrid;
	}

	private String getOrderForPrint(int orderNum, String type){
		String[] strs = {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"};

		if(type.equals(Constant.TYPE_LEVEL1)){
			return strs[orderNum - 1];
		}
		if(type.equals(Constant.TYPE_LEVEL2)){
			return "（" + strs[orderNum - 1] + "）";
		}
		if(type.equals(Constant.TYPE_ENTRY)){
			return "" + orderNum + "、";
		}
		return "";
	}

	@Autowired
	public void setLevelDao(BaseDaoI<TLevel> levelDao) {
		this.levelDao = levelDao;
	}

	@Autowired
	public void setEntryDao(BaseDaoI<TEntry> entryDao) {
		this.entryDao = entryDao;
	}

	@Autowired
	public void setImgDao(BaseDaoI<TImg> imgDao) {
		this.imgDao = imgDao;
	}

	@Autowired
	public void setCatDao(BaseDaoI<TCat> catDao) {
		this.catDao = catDao;
	}

	@Autowired
	public void setOperalogDao(BaseDaoI<TOperalog> operalogDao) {
		this.operalogDao = operalogDao;
	}
}
