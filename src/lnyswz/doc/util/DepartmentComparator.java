package lnyswz.doc.util;

import java.util.Comparator;

import lnyswz.doc.model.TDepartment;


/**
 * 按钮排序
 * 
 * @author 王文阳
 * 
 */
public class DepartmentComparator implements Comparator<TDepartment> {

	public int compare(TDepartment o1, TDepartment o2) {
		return o1.getOrderNum() - o2.getOrderNum();
	}

}
