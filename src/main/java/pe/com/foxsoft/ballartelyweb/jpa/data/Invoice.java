package pe.com.foxsoft.ballartelyweb.jpa.data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * The persistent class for the invoice database table.
 * 
 */
@Entity
@Table(name="invoice")
@EntityListeners(AuditingEntityListener.class)
public class Invoice implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@Column(name = "invoice_serie")
	private String invoiceSerie;
	
	@Column(name = "invoice_number")
	private String invoiceNumber;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="invoice_emission_date")
	private Date invoiceEmissionDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="invoice_expiration_date")
	private Date invoiceExpirationDate;
	
	@Column(name = "invoice_sunat_code")
	private String invoiceSunatCode;
	
	@Column(name = "invoice_sunat_message")
	private String invoiceSunatMessage;
	
	//bi-directional many-to-one association to GuideHead
	@ManyToOne
	@JoinColumn(name="guide_head_id")
	private GuideHead guideHead;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getInvoiceSerie() {
		return invoiceSerie;
	}

	public void setInvoiceSerie(String invoiceSerie) {
		this.invoiceSerie = invoiceSerie;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public Date getInvoiceEmissionDate() {
		return invoiceEmissionDate;
	}

	public void setInvoiceEmissionDate(Date invoiceEmissionDate) {
		this.invoiceEmissionDate = invoiceEmissionDate;
	}

	public Date getInvoiceExpirationDate() {
		return invoiceExpirationDate;
	}

	public void setInvoiceExpirationDate(Date invoiceExpirationDate) {
		this.invoiceExpirationDate = invoiceExpirationDate;
	}

	public String getInvoiceSunatCode() {
		return invoiceSunatCode;
	}

	public void setInvoiceSunatCode(String invoiceSunatCode) {
		this.invoiceSunatCode = invoiceSunatCode;
	}

	public String getInvoiceSunatMessage() {
		return invoiceSunatMessage;
	}

	public void setInvoiceSunatMessage(String invoiceSunatMessage) {
		this.invoiceSunatMessage = invoiceSunatMessage;
	}

	public GuideHead getGuideHead() {
		return guideHead;
	}

	public void setGuideHead(GuideHead guideHead) {
		this.guideHead = guideHead;
	}

}
