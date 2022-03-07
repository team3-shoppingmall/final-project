package com.myspring.spring.review;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.myspring.spring.product.ProductVO;

@RestController
@RequestMapping(value = "/api/review")
public class ReviewController {
	private ReviewService reviewService;

	@Autowired
	public ReviewController(ReviewService reviewService) {
		this.reviewService = reviewService;
	}

	// 리뷰 전체보기
	@GetMapping("/getAllReviews")
	public ResponseEntity<?> getAllReviews(@RequestParam("page") int page, @RequestParam("perPage") int perPage,
			@RequestParam("search") String search, @RequestParam("searchWord") String searchWord,
			@RequestParam(value = "productNo", required = false) int productNo,
			@RequestParam(value = "id", required = false) String id) {
		return reviewService.getAllReviews(page, perPage, search, searchWord, productNo, id);
	}

	// 리뷰 상세보기
	@GetMapping("/getReview/{reviewNo}")
	public ResponseEntity<?> getReview(@PathVariable("reviewNo") int reviewNo) {
		return reviewService.getReview(reviewNo);
	}

	// 리뷰 작성
	@PostMapping("/insert")
	public ResponseEntity<?> insertReview(@RequestPart(value = "data") ReviewVO requestData,
			@RequestParam(value = "fileList", required = false) List<MultipartFile> filList) throws NotFoundException {
		return reviewService.insertReview(requestData, filList);
	}

	// 리뷰 삭제
	@DeleteMapping("/delete/{reviewNo}")
	public ResponseEntity<?> deleteReview(@PathVariable("reviewNo") int reviewNo) {
		return reviewService.deleteReview(reviewNo);
	}

	// 리뷰 수정
	@PatchMapping("/update")
	public ResponseEntity<?> updateReview(@RequestPart(value = "data") ReviewVO requestData,
			@RequestParam(value = "fileList", required = false) List<MultipartFile> fileList) throws NotFoundException {
		return reviewService.updateReview(requestData, fileList);
	}

	// 서버에서 이미지 가져오기
	@GetMapping("/reviewImage/{reviewNo}/{image}")
	public ResponseEntity<?> productimage(@PathVariable("reviewNo") int reviewNo, @PathVariable("image") String image)
			throws IOException {
		InputStream imageStream;
		try {
			imageStream = new FileInputStream("./images/review/" + reviewNo + "/" + image);
		} catch (FileNotFoundException e) {
			imageStream = new FileInputStream("./images/error.png");
		}
		byte[] imageByteArray = IOUtils.toByteArray(imageStream);
		imageStream.close();
		return new ResponseEntity<byte[]>(imageByteArray, HttpStatus.OK);
	}
}
