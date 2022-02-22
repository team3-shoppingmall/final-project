package com.myspring.spring.banner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/member")
public class BannerController {
	private BannerService productService;

	@Autowired
	public BannerController(BannerService productService) {
		this.productService = productService;
	}
	
	// 멤버 등록
	@PostMapping(value = "/insert")
	public ResponseEntity<?> insertMember(@RequestBody BannerVO member) {
		productService.insertMember(member);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	// 전체 멤버 조회
	@GetMapping(value = "/getAllmembers")
	public ResponseEntity<?> getAllmembers() {
		return 	productService.getAllmembers();
	}
	
	// 아이디로 멤버 조회
	@GetMapping(value = "/getMember/{id}")
	public ResponseEntity<?> getMember(@PathVariable("id") String id) {
		return 	productService.getMember(id);
	}
}
