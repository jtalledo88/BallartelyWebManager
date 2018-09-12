package pe.com.foxsoft.ballartelyweb.jpa.data;

import java.io.Serializable;
import javax.persistence.*;

import org.springframework.context.annotation.Scope;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;


/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@Table(name="enterprise")
@EntityListeners(AuditingEntityListener.class)
@Scope("session")
public class Enterprise implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;

	@Column(name="social_reason")
	private String socialReason;
	
	@Column(name="ruc")
	private String ruc;
	
	@Column(name="dni")
	private String dni;
	
	@Column(name="address")
	private String address;
	
	@Column(name="district")
	private String district;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="enterprise_creation_date")
	@CreatedDate
	private Date enterpriseCreationDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="enterprise_modification_date")
	@LastModifiedDate
	private Date enterpriseModificationDate;

	@Column(name="login")
	private String login;

	@Column(name="password")
	private String password;

	public Enterprise() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSocialReason() {
		return socialReason;
	}

	public void setSocialReason(String socialReason) {
		this.socialReason = socialReason;
	}

	public String getRuc() {
		return ruc;
	}

	public void setRuc(String ruc) {
		this.ruc = ruc;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public Date getEnterpriseCreationDate() {
		return enterpriseCreationDate;
	}

	public void setEnterpriseCreationDate(Date enterpriseCreationDate) {
		this.enterpriseCreationDate = enterpriseCreationDate;
	}

	public Date getEnterpriseModificationDate() {
		return enterpriseModificationDate;
	}

	public void setEnterpriseModificationDate(Date enterpriseModificationDate) {
		this.enterpriseModificationDate = enterpriseModificationDate;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}