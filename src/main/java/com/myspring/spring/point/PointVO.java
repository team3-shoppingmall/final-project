package com.myspring.spring.point;

import java.util.Date;

public class PointVO {
	private long num;
	private String id;
	private int point;
	private Date potinDate;

	public PointVO() {
	}

	public PointVO(long num, String id, int point, Date potinDate) {
		this.num = num;
		this.id = id;
		this.point = point;
		this.potinDate = potinDate;
	}

	public long getNum() {
		return num;
	}

	public void setNum(long num) {
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

	public Date getPotinDate() {
		return potinDate;
	}

	public void setPotinDate(Date potinDate) {
		this.potinDate = potinDate;
	}

}
