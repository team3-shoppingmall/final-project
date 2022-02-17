package com.myspring.spring.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/spring")
public class ReviewController {
	private ReviewService reviewService;
	
	@Autowired
	public ReviewController(ReviewService reviewService) {
		this.reviewService = reviewService;
	}
	
	//리뷰 리스트보기
	@GetMapping("/review/list")
	public ResponseEntity<?> getAllreview() {
		return reviewService.getAllreviews();
	}
	
	//리뷰 작성
	@PostMapping("/review/insert")
	public ResponseEntity<?> insertReview(@RequestBody ReviewVO reviewVO) {
		return reviewService.insertReview(reviewVO);
	}
	
	//리뷰 삭제
	@DeleteMapping("/review/delete")
	public ResponseEntity<?> deleteReview(@RequestParam("id") String id) {
		return reviewService.deleteReview(id);
	}
	
	//리뷰 상세보기
	@GetMapping("/review/view/{reviewNo}")
	public ReviewVO getReviewFindByID(@PathVariable("reviewNo") int reviewNo) {
		return reviewService.getReviewFindByID(reviewNo);
	}
	
}
