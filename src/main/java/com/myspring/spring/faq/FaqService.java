package com.myspring.spring.faq;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.myspring.spring.qna.QnaVO;

@Service
public class FaqService {
	private FaqMapper faqMapper;

	@Autowired
	public FaqService(FaqMapper faqMapper) {
		this.faqMapper = faqMapper;
	}

	// 전체 조회
	public ResponseEntity<?> getFaqAll() {
		List<FaqVO> res = faqMapper.getFaqAll();
		if (res == null)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// 카테고리별 조회
	public ResponseEntity<?> getFaqByType(String type) {
		List<FaqVO> res = null;
		if (type.equals("all")) {
			res = faqMapper.getFaqAll();
		} else {
			res = faqMapper.getFaqByType(type);
		}

		if (res == null)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}
    //수정시 데이터 불러오기 (하나만 조회)
	public ResponseEntity<?> getFaqByFaqNo(int faqNo) {
		System.out.println(faqNo);
		FaqVO res = faqMapper.getFaqByFaqNo(faqNo);
		if (res == null)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}
	// faq 등록
	public ResponseEntity<?> insertFaq(FaqVO faqVO) {
		int res = faqMapper.insertFaq(faqVO);
		if (res == 0)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// faq 수정
	public ResponseEntity<?> updateFaq(int faqNo, String type, String title, String content) {
		int res = faqMapper.updateFaq(faqNo, type, title, content);
		if (res == 0)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// faq 삭제
	public ResponseEntity<?> deleteFaq(int faqNo) {
		int res = faqMapper.deleteFaq(faqNo);
		if (res == 0)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}

	

}
