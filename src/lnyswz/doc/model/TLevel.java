package lnyswz.doc.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "t_level")
public class TLevel implements java.io.Serializable {
	private int id;
	private String levelName;
	private int orderNum;
	private String bz;
	private String dir;
	private TCat TCat;
	private TLevel TLevel;
	private Set<TLevel> TLevels = new HashSet<TLevel>(0);
	private Set<TEntry> TEntrys = new HashSet<TEntry>(0);
	
	
	
	public TLevel(){
		
	}
	
	public TLevel(int id, String levelName,int orderNum,String bz,String dir, TCat TCat, TLevel TLevel, Set<TLevel> TLevels, Set<TEntry> TEntrys){
		this.id = id;
		this.levelName = levelName;
		this.orderNum = orderNum;
		this.bz = bz;
		this.dir = dir;
		this.TCat = TCat;
		this.TLevel = TLevel;
		this.TLevels = TLevels;
		this.TEntrys = TEntrys;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name = "levelName", unique = false, nullable = false)
	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	@Column(name = "orderNum", unique = false, nullable = true)
	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	@Column(name = "bz", unique = false, nullable = true)
	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	@Column(name = "dir", unique = false, nullable = true)
	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "catId")
	public TCat getTCat() {
		return TCat;
	}

	public void setTCat(TCat TCat) {
		this.TCat = TCat;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pid")
	public TLevel getTLevel() {
		return this.TLevel;
	}

	public void setTLevel(TLevel TLevel) {
		this.TLevel = TLevel;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "TLevel")
	public Set<TLevel> getTLevels() {
		return this.TLevels;
	}

	public void setTLevels(Set<TLevel> TLevels) {
		this.TLevels = TLevels;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "TLevel")
	public Set<TEntry> getTEntrys() {
		return TEntrys;
	}

	public void setTEntrys(Set<TEntry> TEntrys) {
		this.TEntrys = TEntrys;
	}
}
