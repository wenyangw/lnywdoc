package lnyswz.doc.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "t_person")
public class TPerson implements java.io.Serializable {
	private int id;
	private int orderNum;
	private String name;
	private String postName;
	private String ename;
	private String sex;
	private Date birthTime;
	private Date joinPartyTime;//入党时间
	private Date formalTime;//转正时间
	private String nation;//民族
	private String bz;
	/*************全日制教育******begin****/
	private Date fullEntranceTime;//入学时间
	private Date fullGraduationTime;//毕业时间
	private String fullSchool;//毕业院校
	private String fullMajor;//所学专业
	private String fullEducation;//学历
	private String fullDegree;//学位
	/**************************end******/
	/*************在职教育*******begin****/
	private String jobSchool;
	private String jobMajor;
	private String jobEducation;
	private String jobDegree;
	/**************************end******/	
	private String bestEducation;//最高学历
	private Date nowRankTime;//任现职级时间
	private String rankName;//职称
	private Date getRankTime;//取得职称时间	
	private Date jobTime;//参加工作时间
	private Date jtJobTime;//进入集团的工作时间
	private int outJobCount;//到公司前工作年数
	private Date companyTime;//到本单位工作时间
	private String phone;//手机号
	private String idCard;//身份证号码
	private String socialCard;//个人社保编号
	private Date socialPayTime;//社保缴费时间	
	private String medicalCard;//个人医保编号
	private String companyHouseBankCard;//公司公积金号
	private String houseBankCard;//个人公积金号
	private String imgPath;
	private String timeStamp;
	private String status;
	private String isAudit;

	private TDepartment TDepartment;

	public TPerson(){
		
	}
	
	public TPerson(int id, int orderNum,String name,String postName,String ename, String sex, Date birthTime, Date joinPartyTime,
			Date formalTime,String nation,String bz,Date fullEntranceTime, Date fullGraduationTime,String fullSchool,String fullMajor
			,String fullEducation,String fullDegree,String jobSchool,String jobMajor,String jobEducation,String jobDegree
			,Date nowRankTime,String rankName,Date getRankTime,Date jobTime,Date jtJobTime,int outJobCount,Date companyTime,
			String bestEducation,String phone, String idCard, String socialCard,Date socialPayTime,String medicalCard,
			String imgPath,String companyHouseBankCard,String houseBankCard, String timeStamp, String status, String isAudit, TDepartment TDepartment)
	{
		this.id=id;
		this.orderNum=orderNum;
		this.name=name;
		this.postName=postName;
		this.ename = ename;
		this.sex=sex;
		this.birthTime=birthTime;
		this.joinPartyTime=joinPartyTime;
		this.formalTime=formalTime;
		this.nation=nation;
		this.bz=bz;
		this.fullEntranceTime=fullEntranceTime;
		this.fullGraduationTime=fullGraduationTime;
		this.fullSchool=fullSchool;
		this.fullMajor=fullMajor;
		this.fullEducation=fullEducation;
		this.fullDegree=fullDegree;
		this.jobSchool=jobSchool;
		this.jobMajor=jobMajor;
		this.jobEducation=jobEducation;
		this.jobDegree=jobDegree;
		this.nowRankTime=nowRankTime;
		this.rankName=rankName;
		this.getRankTime=getRankTime;
		this.jobTime=jobTime;
		this.companyTime=companyTime;
		this.phone=phone;
		this.idCard=idCard;
		this.socialCard=socialCard;
		this.socialPayTime=socialPayTime;
		this.medicalCard=medicalCard;
		this.companyHouseBankCard=companyHouseBankCard;
		this.houseBankCard=houseBankCard;
		this.imgPath=imgPath;
		this.jtJobTime=jtJobTime;
		this.outJobCount=outJobCount;
		this.timeStamp = timeStamp;
		this.status = status;
		this.isAudit = isAudit;
		this.TDepartment=TDepartment;

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
	@Column(name = "orderNum", unique = false, nullable = false)
	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	@Column(name = "name", unique = false, nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "postName",  nullable = true)
	public String getPostName() {
		return postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}


	@Column(name = "ename", unique = true, nullable = false)
	public String getEname() {
		return ename;
	}

	public void setEname(String ename) {
		this.ename = ename;
	}

	@Column(name = "sex", unique = false, nullable = false)
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "birthTime", nullable = true)
	public Date getBirthTime() {
		return birthTime;
	}

	public void setBirthTime(Date birthTime) {
		this.birthTime = birthTime;
	}

//	@Temporal(TemporalType.TIMESTAMP)
	@Temporal(TemporalType.DATE)
	@Column(name = "joinPartyTime", nullable = true, length = 23)
	public Date getJoinPartyTime() {
		return joinPartyTime;
	}

	public void setJoinPartyTime(Date joinPartyTime) {
		this.joinPartyTime = joinPartyTime;
	}


	@Temporal(TemporalType.DATE)
	@Column(name = "formalTime", nullable = true, length = 23)
	public Date getFormalTime() {
		return formalTime;
	}

	public void setFormalTime(Date formalTime) {
		this.formalTime = formalTime;
	}

