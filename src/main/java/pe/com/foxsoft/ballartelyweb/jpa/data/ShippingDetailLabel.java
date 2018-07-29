package pe.com.foxsoft.ballartelyweb.jpa.data;

import java.io.Serializable;
import javax.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;


/**
 * The persistent class for the shipping_detail_label database table.
 * 
 */
@Entity
@Table(name="shipping_detail_label")
@EntityListeners(AuditingEntityListener.class)
public class ShippingDetailLabel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="shipping_detail_label_id")
	private int shippingDetailLabelId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="shipping_detail_label_creation_date")
	@CreatedDate
	private Date shippingDetailLabelCreationDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="shipping_detail_label_modification_date")
	@LastModifiedDate
	private Date shippingDetailLabelModificationDate;
	
	@Column(name="shipping_detail_label_quantity_benefit")
	private int shippingDetailLabelQuantityBenefit;

	@Column(name="shipping_detail_label_type")
	private String shippingDetailLabelType;

	//bi-directional many-to-one association to ProductLabel
	@ManyToOne
	@JoinColumn(name="product_label_id")
	private ProductLabel productLabel;

	//bi-directional many-to-one association to ShippingDetail
	@ManyToOne
	@JoinColumn(name="shipping_detail_id")
	private GuideDetail shippingDetail;

	public ShippingDetailLabel() {
	}

	public int getShippingDetailLabelId() {
		return this.shippingDetailLabelId;
	}

	public void setShippingDetailLabelId(int shippingDetailLabelId) {
		this.shippingDetailLabelId = shippingDetailLabelId;
	}

	public Date getShippingDetailLabelCreationDate() {
		return this.shippingDetailLabelCreationDate;
	}

	public void setShippingDetailLabelCreationDate(Date shippingDetailLabelCreationDate) {
		this.shippingDetailLabelCreationDate = shippingDetailLabelCreationDate;
	}

	public Date getShippingDetailLabelModificationDate() {
		return this.shippingDetailLabelModificationDate;
	}

	public void setShippingDetailLabelModificationDate(Date shippingDetailLabelModificationDate) {
		this.shippingDetailLabelModificationDate = shippingDetailLabelModificationDate;
	}
	
	public int getShippingDetailLabelQuantityBenefit() {
		return shippingDetailLabelQuantityBenefit;
	}

	public void setShippingDetailLabelQuantityBenefit(int shippingDetailLabelQuantityBenefit) {
		this.shippingDetailLabelQuantityBenefit = shippingDetailLabelQuantityBenefit;
	}

	public String getShippingDetailLabelType() {
		return this.shippingDetailLabelType;
	}

	public void setShippingDetailLabelType(String shippingDetailLabelType) {
		this.shippingDetailLabelType = shippingDetailLabelType;
	}

	public ProductLabel getProductLabel() {
		return this.productLabel;
	}

	public void setProductLabel(ProductLabel productLabel) {
		this.productLabel = productLabel;
	}

	public GuideDetail getShippingDetail() {
		return this.shippingDetail;
	}

	public void setShippingDetail(GuideDetail shippingDetail) {
		this.shippingDetail = shippingDetail;
	}

}