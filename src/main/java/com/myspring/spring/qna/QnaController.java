package com.myspring.spring.qna;

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
@RequestMapping(value="/api")
public class QnaController {
	private QnaService qnaService;
	
	@Autowired
	public QnaController(QnaService qnaService){
		this.qnaService = qnaService;
	}
	//문의 전체 조회
	@GetMapping("/qnalist/all")
	public ResponseEntity<?> getQnaAll(){
		return qnaService.getQnaAll();
	}
	
	//type별 문의 조회
	@GetMapping("/qnalist/{type}")
	public ResponseEntity<?> getQnaByType(@PathVariable("type") String type) {
		return qnaService.getQnaByType(type);
	}
	
	//상품문의 카테고리 전체 조회
	@GetMapping("/qnalist/productAll")
	public ResponseEntity<?> getQnaProductAll() {
		return qnaService.getQnaProductAll();
	}	
	
	//배송 문의 카테고리 전체 조회
	@GetMapping("/qnalist/deliveryAll")
	public ResponseEntity<?> getQnaDeliveryAll(){
		return qnaService.getQnaDeliveryAll();
	}
	
	//배송 전 변경&취소 카테고리 전체 조회
	@GetMapping("/qnalist/beforedeliveryAll")
	public ResponseEntity<?> getQnaBeforeDeliveryAll() {
		return qnaService.getQnaBeforeDeliveryAll();
	}
	
	//배송 후 교환&반품 카테고리 전체 조회
	@GetMapping("/qnalist/afterdeliveryAll")
	public ResponseEntity<?> getQnaAfterDeliveryAll() {
		return qnaService.getQnaAfterDeliveryAll();
	}

	//문의 등록
	@PostMapping("/qnalist/insertqna")
	public ResponseEntity<?> insertQna(@RequestBody QnaVO qnaVO){
		return qnaService.insertQna(qnaVO);
	}
	
	//문의 수정
	@PatchMapping("/qnalist/updateqna")
	public ResponseEntity<?> updateQna(@RequestParam("qnaNo") int qnaNo,
									   @RequestParam("type") String type,
									   @RequestParam("content") String content,
									   @RequestParam("secret") boolean secret,
									   @RequestParam("image") String image){
		return qnaService.updateQna(qnaNo, type, content, secret, image);
	}
	
	//문의 삭제
	@DeleteMapping("/qnalist/deleteqna")
	public ResponseEntity<?> deleteQna(@RequestParam("qnaNo") int qnaNo){
		return qnaService.deleteQna(qnaNo);
	}
	

}
