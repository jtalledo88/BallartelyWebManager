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
	@Column(name="movement_id")
	private int movementId;

	@Column(name="movement_amount")
	private BigDecimal movementAmount;

	@Temporal(TemporalType.DATE)
	@Column(name="movement_date")
	@CreatedDate
	private Date movementDate;

	@Column(name="movement_observation")
	private String movementObservation;

	@Column(name="movement_quantity")
	private int movementQuantity;

	@Column(name="movement_type")
	private String movementType;

	@Column(name="payment_documentnumber")
	private String paymentDocumentnumber;

	//bi-directional many-to-one association to Account
	@ManyToOne
	@JoinColumn(name="account_id")
	private Account account;

	//bi-directional many-to-one association to Provider
	@ManyToOne
	@JoinColumn(name="provider_id")
	private Provider provider;

	public Movement() {
	}

	public int getMovementId() {
		return this.movementId;
	}

	public void setMovementId(int movementId) {
		this.movementId = movementId;
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

	public int getMovementQuantity() {
		return this.movementQuantity;
	}

	public void setMovementQuantity(int movementQuantity) {
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

}