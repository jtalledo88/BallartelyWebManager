package pe.com.foxsoft.ballartelyweb.jpa.data;

import java.io.Serializable;
import javax.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.List;


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
	@Column(name="product_label_id")
	private int productLabelId;

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

	//bi-directional many-to-one association to ShippingDetail
	@OneToMany(mappedBy="productLabel", fetch=FetchType.LAZY)
	private List<ShippingDetail> shippingDetails;

	//bi-directional many-to-one association to ShippingDetailLabel
	@OneToMany(mappedBy="productLabel", fetch=FetchType.LAZY)
	private List<ShippingDetailLabel> shippingDetailLabels;

	public ProductLabel() {
	}

	public int getProductLabelId() {
		return this.productLabelId;
	}

	public void setProductLabelId(int productLabelId) {
		this.productLabelId = productLabelId;
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

	public List<ShippingDetail> getShippingDetails() {
		return this.shippingDetails;
	}

	public void setShippingDetails(List<ShippingDetail> shippingDetails) {
		this.shippingDetails = shippingDetails;
	}

	public ShippingDetail addShippingDetail(ShippingDetail shippingDetail) {
		getShippingDetails().add(shippingDetail);
		shippingDetail.setProductLabel(this);

		return shippingDetail;
	}

	public ShippingDetail removeShippingDetail(ShippingDetail shippingDetail) {
		getShippingDetails().remove(shippingDetail);
		shippingDetail.setProductLabel(null);

		return shippingDetail;
	}

	public List<ShippingDetailLabel> getShippingDetailLabels() {
		return this.shippingDetailLabels;
	}

	public void setShippingDetailLabels(List<ShippingDetailLabel> shippingDetailLabels) {
		this.shippingDetailLabels = shippingDetailLabels;
	}

	public ShippingDetailLabel addShippingDetailLabel(ShippingDetailLabel shippingDetailLabel) {
		getShippingDetailLabels().add(shippingDetailLabel);
		shippingDetailLabel.setProductLabel(this);

		return shippingDetailLabel;
	}

	public ShippingDetailLabel removeShippingDetailLabel(ShippingDetailLabel shippingDetailLabel) {
		getShippingDetailLabels().remove(shippingDetailLabel);
		shippingDetailLabel.setProductLabel(null);

		return shippingDetailLabel;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += productLabelId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ProductLabel)) {
            return false;
        }
        ProductLabel other = (ProductLabel) object;
        if (this.productLabelId != other.productLabelId) {
            return false;
        }
        return true;
    }
}