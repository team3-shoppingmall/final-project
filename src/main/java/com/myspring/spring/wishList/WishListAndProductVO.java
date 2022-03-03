package com.myspring.spring.wishList;

public class WishListAndProductVO {
	private String id;
	private int productNo;
	private String productName;
	private String imageName;
	private int price;
	private int discount;
	private String color;
	private String size;
	private int amount;
	private Boolean onSale;

	public WishListAndProductVO() {
	}

	public WishListAndProductVO(String id, int productNo, String productName, String imageName, int price, int discount,
			String color, String size, int amount, Boolean onSale) {
		this.id = id;
		this.productNo = productNo;
		this.productName = productName;
		this.imageName = imageName;
		this.price = price;
		this.discount = discount;
		this.color = color;
		this.size = size;
		this.amount = amount;
		this.onSale = onSale;
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

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
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

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public Boolean getOnSale() {
		return onSale;
	}

	public void setOnSale(Boolean onSale) {
		this.onSale = onSale;
	}

}
