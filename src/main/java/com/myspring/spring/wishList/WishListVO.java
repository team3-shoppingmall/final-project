package com.myspring.spring.wishList;

public class WishListVO {
	private long wishListIdx;
	private String id;
	private int productNo;

	public WishListVO() {
	}

	public WishListVO(long wishListIdx, String id, int productNo) {
		this.wishListIdx = wishListIdx;
		this.id = id;
		this.productNo = productNo;
	}

	public long getWishListIdx() {
		return wishListIdx;
	}

	public void setWishListIdx(long wishListIdx) {
		this.wishListIdx = wishListIdx;
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
