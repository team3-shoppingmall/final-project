package com.myspring.spring.wishList;

public class WishListVO {
	private String id;
	private int productNo;

	public WishListVO() {
	}

	public WishListVO(String id, int productNo) {
		this.id = id;
		this.productNo = productNo;
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

}
