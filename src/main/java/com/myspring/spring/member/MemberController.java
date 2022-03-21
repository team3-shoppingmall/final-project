package com.myspring.spring.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/api/member")
public class MemberController {
	private MemberService memberService;

	@Autowired
	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}

	// 멤버 등록
	@PostMapping(value = "/insert")
	public ResponseEntity<?> insertMember(@RequestBody MemberVO member) {
		return memberService.insertMember(member);
	}

	// 멤버 검색
	@GetMapping(value = "/getMemberInfo/{id}")
	public ResponseEntity<?> getMemberInfo(@PathVariable("id") String id) {
		return memberService.getMemberInfo(id);
	}

	// 아이디 중복 체크
	@GetMapping(value = "/check/{id}")
	public ResponseEntity<?> checkId(@PathVariable("id") String id) {
		return memberService.checkId(id);
	}

	// 아이디 중복 체크
	@GetMapping(value = "/find")
	public ResponseEntity<?> find(@RequestParam("tel") String tel,
			@RequestParam(value = "id", required = false) String id) {
		return memberService.find(tel, id);
	}

	@PutMapping(value = "/updateMember")
	public ResponseEntity<?> updateMember(@RequestBody MemberVO member) {
		return memberService.updateMember(member);
	}

	// 전체 멤버 조회
	@GetMapping(value = "/getMembers")
	public ResponseEntity<?> getMembers(@RequestParam("page") int page, @RequestParam("perPage") int perPage,
			@RequestParam(value = "condition", required = false) String condition,
			@RequestParam(value = "param", required = false) Object param,
			@RequestParam(value = "role", required = false) boolean role) {
		return memberService.getMembers(page, perPage, condition, param, role);
	}

	@GetMapping(value = "/login")
	public ResponseEntity<?> login(@RequestParam("id") String id, @RequestParam("password") String pwd) {
		return memberService.login(id, pwd);
	}

	// 멤버 포인트 조회
	@GetMapping("/getMemberPoint")
	public ResponseEntity<?> getMemberPoint() {
		return memberService.getMemberPoint();
	}

	// 회원 탈퇴
	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<?> deleteMember(@PathVariable("id") String id) {
		return memberService.deleteMember(id);
	}

	@GetMapping("/getNaverLogin/{token}")
	public ResponseEntity<?> getNaverLogin(@PathVariable("token") String token) {
		return memberService.getNaverLogin(token);
	}

}
