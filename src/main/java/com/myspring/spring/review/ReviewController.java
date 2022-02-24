package com.myspring.spring.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/api/review")
public class ReviewController {
	private ReviewService reviewService;
	
	@Autowired
	public ReviewController(ReviewService reviewService) {
		this.reviewService = reviewService;
	}
	
	//리뷰 전체보기
	@GetMapping("/getAllReviews")
	public ResponseEntity<?> getAllReviews(@RequestParam("page") int page, @RequestParam("perPage") int perPage,
			@RequestParam("search") String search, @RequestParam("searchWord") String searchWord) {
		return reviewService.getAllReviews(page, perPage, search, searchWord);
	}
	
	//리뷰 상세보기
	@GetMapping("/detail")
	public ResponseEntity<?> getReview(@RequestParam("reviewNo") int reviewNo){
		return reviewService.getReview(reviewNo);
	}
	
	//리뷰 작성
	@PostMapping("/insert")
	public ResponseEntity<?> insertReview(@RequestBody ReviewVO reviewVO) {
		return reviewService.insertReview(reviewVO);
	}
	
	//리뷰 삭제
	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteReview(@RequestParam("reviewNo") int reviewNo) {
		return reviewService.deleteReview(reviewNo);
	}
	
	//리뷰 수정
	@PatchMapping("/update")
	public ResponseEntity<?> updateReview(@RequestParam("reviewNo") int reviewNo, @RequestParam("content") String content, @RequestParam("star") int star) {
//		@RequestParam("image") String image, 
		return reviewService.updateReview(reviewNo, content, star);
	}
	
	//리뷰 상세보기
	/*
	 * @GetMapping("/detail/{reviewNo}") public ResponseEntity<?>
	 * getReviewFindByID(@PathVariable("reviewNo") int reviewNo) { return
	 * reviewService.getFindByReviewNo(reviewNo); }
	 */

	// 리뷰 정렬 by star
}
