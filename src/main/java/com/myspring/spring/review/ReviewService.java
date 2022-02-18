package com.myspring.spring.review;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
private ReviewMapper reviewMapper;
	
	@Autowired
	public ReviewService(ReviewMapper reviewMapper) {
		this.reviewMapper = reviewMapper;
	}
	
	// 전체 개수 가져오기
	public ResponseEntity<?> getCount(String search, String searchWord) {
		int res = reviewMapper.getCount(search, searchWord);
		if (res == 0)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}
		
	//리뷰 전체보기
	public ResponseEntity<?> getAllreviews(int page, int perPage, String search, String searchWord) {
		int start = (page - 1) * perPage;
		List<ReviewVO> res = reviewMapper.getAllreviews(start, perPage, search, searchWord);
		if(res == null)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	//리뷰 작성
	public ResponseEntity<?> insertReview(ReviewVO reviewVO) {
		int res = reviewMapper.insertReview(reviewVO);
		if(res == 0) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}else {
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}
	
	//리뷰 삭제
	public ResponseEntity<?> deleteReview(String id) {
		int res = reviewMapper.deleteReview(id);
		if(res == 0) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}else {
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}
	
	//리뷰 상세보기
	public ResponseEntity<?> getFindByReviewNo(int reviewNo) {
		List<ReviewVO> res = reviewMapper.getFindByReviewNo(reviewNo);
		if(res == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}else {
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}
	//리뷰검색 by content
	public ResponseEntity<?> searchReviewByContent(String content) {
		List<ReviewVO> res = reviewMapper.searchReviewByContent(content);
		if(res == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}else {
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}
	//리뷰검색 by id
		public ResponseEntity<?> searchReviewByid(String id) {
			List<ReviewVO> res = reviewMapper.searchReviewById(id);
			if(res == null) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}else {
				return new ResponseEntity<>(HttpStatus.OK);
			}
		}
}
