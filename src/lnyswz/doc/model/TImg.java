package lnyswz.doc.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_img")
public class TImg implements java.io.Serializable {
	private int id;
	private String filePath;
	private int orderNum;
	private String crux;
	private String bz;
	private TEntry TEntry;

	public TImg(){

	}

	public TImg(int id, String filePath, int orderNum, String crux, String bz, TEntry TEntry){
		this.id = id;
		this.filePath = filePath;
		this.orderNum = orderNum;
		this.crux = crux;
		this.bz = bz;
		this.TEntry = TEntry;
	}
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name = "filePath", unique = false, nullable = false)
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Column(name = "orderNum", unique = false, nullable = true)
	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	@Column(name = "crux", unique = false, nullable = true)
	public String getCrux() {
		return crux;
	}

	public void setCrux(String crux) {
		this.crux = crux;
	}

	@Column(name = "bz", unique = false, nullable = true)
	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "entryId")
	public lnyswz.doc.model.TEntry getTEntry() {
		return TEntry;
	}

	public void setTEntry(lnyswz.doc.model.TEntry TEntry) {
		this.TEntry = TEntry;
	}

}
