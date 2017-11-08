package lnyswz.doc.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_entry")
public class TEntry implements java.io.Serializable {
	private int id;
	private String entryName;
	private Date createTime;
	private Date recordTime;
	private int orderNum;
	private String dir;
	private String bz;
	private String crux;
	private String owner;
	private String fileno;
	private String volume;
//	private String sort;
	private TCat TCat;
	private TLevel TLevel;
	private TPerson TPerson;

	public TEntry(){

	}

	public TEntry(int id, String entryName, Date createTime, Date recordTime, int orderNum, String dir, String bz,
			String crux, String owner, String fileno, String volume, TCat TCat, TLevel TLevel, TPerson TPerson){
		this.id = id;
		this.entryName = entryName;
		this.createTime = createTime;
		this.recordTime = recordTime;
		this.orderNum = orderNum;
		this.dir = dir;
		this.bz = bz;
		this.crux = crux;
		this.owner = owner;
		this.fileno = fileno;
		this.volume = volume;
		this.TCat = TCat;
		this.TLevel = TLevel;
		this.TPerson = TPerson;
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
	
	@Column(name = "entryName", unique = false, nullable = false)
	public String getEntryName() {
		return entryName;
	}

	public void setEntryName(String entryName) {
		this.entryName = entryName;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "createTime", nullable = false, length = 23)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "recordTime", nullable = false, length = 23)
	public Date getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}

	@Column(name = "orderNum", unique = false, nullable = true)
	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	@Column(name = "dir", unique = false, nullable = true)
	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	@Column(name = "bz", unique = false, nullable = true)
	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	@Column(name = "crux", unique = false, nullable = true)
	public String getCrux() {
		return crux;
	}

	public void setCrux(String crux) {
		this.crux = crux;
	}

	@Column(name = "owner", unique = false, nullable = true)
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	@Column(name = "fileno", unique = false, nullable = true)
	public String getFileno() {
		return fileno;
	}

	public void setFileno(String fileno) {
		this.fileno = fileno;
	}

	@Column(name = "volume", unique = false, nullable = true)
	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

//	@Column(name = "sort", unique = false, nullable = true)
//	public String getSort() {
//		return sort;
//	}
//
//	public void setSort(String sort) {
//		this.sort = sort;
//	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "catId")
	public TCat getTCat() {
		return TCat;
	}

	public void setTCat(TCat TCat) {
		this.TCat = TCat;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "levelId")
	public lnyswz.doc.model.TLevel getTLevel() {
		return TLevel;
	}

	public void setTLevel(lnyswz.doc.model.TLevel TLevel) {
		this.TLevel = TLevel;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "personId")
	public lnyswz.doc.model.TPerson getTPerson() {
		return TPerson;
	}

	public void setTPerson(lnyswz.doc.model.TPerson TPerson) {
		this.TPerson = TPerson;
	}
}
