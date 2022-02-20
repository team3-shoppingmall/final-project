package com.myspring.spring.notice;

//페이징을 위한 클래스
public class NoticeCriteria {

	//현재 페이지 번호
	private int currentPageNo;
	
	//페이지당 출력할 데이터 개수
	private int recordsPerPage;
	
	//화면 하단에 출력할 페이지 사이즈
	private int pageSize;
	
	//검색 키워드
	private String searchKeyword;
	
	//검색 유형
	private String searchType;
	

	public int getCurrentPageNo() {
		return currentPageNo;
	}

	public void setCurrentPageNo(int currentPageNo) {
		this.currentPageNo = currentPageNo;
	}

	public int getRecordsPerPage() {
		return recordsPerPage;
	}

	public void setRecordsPerPage(int recordsPerPage) {
		this.recordsPerPage = recordsPerPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	
	//기본값 현재 페이지번호 1, 페이지당 데이터 10개, 하단에 출력할 페이지 개수 10
	public NoticeCriteria() {
		this.currentPageNo = 1;
		this.recordsPerPage = 10;
		this.pageSize = 10;
	}
	
	//MySQL에서 limit 구문의 앞부분에 사용되는 메서드
	public int getStartPage() {
		return (currentPageNo - 1) * recordsPerPage;
	}
	
}
