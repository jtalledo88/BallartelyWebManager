package pe.com.foxsoft.ballartelyweb.jpa.data;

import java.io.Serializable;
import javax.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;


/**
 * The persistent class for the general_parameter database table.
 * 
 */
@Entity
@Table(name="general_parameter")
@EntityListeners(AuditingEntityListener.class)
public class GeneralParameter implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;

	@Column(name="param_code")
	private String paramCode;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="param_creation_date")
	@CreatedDate
	private Date paramCreationDate;

	@Column(name="param_description")
	private String paramDescription;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="param_modification_date")
	@LastModifiedDate
	private Date paramModificationDate;

	@Column(name="param_status")
	private String paramStatus;

	@Column(name="param_type")
	private String paramType;

	@Column(name="param_value")
	private String paramValue;

	public GeneralParameter() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getParamCode() {
		return this.paramCode;
	}

	public void setParamCode(String paramCode) {
		this.paramCode = paramCode;
	}

	public Date getParamCreationDate() {
		return this.paramCreationDate;
	}

	public void setParamCreationDate(Date paramCreationDate) {
		this.paramCreationDate = paramCreationDate;
	}

	public String getParamDescription() {
		return this.paramDescription;
	}

	public void setParamDescription(String paramDescription) {
		this.paramDescription = paramDescription;
	}

	public Date getParamModificationDate() {
		return this.paramModificationDate;
	}

	public void setParamModificationDate(Date paramModificationDate) {
		this.paramModificationDate = paramModificationDate;
	}

	public String getParamStatus() {
		return this.paramStatus;
	}

	public void setParamStatus(String paramStatus) {
		this.paramStatus = paramStatus;
	}

	public String getParamType() {
		return this.paramType;
	}

	public void setParamType(String paramType) {
		this.paramType = paramType;
	}

	public String getParamValue() {
		return this.paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

}