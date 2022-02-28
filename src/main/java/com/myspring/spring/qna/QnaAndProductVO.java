package com.myspring.spring.qna;

import java.util.Date;

public class QnaAndProductVO {
	private int qnaNo;
	private int productNo;
	private String type;
	private int originalNo;
	private boolean reply;
	private String content;
	private String id;
	private Date regDate;
	private boolean secret;
	private String image;
	private String productName;
	
	
	public QnaAndProductVO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public QnaAndProductVO(int qnaNo, int productNo, String type, int originalNo, boolean reply, String content, String id,
			Date regDate, boolean secret, String image, String productName) {
		super();
		this.qnaNo = qnaNo;
		this.productNo = productNo;
		this.type = type;
		this.originalNo = originalNo;
		this.reply = reply;
		this.content = content;
		this.id = id;
		this.regDate = regDate;
		this.secret = secret;
		this.image = image;
		this.productName = productName;
	}
	
	public int getQnaNo() {
		return qnaNo;
	}
	public void setQnaNo(int qnaNo) {
		this.qnaNo = qnaNo;
	}
	public int getProductNo() {
		return productNo;
	}
	public void setProductNo(int productNo) {
		this.productNo = productNo;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getOriginalNo() {
		return originalNo;
	}
	public void setOriginalNo(int originalNo) {
		this.originalNo = originalNo;
	}
	public boolean isReply() {
		return reply;
	}
	public void setReply(boolean reply) {
		this.reply = reply;
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
	public boolean isSecret() {
		return secret;
	}
	public void setSecret(boolean secret) {
		this.secret = secret;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}

	
}
