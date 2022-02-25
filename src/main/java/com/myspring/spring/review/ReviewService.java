package com.myspring.spring.review;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.myspring.spring.product.ProductMapper;

@Service
public class ReviewService {
	private ReviewMapper reviewMapper;
	private ProductMapper productMapper;

	
	@Autowired
	public ReviewService(ReviewMapper reviewMapper, ProductMapper productMapper) {
		 this.reviewMapper = reviewMapper;
	     this.productMapper = productMapper;
	}
		
	//리뷰 전체보기
	public ResponseEntity<?> getAllReviews(int page, int perPage, String search, String searchWord) {
		int start = (page - 1) * perPage;
		List<ReviewAndProductVO> reviewList = reviewMapper.getAllReviews(start, perPage, search, searchWord);
		int count = reviewMapper.getCount(search, searchWord);
		Map<String, Object> resMap = new HashMap<>();
		resMap.put("reviewList", reviewList);
		resMap.put("count", count);
		return new ResponseEntity<>(resMap, HttpStatus.OK);
	}
	
	//리뷰 상세보기
		public ResponseEntity<?> getReview(int reviewNo) {
			ReviewVO res = reviewMapper.getReview(reviewNo);
			if (res == null)
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
	public ResponseEntity<?> deleteReview(int reviewNo) {
		int res = reviewMapper.deleteReview(reviewNo);
		if(res == 0) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}else {
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}
	
	//리뷰 수정
	public ResponseEntity<?> updateReview(int reviewNo, String content, int star) {
		int res = reviewMapper.updateReview(reviewNo, content, star);
		
		if (res == 0)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	/*
	 * //리뷰 상세보기 public ResponseEntity<?> getFindByReviewNo(int reviewNo) {
	 * List<ReviewVO> res = reviewMapper.getFindByReviewNo(reviewNo); if(res ==
	 * null) { return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); }else
	 * { return new ResponseEntity<>(HttpStatus.OK); } }
	 */
}
