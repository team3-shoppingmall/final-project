package com.myspring.spring.banner;

public class BannerVO {
	private int num;
	private String image;
	private String link;

	public BannerVO() {
	}

	public BannerVO(int num, String image, String link) {
		this.num = num;
		this.image = image;
		this.link = link;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

}
