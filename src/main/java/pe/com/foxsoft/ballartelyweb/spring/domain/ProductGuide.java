package pe.com.foxsoft.ballartelyweb.spring.domain;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductGuide implements Serializable, Comparable<ProductGuide>{
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String firstColumn;
	private Integer stockInput;
	private Integer stockProduct;
	private BigDecimal costProduct;
	
	public ProductGuide() {
		
	}
	
	public ProductGuide(Integer productLabelId, String firstColumn) {
		this.id = productLabelId;
		this.firstColumn = firstColumn;
		this.stockInput = null;
		this.stockProduct = null;
		this.costProduct = null;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstColumn() {
		return firstColumn;
	}

	public void setFirstColumn(String firstColumn) {
		this.firstColumn = firstColumn;
	}

	public Integer getStockInput() {
		return stockInput;
	}

	public void setStockInput(Integer stockInput) {
		this.stockInput = stockInput;
	}

	public Integer getStockProduct() {
		return stockProduct;
	}
	public void setStockProduct(Integer stockProduct) {
		this.stockProduct = stockProduct;
	}
	public BigDecimal getCostProduct() {
		return costProduct;
	}
	public void setCostProduct(BigDecimal costProduct) {
		this.costProduct = costProduct;
	}
	//Eclipse Generated hashCode and equals
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((firstColumn == null) ? 0 : firstColumn.hashCode());
        result = prime * result + ((stockInput == null) ? 0 : stockInput.hashCode());
        result = prime * result + ((stockProduct == null) ? 0 : stockProduct.hashCode());
        result = prime * result + ((costProduct == null) ? 0 : costProduct.hashCode());
        return result;
    }
 
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ProductGuide other = (ProductGuide) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (firstColumn == null) {
            if (other.firstColumn != null)
                return false;
        } else if (!firstColumn.equals(other.firstColumn))
            return false;
        if (stockInput == null) {
            if (other.stockInput != null)
                return false;
        } else if (!stockInput.equals(other.stockInput))
            return false;
        if (stockProduct == null) {
            if (other.stockProduct != null)
                return false;
        } else if (!stockProduct.equals(other.stockProduct))
            return false;
        if (costProduct == null) {
            if (other.costProduct != null)
                return false;
        } else if (!costProduct.equals(other.costProduct))
            return false;
        return true;
    }
 
    @Override
    public String toString() {
        return firstColumn;
    }
	@Override
	public int compareTo(ProductGuide o) {
		return this.getFirstColumn().compareTo(o.getFirstColumn());
	}
	

}
