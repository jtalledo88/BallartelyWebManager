package pe.com.foxsoft.ballartelyweb.jpa.data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * The persistent class for the transport database table.
 * 
 */
@Entity
@Table(name = "transport")
@EntityListeners(AuditingEntityListener.class)
public class Transport implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@Column(name="car_number")
	private String carNumber;
	
	@Column(name="car_mark")
	private String carMark;
	
	@Column(name="driver_license_number")
	private String driverLicenseNumber;
	
	@Column(name="driver_names")
	private String driverNames;
	
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	@Column(name="transport_creation_date")
	private Date transportCreationDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	@Column(name="transport_modification_date")
	private Date transportModificationDate;
	
	@Column(name="transport_status")
	private String transportStatus;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCarNumber() {
		return carNumber;
	}

	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}

	public String getCarMark() {
		return carMark;
	}

	public void setCarMark(String carMark) {
		this.carMark = carMark;
	}

	public String getDriverLicenseNumber() {
		return driverLicenseNumber;
	}

	public void setDriverLicenseNumber(String driverLicenseNumber) {
		this.driverLicenseNumber = driverLicenseNumber;
	}

	public String getDriverNames() {
		return driverNames;
	}

	public void setDriverNames(String driverNames) {
		this.driverNames = driverNames;
	}

	public Date getTransportCreationDate() {
		return transportCreationDate;
	}

	public void setTransportCreationDate(Date transportCreationDate) {
		this.transportCreationDate = transportCreationDate;
	}

	public Date getTransportModificationDate() {
		return transportModificationDate;
	}

	public void setTransportModificationDate(Date transportModificationDate) {
		this.transportModificationDate = transportModificationDate;
	}

	public String getTransportStatus() {
		return transportStatus;
	}

	public void setTransportStatus(String transportStatus) {
		this.transportStatus = transportStatus;
	}
	
	@Override
    public int hashCode() {
        int hash = 0;
        hash += id;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Transport)) {
            return false;
        }
        Transport other = (Transport) object;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
	
}
