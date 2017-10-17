package lnyswz.doc.util;

import lnyswz.doc.model.TPerson;

import java.util.Comparator;


/**
 * 按钮排序
 * 
 * @author 王文阳
 * 
 */
public class PersonComparator implements Comparator<TPerson> {

	public int compare(TPerson o1, TPerson o2) {
		return o1.getOrderNum() - o2.getOrderNum();
	}

}
