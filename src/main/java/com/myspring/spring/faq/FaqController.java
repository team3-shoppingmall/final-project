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
@RequestMapping(value = "/api")
public class FaqController {
	private FaqService faqService;
	
	@Autowired
	public FaqController(FaqService faqService) {
		this.faqService = faqService;
	}
	//전체 조회
    @GetMapping("/faqlist/all")
    public ResponseEntity<?> getFaqAll() {
    	return faqService.getFaqAll();
	}
    
    //type별 문의 조회
    @GetMapping("/faqlist/{type}")
    public ResponseEntity<?> getFaqByType(@PathVariable("type") String type) {
    	return faqService.getFaqByType(type);
    }

    //상품관련 전체 조회
    @GetMapping("/faqlist/productAll")
    public ResponseEntity<?> getFaqProductAll() {
    	return faqService.getFaqProductAll();
    }
    
    //배송관련 전체 조회
    @GetMapping("/faqlist/deliveryAll")
    public ResponseEntity<?> getFaqDeliveryAll() {
    	return faqService.getFaqDeliveryAll();
    }
    
    //교환/반품관련
    @GetMapping("/faqlist/exchangeAll")
    public ResponseEntity<?> getFaqExchangeAll() {
    	return faqService.getFaqExchangeAll();
    }
    
    //기타관련 조회
    @GetMapping("/faqlist/etcAll")
    public ResponseEntity<?> getFaqEtcAll() {
    	return faqService.getFaqEtcAll();
    }
    
    //faq 등록
    @PostMapping("/faqlist/insertfaq")
    public ResponseEntity<?> insertFaq(@RequestBody FaqVO faqVO){
    	return faqService.insertFaq(faqVO);
    }
    
    //faq 수정
    @PatchMapping("/faqlist/updatefaq")
    public ResponseEntity<?> updateFaq(@RequestParam("faqNo") int faqNo,
    		@RequestParam("type") String type, @RequestParam("title") String title,
    		@RequestParam("content") String content) {
    	return faqService.updateFaq(faqNo, type, title, content);
    }
    
    //faq 삭제
    @DeleteMapping("/faqlist/deletefaq")
    public ResponseEntity<?> deleteFaq(@RequestParam("faqNo") int faqNo) {
    	return faqService.deleteFaq(faqNo);
    }
}
