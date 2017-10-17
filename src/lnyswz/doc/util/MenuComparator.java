package lnyswz.doc.util;

import java.util.Comparator;

import lnyswz.doc.model.TMenu;


/**
 * 菜单排序
 * 
 * @author 王文阳
 * 
 */
public class MenuComparator implements Comparator<TMenu> {

	public int compare(TMenu o1, TMenu o2) {
		int i = 0;
		//均为一级菜单
		if(o1.getTMenu() == null && o2.getTMenu() == null){
			i =  o1.getOrderNum() - o2.getOrderNum();
		}
		
		//一级菜单在前，二级菜单在后
		if(o1.getTMenu() == null && o2.getTMenu() != null){
			i = -1;
		}
		
		//一级菜单在前，二级菜单在后
		if(o1.getTMenu() != null && o2.getTMenu() == null){
			i = 1;
		}
		//均为二级菜单
		if(o1.getTMenu() != null && o2.getTMenu() != null){
			i = o1.getOrderNum() - o2.getOrderNum();
		}
		return i;
	}

}
