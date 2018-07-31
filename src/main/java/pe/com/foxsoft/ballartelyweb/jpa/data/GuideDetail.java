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
 * The persistent class for the shipping_detail database table.
 * 
 */
@Entity
@Table(name="guide_detail")
@EntityListeners(AuditingEntityListener.class)
public class GuideDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;

	@Column(name="boxes_quantity")
	private int boxesQuantity;
	
	@Column(name="product_quantity")
	private int productQuantity;
	
	@Column(name="initial_weight")
	private BigDecimal initialWeight;

	@Column(name="tara_weight")
	private BigDecimal taraWeight;
	
	@Column(name="net_weight")
	private BigDecimal netWeight;
	
	@Column(name="average")
	private BigDecimal average;
	
	@Column(name="unit_cost")
	private BigDecimal unitCost;
	
	@Column(name="product_description")
	private String productDescription;
	
	@Column(name="dead_quantity")
	private int deadQuantity;
	
	@Column(name="dead_weight")
	private BigDecimal deadWeight;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="guide_creation_date")
	@CreatedDate
	private Date guideCreationDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="guide_modification_date")
	@LastModifiedDate
	private Date guideModificationDate;

	//bi-directional many-to-one association to ShippingHead
	@ManyToOne
	@JoinColumn(name="guide_head_id")
	private GuideHead guideHead;
	
	public GuideDetail() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBoxesQuantity() {
		return boxesQuantity;
	}

	public void setBoxesQuantity(int boxesQuantity) {
		this.boxesQuantity = boxesQuantity;
	}

	public int getProductQuantity() {
		return productQuantity;
	}

	public void setProductQuantity(int productQuantity) {
		this.productQuantity = productQuantity;
	}

	public BigDecimal getInitialWeight() {
		return initialWeight;
	}

	public void setInitialWeight(BigDecimal initialWeight) {
		this.initialWeight = initialWeight;
	}

	public BigDecimal getTaraWeight() {
		return taraWeight;
	}

	public void setTaraWeight(BigDecimal taraWeight) {
		this.taraWeight = taraWeight;
	}

	public BigDecimal getNetWeight() {
		return netWeight;
	}

	public void setNetWeight(BigDecimal netWeight) {
		this.netWeight = netWeight;
	}

	public BigDecimal getAverage() {
		return average;
	}

	public void setAverage(BigDecimal average) {
		this.average = average;
	}

	public BigDecimal getUnitCost() {
		return unitCost;
	}

	public void setUnitCost(BigDecimal unitCost) {
		this.unitCost = unitCost;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public int getDeadQuantity() {
		return deadQuantity;
	}

	public void setDeadQuantity(int deadQuantity) {
		this.deadQuantity = deadQuantity;
	}

	public BigDecimal getDeadWeight() {
		return deadWeight;
	}

	public void setDeadWeight(BigDecimal deadWeight) {
		this.deadWeight = deadWeight;
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

	public GuideHead getGuideHead() {
		return guideHead;
	}

	public void setGuideHead(GuideHead guideHead) {
		this.guideHead = guideHead;
	}
}