package com.myspring.spring.qna;

import java.sql.Date;

public class QnaVO {
	private int qnaNo;
	private int productNo;
	private String type;
	private int originalNo;
	private boolean reply;
	private String content;
	private String id;
	private Date regdate;
	private boolean secret;
	private String image;
	
	public QnaVO() {
		
	}
	
	public QnaVO(int qnaNo, int productNo, String type, int originalNo, boolean reply, String content, String id,
			Date regdate, boolean secret, String image) {
		super();
		this.qnaNo = qnaNo;
		this.productNo = productNo;
		this.type = type;
		this.originalNo = originalNo;
		this.reply = reply;
		this.content = content;
		this.id = id;
		this.regdate = regdate;
		this.secret = secret;
		this.image = image;
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
	public Date getRegdate() {
		return regdate;
	}
	public void setRegdate(Date regdate) {
		this.regdate = regdate;
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
}
