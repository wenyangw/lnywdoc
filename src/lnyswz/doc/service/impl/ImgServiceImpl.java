package lnyswz.doc.service.impl;

import lnyswz.common.dao.BaseDaoI;
import lnyswz.doc.action.UploadAction;
import lnyswz.doc.bean.Img;
import lnyswz.doc.bean.Entry;
import lnyswz.doc.model.*;
import lnyswz.doc.service.ImgServiceI;
import lnyswz.doc.util.Constant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 图片实现表
 * @author 王文阳
 *
 */
@Service("imgService")
public class ImgServiceImpl implements ImgServiceI {
	
	private BaseDaoI<TImg> imgDao;
	private BaseDaoI<TEntry> entryDao;
	private BaseDaoI<TOperalog> operalogDao;

	/**
	 * 增加图片项
	 */
	@Override
	public Img add(Img img) {
		TImg t = new TImg();
		BeanUtils.copyProperties(img, t);

		if(Constant.CATID_PERSON.equals(img.getCatId())) {
			//t.setOrderNum(getOrderNum(img.getEntryId() - Constant.ENTRY_ID_PLUS) + 1);
			t.setOrderNum(getOrderNum(img.getEntryId()) + 1);
			//t.setTEntry(entryDao.load(TEntry.class, img.getEntryId() - Constant.ENTRY_ID_PLUS));
			t.setTEntry(entryDao.load(TEntry.class, img.getEntryId()));
		}else{
			t.setOrderNum(getOrderNum(img.getEntryId()) + 1);
			t.setTEntry(entryDao.load(TEntry.class, img.getEntryId()));
		}

		imgDao.save(t);

		BeanUtils.copyProperties(t, img);

		OperalogServiceImpl.addOperalog(img.getCreateId(), "", "", "" + t.getId(),
				"保存图片", operalogDao);
		return img;
	}
	
	/**
	 * 修改图片
	 */
	@Override
	public void edit(Img img) {
		TImg t = imgDao.get(TImg.class, img.getId());

		t.setBz(img.getBz());
		t.setCrux(img.getCrux());
		t.setOrderNum(img.getOrderNum());

		OperalogServiceImpl.addOperalog(img.getCreateId(), "", "", "" + t.getId(),
				"修改图片", operalogDao);
	}
	
	/**
	 * 删除图片项
	 */
	@Override
	public void delete(Img img) {
        TImg tImg = imgDao.get(TImg.class, img.getId());

        UploadAction.deleteFile("/" + tImg.getFilePath());

		imgDao.delete(tImg);

		OperalogServiceImpl.addOperalog(img.getCreateId(), "", "", "" + img.getId(),
				"删除图片", operalogDao);
	}

	@Override
	public Img getImg(Img img){
		TImg tImg = imgDao.load(TImg.class, img.getId());
		BeanUtils.copyProperties(tImg, img);

		return img;
	}
	@Override
	public List<Img> getImgsByEntry(Img img){
		List<Img> imgs = null;
		String hql = "from TImg t where t.TEntry.id = :entryId order by orderNum";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("entryId", img.getEntryId());

		List<TImg> l = imgDao.find(hql, params);
		if (l != null && l.size() > 0){
			imgs = new ArrayList<Img>();
			Img i = null;
			for (TImg tImg : l) {
				i = new Img();
				BeanUtils.copyProperties(tImg, i);
				imgs.add(i);
			}
		}

		return imgs;
	}

	public static long getImgCount(int entryId, BaseDaoI baseDaoI){
		String hql = "select count(id) from TImg t where t.TEntry.id = :entryId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("entryId", entryId);

		return baseDaoI.count(hql, params);
	}

	private int getOrderNum(int entryId){
		String sql = "select max(orderNum) from t_img t where t.entryId = ?";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("0", entryId);
		if(entryDao.getBySQL(sql, params) != null){
			return Integer.parseInt(entryDao.getBySQL(sql, params).toString());
		}else{
			return 0;
		}
	}

	@Autowired
	public void setImgDao(BaseDaoI<TImg> imgDao) {
		this.imgDao = imgDao;
	}

	@Autowired
	public void setEntryDao(BaseDaoI<TEntry> entryDao) {
		this.entryDao = entryDao;
	}

	@Autowired
	public void setOperalogDao(BaseDaoI<TOperalog> operalogDao) {
		this.operalogDao = operalogDao;
	}
}
