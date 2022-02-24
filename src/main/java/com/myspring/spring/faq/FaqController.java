package com.myspring.spring.faq;

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
@RequestMapping(value = "/api/faq")
public class FaqController {
	private FaqService faqService;
	
	@Autowired
	public FaqController(FaqService faqService) {
		this.faqService = faqService;
	}
	//전체 조회
    @GetMapping("/getAll")
    public ResponseEntity<?> getFaqAll() {
    	return faqService.getFaqAll();
	}
    
    //type별 문의 조회
    @GetMapping("/get/{type}")
    public ResponseEntity<?> getFaqByType(@PathVariable("type") String type) {
    	return faqService.getFaqByType(type);
    }
    //수정시 데이터 불러오기 (하나만 조회)
	@GetMapping("/getfaqbyfaqNo")
	public ResponseEntity<?> getFaqByFaqNo(@RequestParam("faqNo") int faqNo){
		return faqService.getFaqByFaqNo(faqNo);
	}
    
    //faq 등록
    @PostMapping("/insertfaq")
    public ResponseEntity<?> insertFaq(@RequestBody FaqVO faqVO){
    	return faqService.insertFaq(faqVO);
    }
    
    //faq 수정
    @PatchMapping("/updatefaq")
    public ResponseEntity<?> updateFaq(@RequestParam("faqNo") int faqNo,
    		@RequestParam("type") String type, @RequestParam("title") String title,
    		@RequestParam("content") String content )  {
    	return faqService.updateFaq(faqNo, type, title, content);
    }
    
    //faq 삭제
    @DeleteMapping("/deletefaq")
    public ResponseEntity<?> deleteFaq(@RequestParam("faqNo") int faqNo) {
    	return faqService.deleteFaq(faqNo);
    }
}
