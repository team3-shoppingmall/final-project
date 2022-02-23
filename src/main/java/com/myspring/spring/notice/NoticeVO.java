package com.myspring.spring.notice;

public class NoticeVO {

	private int noticeNo; 
	private String title;
	private String content;
	private String id;
	private String image;
	
	
//	public NoticeVO(int noticeNo, String title, String content, String id, String image) {
//		super();
//		this.noticeNo = noticeNo;
//		this.title = title;
//		this.content = content;
//		this.id = id;
//		this.image = image;
//	}
	
	
	public int getNoticeNo() {
		return noticeNo;
	}
	public void setNoticeNo(int noticeNo) {
		this.noticeNo = noticeNo;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
	@Override
	public String toString() {
		return "NoticeVO [noticeNo=" + noticeNo + ", title=" + title + ", content=" + content + ", id=" + id
				+ ", image=" + image + "]";
	}
	
	
}
