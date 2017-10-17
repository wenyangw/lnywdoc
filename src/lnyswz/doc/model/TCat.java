package lnyswz.doc.model;


import javax.persistence.*;

@Entity
@Table(name = "t_cat")
public class TCat implements java.io.Serializable {
	private String id;
	private String catName;

	public TCat(){

	}

	public TCat(String id, String catName){
		this.id = id;
		this.catName = catName;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false, length = 2)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name = "catName", unique = false, nullable = false)
	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

}
