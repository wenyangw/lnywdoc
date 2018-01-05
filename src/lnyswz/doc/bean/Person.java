package lnyswz.doc.bean;

import java.util.Date;

public class Person {
	private int id;
	private int orderNum;
	private String postName;//岗位名称
	private String name;
	private String ename;
	private String sex;
	private Date birthTime;
	private Date joinPartyTime;//入党时间
	private Date formalTime;//转正时间
	private int formalPartyCount;//党龄
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
	private Date companyTime;//到本单位工作时间
	private Date nowRankTime;//任现职级时间
	private String rankName;//职称
	private Date getRankTime;//取得职称时间
	private Date jobTime;//参加工作时间
	private Date jtJobTime;//进入集团的工作时间
	private int outJobCount;//无就业年限
	private String bestEducation;//最高学历
	private String phone;//手机号
	private String idCard;//身份证号码
	private String socialCard;//个人社保编号
	private Date socialPayTime;//社保缴费时间
	private String medicalCard;//个人医保编号
	private String companyHouseBankCard;//单位公积金号
	private String houseBankCard;//个人公积金号
	private String imgPath;
	private String timeStamp;
	private String status;
	private String isAudit;
	private String bmbh;

	
	/*****非数据库中字段***/
	private int workingYears;//工龄
	private int jtWorkingYears;//到集团工作工龄
	private int companyYears;
	private String bmmc;

	private int createId;
	private String createName;
	private String param;
	private String search;
	private String needAudit;
	private String personSpCond;
	private String personCond;
	private String noAuditField;
	
	
	private int page;
	private int rows;

	/*********do()-get()方法***********************/
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEname() {
		return ename;
	}

