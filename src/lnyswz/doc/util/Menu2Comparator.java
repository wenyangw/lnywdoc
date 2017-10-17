package lnyswz.doc.util;

import java.util.Comparator;

import lnyswz.doc.bean.Menu;

/**
 * 菜单排序
 * 
 * @author 王文阳
 * 
 */
public class Menu2Comparator implements Comparator<Menu> {

	public int compare(Menu o1, Menu o2) {
		int i = 0;
		//均为一级菜单
		if(o1.getPid() == null && o2.getPid() == null){
			i =  o1.getOrderNum() - o2.getOrderNum();
		}
		
		//一级菜单在前，二级菜单在后
		if(o1.getPid() == null && o2.getPid() != null){
			i = -1;
		}
		
		//一级菜单在前，二级菜单在后
		if(o1.getPid() != null && o2.getPid() == null){
			i = 1;
		}
		//均为二级菜单
		if(o1.getPid() != null && o2.getPid() != null){
			if(o1.getText().compareTo(o2.getText()) < 0){
				i = -1;
			}else if(o1.getText().compareTo(o2.getText()) > 0){
				i = 1;
			}else{
				i = o1.getOrderNum() - o2.getOrderNum();
			}
		}
		return i;
	}

}
