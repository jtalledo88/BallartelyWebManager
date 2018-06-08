package pe.com.foxsoft.ballartelyweb.jpa.data;

import java.io.Serializable;
import javax.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the account database table.
 * 
 */
@Entity
@Table(name = "account")
@EntityListeners(AuditingEntityListener.class)
public class Account implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="account_id")
	private int accountId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="account_creation_date")
	@CreatedDate
	private Date accountCreationDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="account_modification_date")
	@LastModifiedDate
	private Date accountModificationDate;

	@Column(name="account_status")
	private String accountStatus;

	@Column(name="account_type")
	private String accountType;

	//bi-directional many-to-one association to Client
	@ManyToOne
	@JoinColumn(name="account_owner")
	private Client client;

	//bi-directional many-to-one association to Movement
	@OneToMany(mappedBy="account", fetch=FetchType.LAZY)
	private List<Movement> movements;

	public Account() {
	}

	public int getAccountId() {
		return this.accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public Date getAccountCreationDate() {
		return this.accountCreationDate;
	}

	public void setAccountCreationDate(Date accountCreationDate) {
		this.accountCreationDate = accountCreationDate;
	}

	public Date getAccountModificationDate() {
		return this.accountModificationDate;
	}

	public void setAccountModificationDate(Date accountModificationDate) {
		this.accountModificationDate = accountModificationDate;
	}

	public String getAccountStatus() {
		return this.accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	public String getAccountType() {
		return this.accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public Client getClient() {
		return this.client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public List<Movement> getMovements() {
		return this.movements;
	}

	public void setMovements(List<Movement> movements) {
		this.movements = movements;
	}

	public Movement addMovement(Movement movement) {
		getMovements().add(movement);
		movement.setAccount(this);

		return movement;
	}

	public Movement removeMovement(Movement movement) {
		getMovements().remove(movement);
		movement.setAccount(null);

		return movement;
	}

}