	public void setEname(String ename) {
		this.ename = ename;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Date getBirthTime() {
		return birthTime;
	}

	public void setBirthTime(Date birthTime) {
		this.birthTime = birthTime;
	}

	public Date getJoinPartyTime() {
		return joinPartyTime;
	}

	public void setJoinPartyTime(Date joinPartyTime) {
		this.joinPartyTime = joinPartyTime;
	}

	public Date getFormalTime() {
		return formalTime;
	}

	public void setFormalTime(Date formalTime) {
		this.formalTime = formalTime;
	}

	
	public int getFormalPartyCount() {
		return formalPartyCount;
	}

	public void setFormalPartyCount(int formalPartyCount) {
		this.formalPartyCount = formalPartyCount;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	
	
	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public Date getFullEntranceTime() {
		return fullEntranceTime;
	}

	public void setFullEntranceTime(Date fullEntranceTime) {
		this.fullEntranceTime = fullEntranceTime;
	}

	public Date getFullGraduationTime() {
		return fullGraduationTime;
	}

	public void setFullGraduationTime(Date fullGraduationTime) {
		this.fullGraduationTime = fullGraduationTime;
	}

	public String getFullSchool() {
		return fullSchool;
	}

	public void setFullSchool(String fullSchool) {
		this.fullSchool = fullSchool;
	}

	public String getFullMajor() {
		return fullMajor;
	}

	public void setFullMajor(String fullMajor) {
		this.fullMajor = fullMajor;
	}

	public String getFullEducation() {
		return fullEducation;
	}

	public void setFullEducation(String fullEducation) {
		this.fullEducation = fullEducation;
	}

	public String getFullDegree() {
		return fullDegree;
	}

	public void setFullDegree(String fullDegree) {
		this.fullDegree = fullDegree;
	}

	public String getJobSchool() {
		return jobSchool;
	}

	public void setJobSchool(String jobSchool) {
		this.jobSchool = jobSchool;
	}

	public String getJobMajor() {
		return jobMajor;
	}

	public void setJobMajor(String jobMajor) {
		this.jobMajor = jobMajor;
	}

	public String getJobEducation() {
		return jobEducation;
	}

	public void setJobEducation(String jobEducation) {
		this.jobEducation = jobEducation;
	}

	public String getJobDegree() {
		return jobDegree;
	}

	public void setJobDegree(String jobDegree) {
		this.jobDegree = jobDegree;
	}

	public Date getNowRankTime() {
		return nowRankTime;
	}

	public void setNowRankTime(Date nowRankTime) {
		this.nowRankTime = nowRankTime;
	}

	public String getRankName() {
		return rankName;
	}

	public void setRankName(String rankName) {
		this.rankName = rankName;
	}

	public Date getGetRankTime() {
		return getRankTime;
	}

	public void setGetRankTime(Date getRankTime) {
		this.getRankTime = getRankTime;
	}

	public Date getJobTime() {
		return jobTime;
	}

	public void setJobTime(Date jobTime) {
		this.jobTime = jobTime;
	}

	public Date getCompanyTime() {
		return companyTime;
	}

	public void setCompanyTime(Date companyTime) {
		this.companyTime = companyTime;
	}

	public String getBestEducation() {
		return bestEducation;
	}

	public void setBestEducation(String bestEducation) {
		this.bestEducation = bestEducation;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getWorkingYears() {
		return workingYears;
	}

	public void setWorkingYears(int workingYears) {
		this.workingYears = workingYears;
	}

	
	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getSocialCard() {
		return socialCard;
	}

	public void setSocialCard(String socialCard) {
		this.socialCard = socialCard;
	}

	public Date getSocialPayTime() {
		return socialPayTime;
	}

	public void setSocialPayTime(Date socialPayTime) {
		this.socialPayTime = socialPayTime;
	}

	public String getMedicalCard() {
		return medicalCard;
	}

	public void setMedicalCard(String medicalCard) {
		this.medicalCard = medicalCard;
	}

	
	
	public String getCompanyHouseBankCard() {
		return companyHouseBankCard;
	}

	public void setCompanyHouseBankCard(String companyHouseBankCard) {
		this.companyHouseBankCard = companyHouseBankCard;
	}

	public String getHouseBankCard() {
		return houseBankCard;
	}

	public void setHouseBankCard(String houseBankCard) {
		this.houseBankCard = houseBankCard;
	}
	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsAudit() {
		return isAudit;
	}

	public void setIsAudit(String isAudit) {
		this.isAudit = isAudit;
	}

	public String getBmbh() {
		return bmbh;
	}

	public void setBmbh(String bmbh) {
		this.bmbh = bmbh;
	}

	public String getPostName() {
		return postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}

	public String getBmmc() {
		return bmmc;
	}

	public void setBmmc(String bmmc) {
		this.bmmc = bmmc;
	}

	public int getCreateId() {
		return createId;
	}

	public void setCreateId(int createId) {
		this.createId = createId;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}
	
	public Date getJtJobTime() {
		return jtJobTime;
	}

	public void setJtJobTime(Date jtJobTime) {
		this.jtJobTime = jtJobTime;
	}

	public int getOutJobCount() {
		return outJobCount;
	}

	public void setOutJobCount(int outJobCount) {
		this.outJobCount = outJobCount;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public int getJtWorkingYears() {
		return jtWorkingYears;
	}

	public void setJtWorkingYears(int jtWorkingYears) {
		this.jtWorkingYears = jtWorkingYears;
	}

	public int getCompanyYears() {
		return companyYears;
	}

	public void setCompanyYears(int companyYears) {
		this.companyYears = companyYears;
	}

	public String getNeedAudit() {
		return needAudit;
	}

	public void setNeedAudit(String needAudit) {
		this.needAudit = needAudit;
	}

	public String getPersonSpCond() {
		return personSpCond;
	}

	public void setPersonSpCond(String personSpCond) {
		this.personSpCond = personSpCond;
	}

	public String getPersonCond() {
		return personCond;
	}

	public void setPersonCond(String personCond) {
		this.personCond = personCond;
	}

	public String getNoAuditField() {
		return noAuditField;
	}

	public void setNoAuditField(String noAuditField) {
		this.noAuditField = noAuditField;
	}


}