	@Column(name = "nation", unique = false, nullable = true)
	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}
	
	
	@Column(name = "bz", unique = false, nullable = true)
	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "fullEntranceTime", nullable = true, length = 23)
	public Date getFullEntranceTime() {
		return fullEntranceTime;
	}

	public void setFullEntranceTime(Date fullEntranceTime) {
		this.fullEntranceTime = fullEntranceTime;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "fullGraduationTime", nullable = true, length = 23)
	public Date getFullGraduationTime() {
		return fullGraduationTime;
	}

	public void setFullGraduationTime(Date fullGraduationTime) {
		this.fullGraduationTime = fullGraduationTime;
	}

	@Column(name = "fullSchool", unique = false, nullable = true)
	public String getFullSchool() {
		return fullSchool;
	}

	public void setFullSchool(String fullSchool) {
		this.fullSchool = fullSchool;
	}

	@Column(name = "fullMajor", unique = false, nullable = true)
	public String getFullMajor() {
		return fullMajor;
	}

	public void setFullMajor(String fullMajor) {
		this.fullMajor = fullMajor;
	}

	@Column(name = "fullEducation", unique = false, nullable = true)
	public String getFullEducation() {
		return fullEducation;
	}

	public void setFullEducation(String fullEducation) {
		this.fullEducation = fullEducation;
	}

	@Column(name = "fullDegree", unique = false, nullable = true)
	public String getFullDegree() {
		return fullDegree;
	}

	public void setFullDegree(String fullDegree) {
		this.fullDegree = fullDegree;
	}

	@Column(name = "jobSchool", unique = false, nullable = true)
	public String getJobSchool() {
		return jobSchool;
	}

	public void setJobSchool(String jobSchool) {
		this.jobSchool = jobSchool;
	}

	@Column(name = "jobMajor", unique = false, nullable = true)
	public String getJobMajor() {
		return jobMajor;
	}

	public void setJobMajor(String jobMajor) {
		this.jobMajor = jobMajor;
	}

	@Column(name = "jobEducation", unique = false, nullable = true)
	public String getJobEducation() {
		return jobEducation;
	}

	public void setJobEducation(String jobEducation) {
		this.jobEducation = jobEducation;
	}

	@Column(name = "jobDegree", unique = false, nullable = true)
	public String getJobDegree() {
		return jobDegree;
	}

	public void setJobDegree(String jobDegree) {
		this.jobDegree = jobDegree;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "nowRankTime", nullable = true, length = 23)
	public Date getNowRankTime() {
		return nowRankTime;
	}

	public void setNowRankTime(Date nowRankTime) {
		this.nowRankTime = nowRankTime;
	}

	@Column(name = "rankName", unique = false, nullable = true)
	public String getRankName() {
		return rankName;
	}

	public void setRankName(String rankName) {
		this.rankName = rankName;
	}


	@Temporal(TemporalType.DATE)
	@Column(name = "getRankTime", nullable = true, length = 23)
	public Date getGetRankTime() {
		return getRankTime;
	}

	public void setGetRankTime(Date getRankTime) {
		this.getRankTime = getRankTime;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "jtJobTime", nullable = true, length = 23)
	public Date getJtJobTime() {
		return jtJobTime;
	}

	public void setJtJobTime(Date jtJobTime) {
		this.jtJobTime = jtJobTime;
	}

	@Column(name = "outJobCount", unique = false, nullable = true)
	public int getOutJobCount() {
		return outJobCount;
	}

	public void setOutJobCount(int outJobCount) {
		this.outJobCount = outJobCount;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "jobTime", nullable = true, length = 23)
	public Date getJobTime() {
		return jobTime;
	}

	public void setJobTime(Date jobTime) {
		this.jobTime = jobTime;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "companyTime", nullable = true, length = 23)
	public Date getCompanyTime() {
		return companyTime;
	}

	public void setCompanyTime(Date companyTime) {
		this.companyTime = companyTime;
	}

	@Column(name = "bestEducation", unique = false, nullable = true)
	public String getBestEducation() {
		return bestEducation;
	}

	public void setBestEducation(String bestEducation) {
		this.bestEducation = bestEducation;
	}

	@Column(name = "phone", unique = false, nullable = true)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	
	@Column(name = "idCard", unique = false, nullable = true)
	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	@Column(name = "socialCard", unique = false, nullable = true)
	public String getSocialCard() {
		return socialCard;
	}

	public void setSocialCard(String socialCard) {
		this.socialCard = socialCard;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "socialPayTime", nullable = true, length = 23)
	public Date getSocialPayTime() {
		return socialPayTime;
	}

	public void setSocialPayTime(Date socialPayTime) {
		this.socialPayTime = socialPayTime;
	}

	@Column(name = "medicalCard", unique = false, nullable = true)
	public String getMedicalCard() {
		return medicalCard;
	}

	public void setMedicalCard(String medicalCard) {
		this.medicalCard = medicalCard;
	}

	@Column(name = "houseBankCard", unique = false, nullable = true)
	public String getHouseBankCard() {
		return houseBankCard;
	}

	public void setHouseBankCard(String houseBankCard) {
		this.houseBankCard = houseBankCard;
	}
	
	@Column(name = "companyHouseBankCard", unique = false, nullable = true)
	public String getCompanyHouseBankCard() {
		return companyHouseBankCard;
	}
	
	public void setCompanyHouseBankCard(String companyHouseBankCard) {
		this.companyHouseBankCard = companyHouseBankCard;
	}

	@Column(name = "imgPath", unique = false, nullable = true)
	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	@Column(name = "timeStamp", nullable = true)
	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	@Column(name = "status", nullable = false)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "isAudit", nullable = false)
	public String getIsAudit() {
		return isAudit;
	}

	public void setIsAudit(String isAudit) {
		this.isAudit = isAudit;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "bmbh", nullable = true)
	public TDepartment getTDepartment() {
		return TDepartment;
	}

	public void setTDepartment(TDepartment tDepartment) {
		TDepartment = tDepartment;
	}

}
