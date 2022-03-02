package com.myspring.spring.order;

import java.util.Date;

public class OrderVO {
	private long orderIdx;
	private String id;
	private int productNo;
	private long orderNo;
	private String selectedColor;
	private String selectedSize;
	private int orderAmount;
	private int totalPrice;
	private Date orderDate;
	private String state;
	private String orderMethod;
	private String name;
	private String tel;
	private String zipCode;
	private String address;
	private String detailAddr;

	public OrderVO() {
	}

	public OrderVO(long orderIdx, String id, int productNo, long orderNo, String selectedColor, String selectedSize,
			int orderAmount, int totalPrice, Date orderDate, String state, String orderMethod, String name, String tel,
			String zipCode, String address, String detailAddr) {
		this.orderIdx = orderIdx;
		this.id = id;
		this.productNo = productNo;
		this.orderNo = orderNo;
		this.selectedColor = selectedColor;
		this.selectedSize = selectedSize;
		this.orderAmount = orderAmount;
		this.totalPrice = totalPrice;
		this.orderDate = orderDate;
		this.state = state;
		this.orderMethod = orderMethod;
		this.name = name;
		this.tel = tel;
		this.zipCode = zipCode;
		this.address = address;
		this.detailAddr = detailAddr;
	}

	public long getOrderIdx() {
		return orderIdx;
	}

	public void setOrderIdx(long orderIdx) {
		this.orderIdx = orderIdx;
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

	public long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
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

	public String getOrderMethod() {
		return orderMethod;
	}

	public void setOrderMethod(String orderMethod) {
		this.orderMethod = orderMethod;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDetailAddr() {
		return detailAddr;
	}

	public void setDetailAddr(String detailAddr) {
		this.detailAddr = detailAddr;
	}

}
