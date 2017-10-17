package lnyswz.doc.util;

import java.util.Comparator;

import lnyswz.doc.model.TButton;
import lnyswz.doc.model.TUser;


/**
 * 按钮排序
 * 
 * @author 王文阳
 * 
 */
public class UserComparator implements Comparator<TUser> {

	public int compare(TUser o1, TUser o2) {
		return o1.getOrderNum() - o2.getOrderNum();
	}

}
