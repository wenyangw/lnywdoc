package lnyswz.doc.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lnyswz.common.bean.DataGrid;
import lnyswz.common.dao.BaseDaoI;
import lnyswz.doc.bean.Post;
import lnyswz.doc.model.TPost; 
import lnyswz.doc.service.PostServiceI;

@Service("postService")
public class PostServiceImpl implements PostServiceI {
	private Logger logger = Logger.getLogger(PostServiceImpl.class);
	private BaseDaoI<TPost> postDao;

	/**
	 * 保存人员类型
	 */
	@Override
	public Post add(Post post) {
		TPost t = new TPost();
		BeanUtils.copyProperties(post, t);

		postDao.save(t);
		Post r = new Post();
		BeanUtils.copyProperties(t, r);
		return r;
	}

	/**
	 * 编辑人员类型
	 */
	@Override
	public void edit(Post post) {
		TPost g = postDao.get(TPost.class, post.getId());
		BeanUtils.copyProperties(post, g);

	}

	/**
	 * 删除人员类型
	 */
	@Override
	public void delete(Post post) {
		TPost t = postDao.get(TPost.class, post.getId());
		postDao.delete(t);
	}

	/**
	 * 不带分页的
	 */
	@Override
	public List<Post> listPosts(Post post) {
		String hql = "from TPost t ";
		List<TPost> list = postDao.find(hql);
		return changePost(list);
	}

	@Override
	public DataGrid datagrid(Post post) {
		DataGrid dg = new DataGrid();
		String hql = "from TPost t ";
		Map<String, Object> params = new HashMap<String, Object>();
		// h q l语句拼写

		// 获得总条数
		String totalHql = "select count(*) " + hql;
		// 传入页码、每页条数
		List<TPost> l = postDao.find(hql, params, post.getPage(), post
				.getRows());
		// 处理返回信息
		dg.setTotal(postDao.count(totalHql, params));
		dg.setRows(changePost(l));
		return dg;
	}

	private List<Post> changePost(List<TPost> l) {
		List<Post> nl = new ArrayList<Post>();
		for (TPost t : l) {
			Post nc = new Post();
			BeanUtils.copyProperties(t, nc);

			nl.add(nc);
		}
		return nl;
	}

	@Autowired
	public void setPostDao(BaseDaoI<TPost> postDao) {
		this.postDao = postDao;
	}

}
