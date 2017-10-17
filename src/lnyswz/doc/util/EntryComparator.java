package lnyswz.doc.util;

import lnyswz.doc.model.TEntry;

import java.util.Comparator;


/**
 * 按钮排序
 * 
 * @author 王文阳
 * 
 */
public class EntryComparator implements Comparator<TEntry> {

	public int compare(TEntry o1, TEntry o2) {
		return o1.getOrderNum() - o2.getOrderNum();
	}

}
