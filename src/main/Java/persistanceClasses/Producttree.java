package persistanceClasses;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Producttree generated by hbm2java
 */
@Entity
@Table(name = "producttree", catalog = "c39melidatos")
public class Producttree implements java.io.Serializable {

	private Long idRelationship;
	private String idSon;
	private String idFather;
	private String descFather;
	private String descSon;
	private Boolean isLeaf;
	private Date date;

	public Producttree() {
	}

	public Producttree(String idSon, String idFather, String descFather,
			String descSon, Date date) {
		this.idSon = idSon;
		this.idFather = idFather;
		this.descFather = descFather;
		this.descSon = descSon;
		this.date = date;
	}

	public Producttree(String idSon, String idFather, String descFather,
			String descSon, Boolean isLeaf, Date date) {
		this.idSon = idSon;
		this.idFather = idFather;
		this.descFather = descFather;
		this.descSon = descSon;
		this.isLeaf = isLeaf;
		this.date = date;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "idRelationship", unique = true, nullable = false)
	public Long getIdRelationship() {
		return this.idRelationship;
	}

	public void setIdRelationship(Long idRelationship) {
		this.idRelationship = idRelationship;
	}

	@Column(name = "idSon", nullable = false, length = 45)
	public String getIdSon() {
		return this.idSon;
	}

	public void setIdSon(String idSon) {
		this.idSon = idSon;
	}

	@Column(name = "idFather", nullable = false, length = 45)
	public String getIdFather() {
		return this.idFather;
	}

	public void setIdFather(String idFather) {
		this.idFather = idFather;
	}

	@Column(name = "descFather", nullable = false, length = 45)
	public String getDescFather() {
		return this.descFather;
	}

	public void setDescFather(String descFather) {
		this.descFather = descFather;
	}

	@Column(name = "descSon", nullable = false, length = 45)
	public String getDescSon() {
		return this.descSon;
	}

	public void setDescSon(String descSon) {
		this.descSon = descSon;
	}

	@Column(name = "isLeaf")
	public Boolean getIsLeaf() {
		return this.isLeaf;
	}

	public void setIsLeaf(Boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "date", nullable = false, length = 10)
	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
