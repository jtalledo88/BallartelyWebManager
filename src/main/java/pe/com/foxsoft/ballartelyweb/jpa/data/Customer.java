package pe.com.foxsoft.ballartelyweb.jpa.data;

import java.io.Serializable;
import javax.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the Customer database table.
 * 
 */
@Entity
@Table(name = "customer")
@EntityListeners(AuditingEntityListener.class)
public class Customer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;

	@Column(name="customer_address")
	private String customerAddress;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="customer_creation_date")
	@CreatedDate
	private Date customerCreationDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="customer_modification_date")
	@LastModifiedDate
	private Date customerModificationDate;

	@Column(name="customer_names")
	private String customerNames;

	@Column(name="customer_phone_number")
	private String customerPhoneNumber;

	@Column(name="customer_status")
	private String customerStatus;

	@Column(name="customer_type")
	private String customerType;

	@Column(name="document_number", unique=true)
	private String documentNumber;

	@Column(name="document_type")
	private String documentType;

	//bi-directional many-to-one association to Account
	@OneToMany(mappedBy="customer", fetch=FetchType.LAZY)
	private List<Account> accounts;

	public Customer() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCustomerAddress() {
		return this.customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public Date getCustomerCreationDate() {
		return this.customerCreationDate;
	}

	public void setCustomerCreationDate(Date customerCreationDate) {
		this.customerCreationDate = customerCreationDate;
	}

	public Date getCustomerModificationDate() {
		return this.customerModificationDate;
	}

	public void setCustomerModificationDate(Date customerModificationDate) {
		this.customerModificationDate = customerModificationDate;
	}

	public String getCustomerNames() {
		return this.customerNames;
	}

	public void setCustomerNames(String customerNames) {
		this.customerNames = customerNames;
	}

	public String getCustomerPhoneNumber() {
		return this.customerPhoneNumber;
	}

	public void setCustomerPhoneNumber(String customerPhoneNumber) {
		this.customerPhoneNumber = customerPhoneNumber;
	}

	public String getCustomerStatus() {
		return this.customerStatus;
	}

	public void setCustomerStatus(String customerStatus) {
		this.customerStatus = customerStatus;
	}

	public String getCustomerType() {
		return this.customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
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
		account.setCustomer(this);

		return account;
	}

	public Account removeAccount(Account account) {
		getAccounts().remove(account);
		account.setCustomer(null);

		return account;
	}

}