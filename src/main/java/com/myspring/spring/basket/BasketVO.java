package com.myspring.spring.basket;

public class BasketVO {
	private long basketIdx;
	private String id;
	private int productNo;
	private String selectedColor;
	private String selectedSize;
	private int amount;

	public BasketVO() {
	}

	public BasketVO(long basketIdx, String id, int productNo, String selectedColor, String selectedSize, int amount) {
		this.basketIdx = basketIdx;
		this.id = id;
		this.productNo = productNo;
		this.selectedColor = selectedColor;
		this.selectedSize = selectedSize;
		this.amount = amount;
	}

	public long getBasketIdx() {
		return basketIdx;
	}

	public void setBasketIdx(long basketIdx) {
		this.basketIdx = basketIdx;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getProductNo() {
		return productNo;
	}

	public void setProductNo(int productNo) {
		this.productNo = productNo;
	}

	public String getSelectedColor() {
		return selectedColor;
	}

	public void setSelectedColor(String selectedColor) {
		this.selectedColor = selectedColor;
	}

	public String getSelectedSize() {
		return selectedSize;
	}

	public void setSelectedSize(String selectedSize) {
		this.selectedSize = selectedSize;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

}
