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
	//리뷰 리스트 보기
	public ResponseEntity<?> getAllreviews() {
		List<ReviewVO> res = reviewMapper.getAllreviews();
		if(res == null)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}
	/*
	 * public ResponseEntity<?> insertReview(String content, String id, String
	 * image, int star) { int res = reviewMapper.insertReview(content, id, image,
	 * star); if(res == 0) { return new
	 * ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); }else { return new
	 * ResponseEntity<>(HttpStatus.OK); } }
	 */
	
	//리뷰 작성
	public ResponseEntity<?> insertReview(ReviewVO reviewVO) {
		return reviewMapper.insertReview(reviewVO);
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
	public ReviewVO getReviewFindByID(int reviewNo) {
		return reviewMapper.getReviewFindByID(reviewNo);
	}
	
}
