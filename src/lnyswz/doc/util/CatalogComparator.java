package lnyswz.doc.util;

import java.util.Comparator;

import lnyswz.doc.model.TCatalog;


/**
 * 模块排序
 * 
 * @author 王文阳
 * 
 */
public class CatalogComparator implements Comparator<TCatalog> {

	public int compare(TCatalog o1, TCatalog o2) {
		return o1.getOrderNum() - o2.getOrderNum();
	}

}
