package lnyswz.doc.service;

import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.TreeNode;
import lnyswz.doc.bean.Level;

import java.util.List;

public interface LevelServiceI {
	public void add(Level level);
	public void edit(Level level);
	public void delete(Level level);

	public List<Level> treegrid(Level level);
	public List<TreeNode> getLevelTree(Level level, Boolean b);

	public List<Level> allTopTree();

	public boolean checkDir(Level level);

	public DataGrid printLevel(Level level);

}
