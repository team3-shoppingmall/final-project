package com.myspring.spring.point;

import java.util.Date;

public class PointVO {
	private int num;
	private String id;
	private int point;
	private String content;
	private Date pointDate;

	public PointVO(int num, String id, int point, Date potinDate) {
		this.num = num;
		this.id = id;
		this.point = point;
		this.content = content;
		this.pointDate = potinDate;
	}


	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}
    
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getPointDate() {
		return pointDate;
	}

	public void setPointDate(Date pointDate) {
		this.pointDate = pointDate;
	}

	public PointVO() {
	}

	

}
