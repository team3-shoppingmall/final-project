package com.myspring.spring.basket;

public class BasketAndProductVO {
	private long basketIdx;
	private String id;
	private int productNo;
	private String selectedColor;
	private String selectedSize;
	private int basketAmount;
	private int price;
	private int discount;
	private int amount;
	private boolean onSale;

	public BasketAndProductVO() {
	}

	public BasketAndProductVO(long basketIdx, String id, int productNo, String selectedColor, String selectedSize,
			int basketAmount, int price, int discount, int amount, boolean onSale) {
		this.basketIdx = basketIdx;
		this.id = id;
		this.productNo = productNo;
		this.selectedColor = selectedColor;
		this.selectedSize = selectedSize;
		this.basketAmount = basketAmount;
		this.price = price;
		this.discount = discount;
		this.amount = amount;
		this.onSale = onSale;
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

	public int getBasketAmount() {
		return basketAmount;
	}

	public void setBasketAmount(int basketAmount) {
		this.basketAmount = basketAmount;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public boolean isOnSale() {
		return onSale;
	}

	public void setOnSale(boolean onSale) {
		this.onSale = onSale;
	}

}
