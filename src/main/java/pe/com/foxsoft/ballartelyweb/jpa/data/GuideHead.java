package pe.com.foxsoft.ballartelyweb.jpa.data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


/**
 * The persistent class for the guide_head database table.
 * 
 */
@Entity
@Table(name="guide_head")
@EntityListeners(AuditingEntityListener.class)
public class GuideHead implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;

	@Column(name="guide_number", unique=true)
	private String guideNumber;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="emission_date")
	private Date emissionDate;
	
	@Column(name = "invoice_number", unique=true)
	private String invoiceNumber;
	
	@Column(name = "observations")
	private String observations;
	
	@Column(name = "start_point")
	private String startPoint;
	
	@Column(name = "end_point")
	private String endPoint;
	
	@Column(name = "guide_benefied")
	private String guideBenefied;
	
	@Column(name = "guide_cotized")
	private String guideCotized;
	
	//bi-directional many-to-one association to Enterprise
	@ManyToOne
	@JoinColumn(name="enterprise_id")
	private Enterprise enterprise;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="guide_creation_date")
	@CreatedDate
	private Date guideCreationDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="guide_modification_date")
	@LastModifiedDate
	private Date guideModificationDate;

	@Column(name="guide_file")
	private String guideFile;

	@Column(name="guide_status")
	private String guideStatus;
	
	@Column(name="reason")
	private String reason;
	
	@Column(name="guide_type")
	private String guideType;
	
	//bi-directional many-to-one association to Transport
	@ManyToOne
	@JoinColumn(name="transport_id")
	private Transport transport;

	//bi-directional many-to-one association to GuideDetail
	@OneToMany(mappedBy="guideHead", fetch=FetchType.LAZY)
	private List<GuideDetail> guideDetails;
	
	//bi-directional many-to-one association to Invoice
	@OneToMany(mappedBy="guideHead", fetch=FetchType.LAZY)
	private List<Invoice> invoices;

	//bi-directional many-to-one association to Provider
	@ManyToOne
	@JoinColumn(name="provider_id")
	private Provider provider;
	
	//bi-directional many-to-one association to Provider
	@ManyToOne
	@JoinColumn(name="customer_id")
	private Customer customer;

	public GuideHead() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGuideNumber() {
		return guideNumber;
	}

	public void setGuideNumber(String guideNumber) {
		this.guideNumber = guideNumber;
	}

	public Date getEmissionDate() {
		return emissionDate;
	}

	public void setEmissionDate(Date emissionDate) {
		this.emissionDate = emissionDate;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getObservations() {
		return observations;
	}

	public void setObservations(String observations) {
		this.observations = observations;
	}

	public String getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(String startPoint) {
		this.startPoint = startPoint;
	}

	public String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	public Enterprise getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(Enterprise enterprise) {
		this.enterprise = enterprise;
	}

	public Date getGuideCreationDate() {
		return guideCreationDate;
	}

	public void setGuideCreationDate(Date guideCreationDate) {
		this.guideCreationDate = guideCreationDate;
	}

	public Date getGuideModificationDate() {
		return guideModificationDate;
	}

	public void setGuideModificationDate(Date guideModificationDate) {
		this.guideModificationDate = guideModificationDate;
	}

	public String getGuideFile() {
		return guideFile;
	}

	public void setGuideFile(String guideFile) {
		this.guideFile = guideFile;
	}

	public String getGuideStatus() {
		return guideStatus;
	}

	public void setGuideStatus(String guideStatus) {
		this.guideStatus = guideStatus;
	}

	public String getGuideBenefied() {
		return guideBenefied;
	}

	public void setGuideBenefied(String guideBenefied) {
		this.guideBenefied = guideBenefied;
	}

	public String getGuideCotized() {
		return guideCotized;
	}

	public void setGuideCotized(String guideCotized) {
		this.guideCotized = guideCotized;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getGuideType() {
		return guideType;
	}

	public void setGuideType(String guideType) {
		this.guideType = guideType;
	}

	public List<GuideDetail> getGuideDetails() {
		return guideDetails;
	}

	public void setGuideDetails(List<GuideDetail> guideDetails) {
		this.guideDetails = guideDetails;
	}

	public List<Invoice> getInvoices() {
		return invoices;
	}

	public void setInvoices(List<Invoice> invoices) {
		this.invoices = invoices;
	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Transport getTransport() {
		return transport;
	}

	public void setTransport(Transport transport) {
		this.transport = transport;
	}

}