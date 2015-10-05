package persistanceClasses;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * State generated by hbm2java
 */
@Entity
@Table(name = "state", catalog = "c39melidatos")
public class State implements java.io.Serializable {

	private String idState;
	private Country country;
	private String descState;
	private Set statesdatas = new HashSet(0);

	public State() {
	}

	public State(String idState, String descState) {
		this.idState = idState;
		this.descState = descState;
	}

	public State(String idState, Country country, String descState,
			Set statesdatas) {
		this.idState = idState;
		this.country = country;
		this.descState = descState;
		this.statesdatas = statesdatas;
	}

	@Id
	@Column(name = "idState", unique = true, nullable = false, length = 45)
	public String getIdState() {
		return this.idState;
	}

	public void setIdState(String idState) {
		this.idState = idState;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idCountry")
	public Country getCountry() {
		return this.country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	@Column(name = "descState", nullable = false, length = 45)
	public String getDescState() {
		return this.descState;
	}

	public void setDescState(String descState) {
		this.descState = descState;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "state")
	public Set getStatesdatas() {
		return this.statesdatas;
	}

	public void setStatesdatas(Set statesdatas) {
		this.statesdatas = statesdatas;
	}

}
