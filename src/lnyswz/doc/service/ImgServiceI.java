package lnyswz.doc.service;

import lnyswz.doc.bean.Img;

import java.util.List;

public interface ImgServiceI {
	public Img add(Img img);
	public void edit(Img img);
	public void delete(Img img);
	public Img getImg(Img img);
	public List<Img> getImgsByEntry(Img img);
}
