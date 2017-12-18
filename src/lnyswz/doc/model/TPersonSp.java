package lnyswz.doc.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_person_sp")
public class TPersonSp implements java.io.Serializable {
	private int id;
	private int personId;
	private String personName;
	private int createId;
	private String createName;
	private Date createTime;
	private String field;
	private String oldValue;
	private String newValue;
	private String needAudit;
	private String isAudit;
	private String status;
	private String timeStamp;
	private String bz;

	public TPersonSp(){

	}

	public TPersonSp(int id, int personId, String personName, int createId,	String createName, Date createTime, String field,
					 String oldValue, String newValue, String needAudit, String isAudit, String status, String timeStamp, String bz){
		this.id = id;
		this.personId = personId;
		this.personName = personName;
		this.createId = createId;
		this.createName = createName;
		this.createTime = createTime;
		this.field = field;
		this.oldValue = oldValue;
		this.newValue = newValue;
		this.needAudit = needAudit;
		this.isAudit = isAudit;
		this.status = status;
		this.timeStamp = timeStamp;
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

	@Column(name = "personName", nullable = false)
	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
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

	@Column(name = "field", nullable = true)
	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	@Column(name = "oldValue", nullable = true)
	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	@Column(name = "newValue", nullable = true)
	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
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

	@Column(name = "status", nullable = false)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
