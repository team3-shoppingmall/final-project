package com.myspring.spring.notice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.myspring.spring.qna.QnaVO;

@Service
public class NoticeService {
	private NoticeMapper noticeMapper;

	@Autowired
	public NoticeService(NoticeMapper noticeMapper) {
		this.noticeMapper = noticeMapper;
	}

	// 전체 개수 가져오기
	public ResponseEntity<?> getCount(String search, String searchWord) {
		int res = noticeMapper.getCount(search, searchWord);
		if (res == 0)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}

//	// 공지사항 목록 출력
//	public List<NoticeVO> getAllMembers(int page, int perPage) {
//		int start = (page - 1) * perPage;
//		return noticeMapper.getAllMembers(start, perPage);
//	}

	// 공지사항 목록 출력
	public ResponseEntity<?> getNotice(NoticeVO noticeVO) {
		int start = (noticeVO.getPage() - 1) * noticeVO.getPerPage();
		noticeVO.setStart(start);
		List<NoticeVO> res = noticeMapper.getNotice(noticeVO);
		if (res == null)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// 공지사항 게시물 보기
	public ResponseEntity<?> getNoticeFindByID(NoticeVO noticeVO) {
		 NoticeVO res = noticeMapper.getNoticeFindByID(noticeVO);
		 
		 if (res == null)
			 return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		 else
			 return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// 공지사항 삭제
	public ResponseEntity<?> deleteNotice(int noticeNo) {
		int res = noticeMapper.deleteNotice(noticeNo);

		if (res == 0)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// 공지사항 게시물 작성
	public ResponseEntity<?> insertNotice(NoticeVO noticeVO) {
		int res = noticeMapper.insertNotice(noticeVO);
		
		if (res == 0)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}

//	// 공지사항 수정
//	public ResponseEntity<?> updateNotice(int noticeNo, String title, String content, String image) {
//		int res = noticeMapper.updateNotice( noticeNo, title, content, image);
//
//		if (res == 0)
//			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
//		else
//			return new ResponseEntity<>(res, HttpStatus.OK);
//	}
	
	public ResponseEntity<?> updateNotice(NoticeVO noticeVO) {
		int res = noticeMapper.updateNotice(noticeVO);
		
		if (res == 0)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// 게시글 페이징 출력
	public ResponseEntity<?> selectNoticeList(NoticeVO noticeVO) {
		List<NoticeVO> res = noticeMapper.selectNoticeList(noticeVO);
		
		if (res == null)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	// 게시판 테이블에서 검색 조건에 해당하는 게시글의 개수를 확인하기 위한 용도
	public ResponseEntity<?> selectNoticeTotalCount() {
		int res = noticeMapper.selectNoticeTotalCount();
		
		if (res == 0)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
}
