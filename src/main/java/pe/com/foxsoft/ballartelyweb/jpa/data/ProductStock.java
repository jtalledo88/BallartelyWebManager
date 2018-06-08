package pe.com.foxsoft.ballartelyweb.jpa.data;

import java.io.Serializable;
import javax.persistence.*;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;


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
	@Column(name="product_label_id")
	private int productLabelId;

	@Column(name="product_stock_cant")
	private int productStockCant;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="product_stock_modification_date")
	@LastModifiedDate
	private Date productStockModificationDate;

	public ProductStock() {
	}

	public int getProductLabelId() {
		return this.productLabelId;
	}

	public void setProductLabelId(int productLabelId) {
		this.productLabelId = productLabelId;
	}

	public int getProductStockCant() {
		return this.productStockCant;
	}

	public void setProductStockCant(int productStockCant) {
		this.productStockCant = productStockCant;
	}

	public Date getProductStockModificationDate() {
		return this.productStockModificationDate;
	}

	public void setProductStockModificationDate(Date productStockModificationDate) {
		this.productStockModificationDate = productStockModificationDate;
	}

}