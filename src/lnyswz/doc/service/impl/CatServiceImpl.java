package lnyswz.doc.service.impl;

import lnyswz.common.bean.DataGrid;
import lnyswz.common.bean.TreeNode;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.doc.action.UploadAction;
import lnyswz.doc.bean.Entry;
import lnyswz.doc.bean.Cat;
import lnyswz.doc.model.TEntry;
import lnyswz.doc.model.TImg;
import lnyswz.doc.model.TCat;
import lnyswz.doc.model.TOperalog;
import lnyswz.doc.service.CatServiceI;
import lnyswz.doc.util.Constant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 分类实现表
 * @author 王文阳
 *
 */
@Service("catService")
public class CatServiceImpl implements CatServiceI {
	
	private BaseDaoI<TCat> catDao;

	@Override
	public List<Cat> getCats(Cat cat) {
		String hql = "from TCat t";
		
		List<TCat> tCats = catDao.find(hql);

		List<Cat> cats = new ArrayList<Cat>();
		Cat c = null;
		for (TCat tCat : tCats) {
			c = new Cat();
			BeanUtils.copyProperties(tCat, c);

			cats.add(c);
		}
		return cats;
	}
	
	@Autowired
	public void setCatDao(BaseDaoI<TCat> catDao) {
		this.catDao = catDao;
	}

}
