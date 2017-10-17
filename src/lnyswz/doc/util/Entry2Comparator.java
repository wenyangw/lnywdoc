package lnyswz.doc.util;

import lnyswz.doc.bean.Entry;

import java.util.Comparator;


/**
 * 按钮排序
 * 
 * @author 王文阳
 * 
 */
public class Entry2Comparator implements Comparator<Entry> {

	public int compare(Entry o1, Entry o2) {
		return o1.getOrderNum() - o2.getOrderNum();
	}

}
