package lnyswz.doc.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_czsh")
public class TCzsh implements java.io.Serializable {
	private int id;
	private int personId;
	private String timeStamp;
	private String bmbh;
	private int createId;
	private String createName;
	private Date createTime;
	private String needAudit;
	private String isAudit;
	private String bz;

	public TCzsh(){

	}

	public TCzsh(int id, int personId, String timeStamp, String bmbh, int createId, String createName, Date createTime,
                 String needAudit, String isAudit, String bz){
		this.id = id;
		this.personId = personId;
		this.timeStamp = timeStamp;
		this.bmbh = bmbh;
		this.createId = createId;
		this.createName = createName;
		this.createTime = createTime;
		this.needAudit = needAudit;
		this.isAudit = isAudit;
		this.bz = bz;
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

	@Column(name = "personId", nullable = false)
	public int getPersonId() {
		return personId;
	}

	public void setPersonId(int personId) {
		this.personId = personId;
	}

	@Column(name = "bmbh", nullable = false)
	public String getBmbh() {
		return bmbh;
	}

	public void setBmbh(String bmbh) {
		this.bmbh = bmbh;
	}

	@Column(name = "createId", nullable = false)
	public int getCreateId() {
		return createId;
	}

	public void setCreateId(int createId) {
		this.createId = createId;
	}

	@Column(name = "createName", nullable = false)
	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "createTime", nullable = false, length = 23)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "needAudit", nullable = false)
	public String getNeedAudit() {
		return needAudit;
	}

	public void setNeedAudit(String needAudit) {
		this.needAudit = needAudit;
	}

	@Column(name = "isAudit", nullable = false)
	public String getIsAudit() {
		return isAudit;
	}

	public void setIsAudit(String isAudit) {
		this.isAudit = isAudit;
	}

	@Column(name = "timeStamp", nullable = false)
	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	@Column(name = "bz", nullable = true)
	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}
}
