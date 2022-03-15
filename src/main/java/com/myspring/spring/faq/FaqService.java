package com.myspring.spring.faq;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class FaqService {
	private FaqMapper faqMapper;

	@Autowired
	public FaqService(FaqMapper faqMapper) {
		this.faqMapper = faqMapper;
	}

	// 전체 조회
	public ResponseEntity<?> getFaqBySearch(String searchWord) {
		List<FaqVO> res = faqMapper.getFaqBySearch(searchWord);
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// 카테고리별 조회
	public ResponseEntity<?> getFaqByType(String type, int limit) {
		List<FaqVO> faqList = null;
		int count = 0;
		if (type.equals("all")) {
			faqList = faqMapper.getFaqAll(limit);
			count = faqMapper.getFaqCountAll();
		} else {
			faqList = faqMapper.getFaqByType(type, limit);
			count = faqMapper.getFaqCountByType(type);
		}
		Map<String, Object> resMap = new HashMap<>();
		resMap.put("faqList", faqList);
		resMap.put("count", count);
		return new ResponseEntity<>(resMap, HttpStatus.OK);
	}

	// 수정시 데이터 불러오기 (하나만 조회)
	public ResponseEntity<?> getFaqByFaqNo(int faqNo) {
		FaqVO res = faqMapper.getFaqByFaqNo(faqNo);
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
