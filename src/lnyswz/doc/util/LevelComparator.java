package lnyswz.doc.util;

import lnyswz.doc.model.TLevel;

import java.util.Comparator;


/**
 * 类别排序
 * 
 * @author 王文阳
 * 
 */
public class LevelComparator implements Comparator<TLevel> {

	public int compare(TLevel o1, TLevel o2) {
		int i = 0;
		//均为一级类别
		if(o1.getTLevel() == null && o2.getTLevel() == null){
			i =  o1.getOrderNum() - o2.getOrderNum();
		}
		
		//一级类别在前，二级类别在后
		if(o1.getTLevel() == null && o2.getTLevel() != null){
			i = -1;
		}
		
		//一级类别在前，二级类别在后
		if(o1.getTLevel() != null && o2.getTLevel() == null){
			i = 1;
		}
		//均为二级类别
		if(o1.getTLevel() != null && o2.getTLevel() != null){
			i = o1.getOrderNum() - o2.getOrderNum();
		}
		return i;
	}

}
