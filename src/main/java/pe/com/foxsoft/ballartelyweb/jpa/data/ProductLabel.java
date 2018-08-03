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
 * The persistent class for the product_label database table.
 * 
 */
@Entity
@Table(name="product_label")
@EntityListeners(AuditingEntityListener.class)
public class ProductLabel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;

	@Column(name="product_label_code")
	private String productLabelCode;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="product_label_creation_date")
	@CreatedDate
	private Date productLabelCreationDate;

	@Column(name="product_label_description")
	private String productLabelDescription;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="product_label_modification_date")
	@LastModifiedDate
	private Date productLabelModificationDate;

	@Column(name="product_label_status")
	private String productLabelStatus;

	public ProductLabel() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProductLabelCode() {
		return this.productLabelCode;
	}

	public void setProductLabelCode(String productLabelCode) {
		this.productLabelCode = productLabelCode;
	}

	public Date getProductLabelCreationDate() {
		return this.productLabelCreationDate;
	}

	public void setProductLabelCreationDate(Date productLabelCreationDate) {
		this.productLabelCreationDate = productLabelCreationDate;
	}

	public String getProductLabelDescription() {
		return this.productLabelDescription;
	}

	public void setProductLabelDescription(String productLabelDescription) {
		this.productLabelDescription = productLabelDescription;
	}

	public Date getProductLabelModificationDate() {
		return this.productLabelModificationDate;
	}

	public void setProductLabelModificationDate(Date productLabelModificationDate) {
		this.productLabelModificationDate = productLabelModificationDate;
	}

	public String getProductLabelStatus() {
		return this.productLabelStatus;
	}

	public void setProductLabelStatus(String productLabelStatus) {
		this.productLabelStatus = productLabelStatus;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += id;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ProductLabel)) {
            return false;
        }
        ProductLabel other = (ProductLabel) object;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}