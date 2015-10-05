package persistanceClasses;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Users generated by hbm2java
 */
@Entity
@Table(name = "users", catalog = "c39melidatos", uniqueConstraints = @UniqueConstraint(columnNames = "idMail"))
public class Users implements java.io.Serializable {

	private Long oid;
	private String idMail;
	private String name;
	private String lastName;
	private String password;
	private boolean isAdmin;

	public Users() {
	}

	public Users(String idMail, String name, String lastName, String password,
			boolean isAdmin) {
		this.idMail = idMail;
		this.name = name;
		this.lastName = lastName;
		this.password = password;
		this.isAdmin = isAdmin;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "oid", unique = true, nullable = false)
	public Long getOid() {
		return this.oid;
	}

	public void setOid(Long oid) {
		this.oid = oid;
	}

	@Column(name = "idMail", unique = true, nullable = false, length = 45)
	public String getIdMail() {
		return this.idMail;
	}

	public void setIdMail(String idMail) {
		this.idMail = idMail;
	}

	@Column(name = "name", nullable = false, length = 45)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "lastName", nullable = false, length = 45)
	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name = "password", nullable = false, length = 45)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "isAdmin", nullable = false)
	public boolean isIsAdmin() {
		return this.isAdmin;
	}

	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

}
