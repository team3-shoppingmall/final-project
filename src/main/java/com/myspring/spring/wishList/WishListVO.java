package com.myspring.spring.wishList;

public class WishListVO {
	private long wishListIdx;
	private String id;
	private int proudctNo;

	public WishListVO() {
	}

	public WishListVO(long wishListIdx, String id, int proudctNo) {
		this.wishListIdx = wishListIdx;
		this.id = id;
		this.proudctNo = proudctNo;
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

	public int getProudctNo() {
		return proudctNo;
	}

	public void setProudctNo(int proudctNo) {
		this.proudctNo = proudctNo;
	}

}
