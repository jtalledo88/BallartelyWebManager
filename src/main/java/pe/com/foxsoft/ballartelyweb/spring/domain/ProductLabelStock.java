package pe.com.foxsoft.ballartelyweb.spring.domain;

import pe.com.foxsoft.ballartelyweb.jpa.data.ProductLabel;
import pe.com.foxsoft.ballartelyweb.jpa.data.ProductStock;

public class ProductLabelStock {
	
	private int item;
	private ProductLabel productLabel;
	private ProductStock productStock;
	
	public int getItem() {
		return item;
	}
	public void setItem(int item) {
		this.item = item;
	}
	public ProductLabel getProductLabel() {
		return productLabel;
	}
	public void setProductLabel(ProductLabel productLabel) {
		this.productLabel = productLabel;
	}
	public ProductStock getProductStock() {
		return productStock;
	}
	public void setProductStock(ProductStock productStock) {
		this.productStock = productStock;
	}

	
}
