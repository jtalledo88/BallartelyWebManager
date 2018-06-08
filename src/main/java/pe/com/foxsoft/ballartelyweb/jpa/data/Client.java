package pe.com.foxsoft.ballartelyweb.jpa.data;

import java.io.Serializable;
import javax.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the client database table.
 * 
 */
@Entity
@Table(name = "client")
@EntityListeners(AuditingEntityListener.class)
public class Client implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="client_id")
	private int clientId;

	@Column(name="client_address")
	private String clientAddress;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="client_creation_date")
	@CreatedDate
	private Date clientCreationDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="client_modification_date")
	@LastModifiedDate
	private Date clientModificationDate;

	@Column(name="client_names")
	private String clientNames;

	@Column(name="client_phone_number")
	private String clientPhoneNumber;

	@Column(name="client_status")
	private String clientStatus;

	@Column(name="client_type")
	private String clientType;

	@Column(name="document_number")
	private String documentNumber;

	@Column(name="document_type")
	private String documentType;

	//bi-directional many-to-one association to Account
	@OneToMany(mappedBy="client", fetch=FetchType.LAZY)
	private List<Account> accounts;

	public Client() {
	}

	public int getClientId() {
		return this.clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public String getClientAddress() {
		return this.clientAddress;
	}

	public void setClientAddress(String clientAddress) {
		this.clientAddress = clientAddress;
	}

	public Date getClientCreationDate() {
		return this.clientCreationDate;
	}

	public void setClientCreationDate(Date clientCreationDate) {
		this.clientCreationDate = clientCreationDate;
	}

	public Date getClientModificationDate() {
		return this.clientModificationDate;
	}

	public void setClientModificationDate(Date clientModificationDate) {
		this.clientModificationDate = clientModificationDate;
	}

	public String getClientNames() {
		return this.clientNames;
	}

	public void setClientNames(String clientNames) {
		this.clientNames = clientNames;
	}

	public String getClientPhoneNumber() {
		return this.clientPhoneNumber;
	}

	public void setClientPhoneNumber(String clientPhoneNumber) {
		this.clientPhoneNumber = clientPhoneNumber;
	}

	public String getClientStatus() {
		return this.clientStatus;
	}

	public void setClientStatus(String clientStatus) {
		this.clientStatus = clientStatus;
	}

	public String getClientType() {
		return this.clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	public String getDocumentNumber() {
		return this.documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	public String getDocumentType() {
		return this.documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public List<Account> getAccounts() {
		return this.accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}

	public Account addAccount(Account account) {
		getAccounts().add(account);
		account.setClient(this);

		return account;
	}

	public Account removeAccount(Account account) {
		getAccounts().remove(account);
		account.setClient(null);

		return account;
	}

}