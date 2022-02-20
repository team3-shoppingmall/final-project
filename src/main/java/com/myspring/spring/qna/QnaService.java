package com.myspring.spring.qna;

import java.util.List;

import javax.swing.text.html.parser.ContentModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.myspring.spring.notice.NoticeVO;

@Service
public class QnaService {
	private QnaMapper qnaMapper;

	@Autowired
	public QnaService(QnaMapper qnaMapper) {
		this.qnaMapper = qnaMapper;
	}

	// 전체 개수 가져오기
	public ResponseEntity<?> getCount(String search, String searchWord) {
		int res = qnaMapper.getCount(search, searchWord);
		if (res == 0)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// 문의게시판 목록 출력
	public ResponseEntity<?> getQna(int page, int perPage, String search, String searchWord) {
		int start = (page - 1) * perPage;
		List<NoticeVO> res = qnaMapper.getQna(start, perPage, search, searchWord);
		if (res == null)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// 문의 전체 조회
	public ResponseEntity<?> getQnaAll() {
		List<QnaVO> res = qnaMapper.getQnaAll();
		if (res == null)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// 카테고리별 조회
	public ResponseEntity<?> getQnaByType(String type) {
		List<QnaVO> res = qnaMapper.getQnaByType(type);
		if (res == null)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// 상품문의 카테고리 전체 조회
	public ResponseEntity<?> getQnaProductAll() {
		List<QnaVO> res = qnaMapper.getQnaProductAll();
		if (res == null)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// 배송문의 카테고리 전체 조회
	public ResponseEntity<?> getQnaDeliveryAll() {
		List<QnaVO> res = qnaMapper.getQnaDelieveryAll();
		if (res == null)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// 배송 전 변경&취소 카테고리 전체 조회
	public ResponseEntity<?> getQnaBeforeDeliveryAll() {
		List<QnaVO> res = qnaMapper.getQnaBeforeDeliveryAll();
		if (res == null)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// 배송 후 교환&반품 카테고리 전체 조회
	public ResponseEntity<?> getQnaAfterDeliveryAll() {
		List<QnaVO> res = qnaMapper.getQnaAfterDeliveryAll();
		if (res == null)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// 문의 등록 & 댓글 등록 (댓글 등록시 originalNo의 문의글의 reply = true)
	public ResponseEntity<?> insertQna(QnaVO qnaVO) {
		int resQna = qnaMapper.insertQna(qnaVO);
		int originalNo = qnaVO.getOriginalNo();
		int resReply = qnaMapper.updateReplyTrue(originalNo);

		if (originalNo != 0) {
			if (resReply == 0)
				return new ResponseEntity<>(resReply, HttpStatus.INTERNAL_SERVER_ERROR);
			else
				return new ResponseEntity<>(resReply, HttpStatus.OK);
		} else {
			if (resQna == 0)
				return new ResponseEntity<>(resQna, HttpStatus.INTERNAL_SERVER_ERROR);
			else
				return new ResponseEntity<>(resQna, HttpStatus.OK);
		}
	}

	// 문의 수정 & 댓글 수정
	public ResponseEntity<?> updateQna(int qnaNo, String type, String content, boolean secret, String image) {
		int res = qnaMapper.updateQna(qnaNo, type, content, secret, image);
		if (res == 0)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// 문의 삭제 & 댓글 삭제
	public ResponseEntity<?> deleteQna(int qnaNo) {
		// OriginalNo 있으면 답글, 없으면 원글
		QnaVO res = qnaMapper.getQna(qnaNo);

		// 문의 삭제
		int resQna = qnaMapper.deleteQna(qnaNo);

		int resReply;
		if (res.getOriginalNo() != 0) {
			resReply = qnaMapper.updateReplyFalse(res.getOriginalNo());
			if (resReply == 0)
				return new ResponseEntity<>(resReply, HttpStatus.INTERNAL_SERVER_ERROR);
			else
				return new ResponseEntity<>(resReply, HttpStatus.OK);
		}
			
		if (resQna == 0)
			return new ResponseEntity<>(resQna, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(resQna, HttpStatus.OK);
	}

	// 아이디로 문의 검색
	public ResponseEntity<?> searchQnaById(String id) {
		List<QnaVO> res = qnaMapper.searchQnaById(id);
		if (res == null)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// 내용으로 문의 검색
	public ResponseEntity<?> searchQnaByContent(String content) {
		List<QnaVO> res = qnaMapper.searchQnaByContent(content);
		if (res == null)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}

//	//기간으로 문의 검색(일주일)
//	public ResponseEntity<?> searchQnaByWeek() {
//		List<QnaVO> res = qnaMapper.searchQnaByWeek();
//		if(res == null)
//			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
//		else
//			return new ResponseEntity<>(res, HttpStatus.OK);
//	}
//	
//	//기간으로 문의 검색(한달)
//	public ResponseEntity<?> searchQnaByMonth() {
//		List<QnaVO> res = qnaMapper.searchQnaByMonth();
//		if(res == null)
//			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
//		else
//			return new ResponseEntity<>(res, HttpStatus.OK);
//	}
//	//기간으로 문의 검색(세달)
//	public ResponseEntity<?> searchQnaByMonths() {
//		List<QnaVO> res = qnaMapper.searchQnaByMonths();
//		if(res == null)
//			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
//		else
//			return new ResponseEntity<>(res, HttpStatus.OK);
//	}

}
