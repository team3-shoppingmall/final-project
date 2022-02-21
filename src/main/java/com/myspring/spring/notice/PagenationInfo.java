package com.myspring.spring.notice;

public class PagenationInfo {

	/** 페이징 계산에 필요한 파라미터들이 담긴 클래스 */
	private NoticeCommonVO noticeCommonVO;

	/** 전체 데이터 개수 */
	private int totalRecordCount;

	/** 전체 페이지 개수 */
	private int totalPageCount;

	/** 페이지 리스트의 첫 페이지 번호 */
	private int firstPage;

	/** 페이지 리스트의 마지막 페이지 번호 */
	private int lastPage;

	/** SQL의 조건절에 사용되는 첫 RNUM */
	private int firstRecordIndex;

	/** SQL의 조건절에 사용되는 마지막 RNUM */
//	private int lastRecordIndex; 오라클에서 이용

	/** 이전 페이지 존재 여부 */
	private boolean hasPreviousPage;

	/** 다음 페이지 존재 여부 */
	private boolean hasNextPage;

	public PagenationInfo(NoticeCommonVO noticeCommonVO) {
		if (noticeCommonVO.getCurrentPageNo() < 1) {
			noticeCommonVO.setCurrentPageNo(1);
		}
		if (noticeCommonVO.getRecordsPerPage() < 1 || noticeCommonVO.getRecordsPerPage() > 100) {
			noticeCommonVO.setRecordsPerPage(10);
		}
		if (noticeCommonVO.getPageSize() < 5 || noticeCommonVO.getPageSize() > 20) {
			noticeCommonVO.setPageSize(10);
		}

		this.noticeCommonVO = noticeCommonVO;
	}

	public void setTotalRecordCount(int totalRecordCount) {
		this.totalRecordCount = totalRecordCount;

		if (totalRecordCount > 0) {
			calculation();
		}
	}

	private void calculation() {

		/* 전체 페이지 수 (현재 페이지 번호가 전체 페이지 수보다 크면 현재 페이지 번호에 전체 페이지 수를 저장) */
		totalPageCount = ((totalRecordCount - 1) / noticeCommonVO.getRecordsPerPage()) + 1;
		if (noticeCommonVO.getCurrentPageNo() > totalPageCount) {
			noticeCommonVO.setCurrentPageNo(totalPageCount);
		}

		/* 페이지 리스트의 첫 페이지 번호 */
		firstPage = ((noticeCommonVO.getCurrentPageNo() - 1) / noticeCommonVO.getPageSize()) * noticeCommonVO.getPageSize() + 1;

		/* 페이지 리스트의 마지막 페이지 번호 (마지막 페이지가 전체 페이지 수보다 크면 마지막 페이지에 전체 페이지 수를 저장) */
		lastPage = firstPage + noticeCommonVO.getPageSize() - 1;
		if (lastPage > totalPageCount) {
			lastPage = totalPageCount;
		}

		/* SQL의 조건절에 사용되는 첫 RNUM */
		firstRecordIndex = (noticeCommonVO.getCurrentPageNo() - 1) * noticeCommonVO.getRecordsPerPage();

		/* SQL의 조건절에 사용되는 마지막 RNUM */
//		lastRecordIndex = noticeCommonVO.getCurrentPageNo() * noticeCommonVO.getRecordsPerPage();

		/* 이전 페이지 존재 여부 */
		hasPreviousPage = firstPage != 1;

		/* 다음 페이지 존재 여부 */
		hasNextPage = (lastPage * noticeCommonVO.getRecordsPerPage()) < totalRecordCount;
	}

	
}
