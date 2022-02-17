package com.myspring.spring.qna;

import java.util.List;

import javax.swing.text.html.parser.ContentModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class QnaService {
	private QnaMapper qnaMapper;
	
	@Autowired
	public QnaService(QnaMapper qnaMapper) {
		this.qnaMapper = qnaMapper;
	}
	//문의 전체 조회
	public ResponseEntity<?> getQnaAll() {
		List<QnaVO> res = qnaMapper.getQnaAll();
		if(res == null)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	//카테고리별 조회
	public ResponseEntity<?> getQnaByType(String type) {
		List<QnaVO> res = qnaMapper.getQnaByType(type);
		if(res == null)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	//상품문의 카테고리 전체 조회
	public ResponseEntity<?> getQnaProductAll() {
		List<QnaVO> res = qnaMapper.getQnaProductAll();
		if(res == null)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	//배송문의 카테고리 전체 조회
	public ResponseEntity<?> getQnaDeliveryAll() {
		List<QnaVO> res = qnaMapper.getQnaDelieveryAll();
		if(res == null)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	//배송 전 변경&취소 카테고리 전체 조회
	public ResponseEntity<?> getQnaBeforeDeliveryAll() {
		List<QnaVO> res = qnaMapper.getQnaBeforeDeliveryAll();
		if(res == null)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}
			
	//배송 후 교환&반품 카테고리 전체 조회
	public ResponseEntity<?> getQnaAfterDeliveryAll() {
		List<QnaVO> res = qnaMapper.getQnaAfterDeliveryAll();
		if(res == null)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	//문의 등록
	public ResponseEntity<?> insertQna(QnaVO qnaVO) {
		int res = qnaMapper.insertQna(qnaVO);
		if(res == 0)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}
	//문의 수정
	public ResponseEntity<?> updateQna(int qnaNo, String type, String content, boolean secret, String image) {
		int res = qnaMapper.updateQna(qnaNo, type, content, secret, image);
		if(res == 0)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}
	//문의 삭제
	public ResponseEntity<?> deleteQna(int qnaNo) {
		int res = qnaMapper.deleteQna(qnaNo);
		if(res == 0)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	

	

	
}
