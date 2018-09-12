package pe.com.foxsoft.ballartelyweb.jpa.data;

import java.io.Serializable;
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
 * The persistent class for the product_stock database table.
 * 
 */
@Entity
@Table(name="product_stock")
@EntityListeners(AuditingEntityListener.class)
public class ProductStock implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;

	@Column(name="product_stock_cant")
	private Integer productStockCant;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="product_stock_creation_date")
	@CreatedDate
	private Date productStockCreationDate;
	
	@ManyToOne
	@JoinColumn(name="guide_head_id")
	private GuideHead guideHead;
	
	@ManyToOne
	@JoinColumn(name="product_label_id")
	private ProductLabel productLabel;

	public ProductStock() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getProductStockCant() {
		return this.productStockCant;
	}

	public void setProductStockCant(Integer productStockCant) {
		this.productStockCant = productStockCant;
	}

	public Date getProductStockCreationDate() {
		return this.productStockCreationDate;
	}

	public void setProductStockCreationDate(Date productStockCreationDate) {
		this.productStockCreationDate = productStockCreationDate;
	}

	public GuideHead getGuideHead() {
		return guideHead;
	}

	public void setGuideHead(GuideHead guideHead) {
		this.guideHead = guideHead;
	}

	public ProductLabel getProductLabel() {
		return productLabel;
	}

	public void setProductLabel(ProductLabel productLabel) {
		this.productLabel = productLabel;
	}

}