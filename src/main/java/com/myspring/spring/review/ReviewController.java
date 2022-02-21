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

import com.myspring.spring.notice.NoticeVO;

@RestController
@RequestMapping(value="/api/review")
public class ReviewController {
	private ReviewService reviewService;
	
	@Autowired
	public ReviewController(ReviewService reviewService) {
		this.reviewService = reviewService;
	}
	// 전체 개수 가져오기
	@GetMapping("/getCount")
	public ResponseEntity<?> getCount(@RequestParam("search") String search, @RequestParam("searchWord") String searchWord) {
		return reviewService.getCount(search, searchWord);
	}
	
	//리뷰 전체보기
	@GetMapping("/getReview")
	public ResponseEntity<?> getAllreviews(@RequestParam("page") int page, @RequestParam("perPage") int perPage,
			@RequestParam("search") String search, @RequestParam("searchWord") String searchWord) {
		return reviewService.getAllreviews(page, perPage, search, searchWord);
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
	public ResponseEntity<?> updateReview(@RequestBody ReviewVO reviewVO) {
		return reviewService.updateReview(reviewVO);
	}
	
	//리뷰 상세보기
	@GetMapping("/detail/{reviewNo}")
	public ResponseEntity<?> getReviewFindByID(@PathVariable("reviewNo") int reviewNo) {
		return reviewService.getFindByReviewNo(reviewNo);
	}
	
	// 리뷰 검색 by content
	@GetMapping("/searchByContent")
	public ResponseEntity<?> searchReviewByContent(@RequestParam("content") String content) {
		return reviewService.searchReviewByContent(content);
	}
	// 리뷰 검색 by id
	@GetMapping("/searchById")
	public ResponseEntity<?> searchReviewByid(@RequestParam("id") String id) {
		return reviewService.searchReviewByid(id);
	}
	// 리뷰 정렬 by star
}
