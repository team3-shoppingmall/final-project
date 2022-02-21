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
@RequestMapping(value = "/api/qna")
public class QnaController {
	private QnaService qnaService;

	@Autowired
	public QnaController(QnaService qnaService) {
		this.qnaService = qnaService;
	}

	// 전체 개수 가져오기
	@GetMapping("/getCount")
	public ResponseEntity<?> getCount(@RequestParam("search") String search,
			@RequestParam("searchWord") String searchWord) {
		return qnaService.getCount(search, searchWord);
	}

	// 문의게시판 목록 출력
	@GetMapping("/getQnaPage")
	public ResponseEntity<?> getQna(@RequestParam("page") int page, @RequestParam("perPage") int perPage,
			@RequestParam("search") String search, @RequestParam("searchWord") String searchWord) {
		return qnaService.getQnaWithSearch(page, perPage, search, searchWord);
	}

	// 문의 전체 조회
	@GetMapping("/getqnaAll")
	public ResponseEntity<?> getQnaAll() {
		return qnaService.getQnaAll();
	}

	// type별 문의 조회
	@GetMapping("/{type}")
	public ResponseEntity<?> getQnaByType(@PathVariable("type") String type) {
		return qnaService.getQnaByType(type);
	}

	// 상품문의 카테고리 전체 조회
	@GetMapping("/getproductAll")
	public ResponseEntity<?> getQnaProductAll() {
		return qnaService.getQnaProductAll();
	}

	// 배송 문의 카테고리 전체 조회
	@GetMapping("/getdeliveryAll")
	public ResponseEntity<?> getQnaDeliveryAll() {
		return qnaService.getQnaDeliveryAll();
	}

	// 배송 전 변경&취소 카테고리 전체 조회
	@GetMapping("/getbeforedeliveryAll")
	public ResponseEntity<?> getQnaBeforeDeliveryAll() {
		return qnaService.getQnaBeforeDeliveryAll();
	}

	// 배송 후 교환&반품 카테고리 전체 조회
	@GetMapping("/getafterdeliveryAll")
	public ResponseEntity<?> getQnaAfterDeliveryAll() {
		return qnaService.getQnaAfterDeliveryAll();
	}

	// 문의 등록 & 댓글 등록
	@PostMapping("/insertqna")
	public ResponseEntity<?> insertQna(@RequestBody QnaVO qnaVO) {
		return qnaService.insertQna(qnaVO);
	}

	//문의글 수정 시 댓글이 있으면 수정 불가
	// 문의 수정 & 댓글 수정
	@PatchMapping("/updateqna")
	public ResponseEntity<?> updateQna(@RequestParam("qnaNo") int qnaNo, @RequestParam("type") String type,
			@RequestParam("content") String content, @RequestParam("secret") boolean secret,
			@RequestParam("image") String image) {
		return qnaService.updateQna(qnaNo, type, content, secret, image);
	}

	// 문의 삭제 & 댓글 삭제
	// 댓글 삭제시 originalNo 없이 삭제할때
	// reply true -> false
	@DeleteMapping("/deleteqna")
	public ResponseEntity<?> deleteQna(@RequestParam("qnaNo") int qnaNo) {
		eturn qnaService.deleteQna(qnaNo);
	}

	// 아이디로 문의 검색
	@GetMapping("/searchQnaById")
	public ResponseEntity<?> searchQnaById(@RequestParam("id") String id) {
		return qnaService.searchQnaById(id);
	}

	// 내용으로 문의 검색
	@GetMapping("/searchQnaByContent")
	public ResponseEntity<?> searchQnaByContent(@RequestParam("content") String content) {
		return qnaService.searchQnaByContent(content);
	}
	
	
//	//기간으로 문의 검색(일주일)
//	@GetMapping("/qna/searchQnaByWeek") public ResponseEntity<?>
//	searchQnaByWeek(){ return qnaService.searchQnaByWeek(); }
//	  
//	//기간으로 문의 검색(한달)  
//	@GetMapping("/qna/searchQnaByMonth") public ResponseEntity<?>
//	searchQnaByMonth(){ return qnaService.searchQnaByMonth(); }
//	  
//	//기간으로 문의 검색(세달)
//	@GetMapping("/qna/searchQnaByMonths") public ResponseEntity<?>
//	searchQnaByMonths(){ return qnaService.searchQnaByMonths(); }
	 
}
