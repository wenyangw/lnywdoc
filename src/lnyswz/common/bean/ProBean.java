package lnyswz.common.bean;

import java.math.BigDecimal;

public class ProBean {

	private String group;
	private String name;
	private String value = "";
	private BigDecimal xscb; 
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public BigDecimal getXscb() {
		return xscb;
	}
	public void setXscb(BigDecimal xscb) {
		this.xscb = xscb;
	}
	
}
