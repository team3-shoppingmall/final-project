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
	public ResponseEntity<?> getNotice(int page, int perPage, String search, String searchWord) {
		int start = (page - 1) * perPage;
		List<NoticeVO> res = noticeMapper.getNotice(start, perPage, search, searchWord);
		if (res == null)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// 공지사항 게시물 보기
	public NoticeVO getMemberFindByID(int noticeNo) {
		return noticeMapper.getMemberFindByID(noticeNo);
	}

	// 공지사항 삭제
	public ResponseEntity<?> deleteMember(int noticeNo) {
		int res = noticeMapper.deleteMember(noticeNo);

		if (res == 0)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// 공지사항 게시물 작성
	public ResponseEntity<?> insertMember(NoticeVO noticeVO) {
		return noticeMapper.insertMember(noticeVO);
	}

	// 공지사항 수정
	public ResponseEntity<?> updateMember(int noticeNo, String title, String content, String image) {
		int res = noticeMapper.updateMember(noticeNo, title, content, image);

		if (res == 0)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}

}
