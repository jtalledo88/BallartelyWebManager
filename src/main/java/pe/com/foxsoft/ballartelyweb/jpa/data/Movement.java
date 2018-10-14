package pe.com.foxsoft.ballartelyweb.jpa.data;

import java.io.Serializable;
import javax.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the movements database table.
 * 
 */
@Entity
@Table(name="movements")
@EntityListeners(AuditingEntityListener.class)
public class Movement implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;

	@Column(name="movement_amount")
	private BigDecimal movementAmount;

	@Temporal(TemporalType.DATE)
	@Column(name="movement_date")
	@CreatedDate
	private Date movementDate;

	@Column(name="movement_observation")
	private String movementObservation;

	@Column(name="movement_quantity")
	private Integer movementQuantity;

	@Column(name="movement_type")
	private String movementType;

	@Column(name="payment_documentnumber", unique=true)
	private String paymentDocumentnumber;

	//bi-directional many-to-one association to Account
	@ManyToOne
	@JoinColumn(name="account_id")
	private Account account;

	//bi-directional many-to-one association to Provider
	@ManyToOne
	@JoinColumn(name="provider_id")
	private Provider provider;
	
	//bi-directional many-to-one association to Customer
	@ManyToOne
	@JoinColumn(name="customer_id")
	private Customer customer;

	public Movement() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getMovementAmount() {
		return this.movementAmount;
	}

	public void setMovementAmount(BigDecimal movementAmount) {
		this.movementAmount = movementAmount;
	}

	public Date getMovementDate() {
		return this.movementDate;
	}

	public void setMovementDate(Date movementDate) {
		this.movementDate = movementDate;
	}

	public String getMovementObservation() {
		return this.movementObservation;
	}

	public void setMovementObservation(String movementObservation) {
		this.movementObservation = movementObservation;
	}

	public Integer getMovementQuantity() {
		return this.movementQuantity;
	}

	public void setMovementQuantity(Integer movementQuantity) {
		this.movementQuantity = movementQuantity;
	}

	public String getMovementType() {
		return this.movementType;
	}

	public void setMovementType(String movementType) {
		this.movementType = movementType;
	}

	public String getPaymentDocumentnumber() {
		return this.paymentDocumentnumber;
	}

	public void setPaymentDocumentnumber(String paymentDocumentnumber) {
		this.paymentDocumentnumber = paymentDocumentnumber;
	}

	public Account getAccount() {
		return this.account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Provider getProvider() {
		return this.provider;
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

}