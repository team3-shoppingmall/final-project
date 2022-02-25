package com.myspring.spring.review;

import java.util.Date;

public class ReviewAndProductVO {
		private int reviewNo;
		private int productNo;
		private String content;
		private String id;
		private Date regDate;
		private String image;
		private int star;
		private String productName;
		
		public ReviewAndProductVO() {
		}

		public ReviewAndProductVO(int reviewNo, int productNo, String content, String id, Date regDate, String image,
				int star, String productName) {
			this.reviewNo = reviewNo;
			this.productNo = productNo;
			this.content = content;
			this.id = id;
			this.regDate = regDate;
			this.image = image;
			this.star = star;
			this.productName = productName;
		}

		public int getReviewNo() {
			return reviewNo;
		}

		public void setReviewNo(int reviewNo) {
			this.reviewNo = reviewNo;
		}

		public int getProductNo() {
			return productNo;
		}

		public void setProductNo(int productNo) {
			this.productNo = productNo;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public Date getRegDate() {
			return regDate;
		}

		public void setRegDate(Date regDate) {
			this.regDate = regDate;
		}

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
		}

		public int getStar() {
			return star;
		}

		public void setStar(int star) {
			this.star = star;
		}

		public String getProductName() {
			return productName;
		}

		public void setProductName(String productName) {
			this.productName = productName;
		}

		@Override
		public String toString() {
			return "ReviewAndProductVO [reviewNo=" + reviewNo + ", productNo=" + productNo + ", content=" + content
					+ ", id=" + id + ", regDate=" + regDate + ", image=" + image + ", star=" + star + ", productName="
					+ productName + "]";
		}
		
		
		
		
		
		
}
