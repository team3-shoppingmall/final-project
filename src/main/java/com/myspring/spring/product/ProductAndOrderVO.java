package com.myspring.spring.product;

import java.util.Date;

public class ProductAndOrderVO {
	private int productNo;
	private String productName;
	private String type1;
	private String type2;
	private String imageName;
	private int price;
	private int discount;
	private String color;
	private String size;
	private int amount;
	private Date regDate;
	private String detailImageName;
	private Boolean onSale;
	private int orderAmount;
	private int totalPrice;
	private Date orderDate;
	private String state;

	public ProductAndOrderVO() {
		super();
	}

	public ProductAndOrderVO(int productNo, String productName, String type1, String type2, String imageName, int price,
			int discount, String color, String size, int amount, Date regDate, String detailImageName, Boolean onSale,
			int orderAmount, int totalPrice, Date orderDate, String state) {
		this.productNo = productNo;
		this.productName = productName;
		this.type1 = type1;
		this.type2 = type2;
		this.imageName = imageName;
		this.price = price;
		this.discount = discount;
		this.color = color;
		this.size = size;
		this.amount = amount;
		this.regDate = regDate;
		this.detailImageName = detailImageName;
		this.onSale = onSale;
		this.orderAmount = orderAmount;
		this.totalPrice = totalPrice;
		this.orderDate = orderDate;
		this.state = state;
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

	public String getType1() {
		return type1;
	}

	public void setType1(String type1) {
		this.type1 = type1;
	}

	public String getType2() {
		return type2;
	}

	public void setType2(String type2) {
		this.type2 = type2;
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

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public String getDetailImageName() {
		return detailImageName;
	}

	public void setDetailImageName(String detailImageName) {
		this.detailImageName = detailImageName;
	}

	public Boolean getOnSale() {
		return onSale;
	}

	public void setOnSale(Boolean onSale) {
		this.onSale = onSale;
	}

	public int getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(int orderAmount) {
		this.orderAmount = orderAmount;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
