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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * The persistent class for the guide_cotization database table.
 * 
 */
@Entity
@Table(name="guide_cotization")
@EntityListeners(AuditingEntityListener.class)
public class GuideCotization implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="unit_cost_guide")
	private BigDecimal unitCostGuide;
	
	@Column(name="total_weight_dehydrated")
	private BigDecimal totalWeightDehydrated;
	
	@Column(name="total_expenses")
	private BigDecimal totalExpenses;
	
	@Column(name="total_decrease")
	private BigDecimal totalDecrease;
	
	@Column(name="total_unit_cost")
	private BigDecimal totalUnitCost;
	
	@Temporal(TemporalType.DATE)
	@Column(name="cotization_creation_date")
	@CreatedDate
	private Date cotizationCreationDate;
	
	@ManyToOne
	@JoinColumn(name="guide_head_id")
	private GuideHead guideHead;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public BigDecimal getUnitCostGuide() {
		return unitCostGuide;
	}

	public void setUnitCostGuide(BigDecimal unitCostGuide) {
		this.unitCostGuide = unitCostGuide;
	}

	public BigDecimal getTotalWeightDehydrated() {
		return totalWeightDehydrated;
	}

	public void setTotalWeightDehydrated(BigDecimal totalWeightDehydrated) {
		this.totalWeightDehydrated = totalWeightDehydrated;
	}

	public BigDecimal getTotalExpenses() {
		return totalExpenses;
	}

	public void setTotalExpenses(BigDecimal totalExpenses) {
		this.totalExpenses = totalExpenses;
	}

	public BigDecimal getTotalDecrease() {
		return totalDecrease;
	}

	public void setTotalDecrease(BigDecimal totalDecrease) {
		this.totalDecrease = totalDecrease;
	}

	public BigDecimal getTotalUnitCost() {
		return totalUnitCost;
	}

	public void setTotalUnitCost(BigDecimal totalUnitCost) {
		this.totalUnitCost = totalUnitCost;
	}

	public Date getCotizationCreationDate() {
		return cotizationCreationDate;
	}

	public void setCotizationCreationDate(Date cotizationCreationDate) {
		this.cotizationCreationDate = cotizationCreationDate;
	}

	public GuideHead getGuideHead() {
		return guideHead;
	}

	public void setGuideHead(GuideHead guideHead) {
		this.guideHead = guideHead;
	}
}
