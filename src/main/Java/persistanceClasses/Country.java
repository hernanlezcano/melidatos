package persistanceClasses;
// Generated 04-oct-2015 21:23:47 by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Country generated by hbm2java
 */
@Entity
@Table(name = "country", catalog = "c39melidatos")
public class Country implements java.io.Serializable {

	private Integer idcountry;
	private String descCountry;
	private Set states = new HashSet(0);

	public Country() {
	}

	public Country(String descCountry) {
		this.descCountry = descCountry;
	}

	public Country(String descCountry, Set states) {
		this.descCountry = descCountry;
		this.states = states;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "idcountry", unique = true, nullable = false)
	public Integer getIdcountry() {
		return this.idcountry;
	}

	public void setIdcountry(Integer idcountry) {
		this.idcountry = idcountry;
	}

	@Column(name = "descCountry", nullable = false, length = 45)
	public String getDescCountry() {
		return this.descCountry;
	}

	public void setDescCountry(String descCountry) {
		this.descCountry = descCountry;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "country")
	public Set getStates() {
		return this.states;
	}

	public void setStates(Set states) {
		this.states = states;
	}

}