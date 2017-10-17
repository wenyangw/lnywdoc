package lnyswz.doc.util;

import java.util.Comparator;

import lnyswz.doc.model.TButton;


/**
 * 按钮排序
 * 
 * @author 王文阳
 * 
 */
public class ButtonComparator implements Comparator<TButton> {

	public int compare(TButton o1, TButton o2) {
		return o1.getOrderNum() - o2.getOrderNum();
	}

}
