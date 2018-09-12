package pe.com.foxsoft.ballartelyweb.jpa.data;

import java.io.Serializable;
import java.math.BigDecimal;
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

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


/**
 * The persistent class for the guide_detail_sales database table.
 * 
 */
@Entity
@Table(name="guide_detail_sales")
@EntityListeners(AuditingEntityListener.class)
public class GuideDetailSales implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@Column(name="product_quantity")
	private Integer productQuantity;

	@Column(name="metric_unit")
	private String metricUnit;
	
	@Column(name="total_weight")
	private BigDecimal totalWeight;
	
	@Column(name="total_cost")
	private BigDecimal totalCost;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="guide_creation_date")
	@CreatedDate
	private Date guideCreationDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="guide_modification_date")
	@LastModifiedDate
	private Date guideModificationDate;

	//bi-directional many-to-one association to ProductLabel
	@ManyToOne
	@JoinColumn(name="product_label_id")
	private ProductLabel productLabel;
	
	//bi-directional many-to-one association to GuideHead
	@ManyToOne
	@JoinColumn(name="guide_head_id")
	private GuideHead guideHead;
	
	public GuideDetailSales() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getProductQuantity() {
		return productQuantity;
	}

	public void setProductQuantity(Integer productQuantity) {
		this.productQuantity = productQuantity;
	}

	public String getMetricUnit() {
		return metricUnit;
	}

	public void setMetricUnit(String metricUnit) {
		this.metricUnit = metricUnit;
	}

	public BigDecimal getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(BigDecimal totalWeight) {
		this.totalWeight = totalWeight;
	}

	public BigDecimal getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(BigDecimal totalCost) {
		this.totalCost = totalCost;
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

	public ProductLabel getProductLabel() {
		return productLabel;
	}

	public void setProductLabel(ProductLabel productLabel) {
		this.productLabel = productLabel;
	}

	public GuideHead getGuideHead() {
		return guideHead;
	}

	public void setGuideHead(GuideHead guideHead) {
		this.guideHead = guideHead;
	}
}