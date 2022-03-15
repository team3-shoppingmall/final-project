package com.myspring.spring.review;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

	// 리뷰 전체보기
	public ResponseEntity<?> getAllReviews(int page, int perPage, String search, String searchWord, int productNo,
			String id) {
		int start = (page - 1) * perPage;
		List<ReviewAndProductVO> reviewList = reviewMapper.getReivewList(start, perPage, search, searchWord, productNo,
				id);
		int count = reviewMapper.getReviewCount(search, searchWord, productNo, id);
		Map<String, Object> resMap = new HashMap<>();
		resMap.put("reviewList", reviewList);
		resMap.put("count", count);
		return new ResponseEntity<>(resMap, HttpStatus.OK);
	}

	// 리뷰 상세보기
	public ResponseEntity<?> getReview(int reviewNo) {
		ReviewVO res = reviewMapper.getReview(reviewNo);
		if (res == null)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// 리뷰 작성
	public ResponseEntity<?> insertReview(ReviewVO requestData, List<MultipartFile> fileList) {
		ReviewVO result = new ReviewVO();
		ResponseEntity<?> entity = null;

		try {
			reviewMapper.insertReview(requestData, result);

			int reviewNo = result.getReviewNo();
			File file = new File("./images/review/" + reviewNo + "/");
			file.mkdir();

			if (fileList != null) {
				MultipartFile multipartFile = fileList.get(0);
				FileOutputStream writer = new FileOutputStream(
						"./images/review/" + reviewNo + "/" + multipartFile.getOriginalFilename());
				writer.write(multipartFile.getBytes());
				writer.close();
			}
			entity = new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return entity;
	}

	// 리뷰 삭제
	public ResponseEntity<?> deleteReview(int reviewNo) {
		ResponseEntity<?> entity = null;
		
		try {
			int res = reviewMapper.deleteReview(reviewNo);
			if(res == 0) 
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				File file;
				File[] underDir;
				// 폴더 내 모든 파일 삭제
				file = new File("./images/review/" + reviewNo + "/");
				file.mkdir();
				underDir = file.listFiles();
				if (underDir != null) {
					for (int i = 0; i < underDir.length; i++) {
						underDir[i].delete();
					}
				}
				entity = new ResponseEntity<>(HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
				entity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return entity;
	}

	// 리뷰 수정
	public ResponseEntity<?> updateReview(ReviewVO requestData, List<MultipartFile> fileList) {
		ResponseEntity<?> entity = null;

		try {
			int res = reviewMapper.updateReview(requestData);
			if (res == 0) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			File file;
			File[] underDir;

			// 폴더 내 모든 파일 삭제
			file = new File("./images/review/" + requestData.getReviewNo() + "/");
			file.mkdir();
			underDir = file.listFiles();
			if (underDir != null) {
				for (int i = 0; i < underDir.length; i++) {
					underDir[i].delete();
				}
			}
			if (fileList != null) {
				MultipartFile multipartFile = fileList.get(0);
				FileOutputStream writer = new FileOutputStream(
						"./images/review/" + requestData.getReviewNo() + "/" + multipartFile.getOriginalFilename());
				writer.write(multipartFile.getBytes());
				writer.close();
			}
			entity = new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return entity;
	}
}
