package com.myspring.spring.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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

	@PutMapping(value = "/updateMember")
	public ResponseEntity<?> updateMember(@RequestBody MemberVO member) {
		return memberService.updateMember(member);
	}

	// 전체 멤버 조회
	@GetMapping(value = "/getMembers")
	public ResponseEntity<?> getMembers(@RequestParam("page") int page, @RequestParam("perPage") int perPage,
			@RequestParam(value = "condition", required = false) String condition,
			@RequestParam(value = "param", required = false) Object param) {
		return memberService.getMembers(page, perPage, condition, param);
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

	@GetMapping("/getNaverLogin/{token}")
	public ResponseEntity<?> getNaverLogin(@PathVariable("token") String token) {
		return memberService.getNaverLogin(token);
	}
	@GetMapping("/getKakaoLogin")
	public @ResponseBody String getKakaoLogin(String code) { //Data를 리턴해주는 컨트롤러 함수
		
		// POST방식으로 key=value 데이터를 요청 (카카오쪽으로)
		//Retrofit2 - 안드로이드 많이 씀
		//OkHttp
		//RestTemplate
		
		RestTemplate rt = new RestTemplate();
		
		//HttpHeader 오브젝트 생성
		HttpHeaders headers = new HttpHeaders();
		//지금 body에 입력한 데이터가 key=value 데이터라는걸 명시
		headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");
		
		//HttpBody 오브젝트 생성
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type","authorization_code");
		params.add("client_id","42b435f74388d237a488dc4489022bf7");
		params.add("redirect_uri","http://localhost:8085/api/member/getKakaoLogin");
		params.add("code",code);
		
		//HttpHeader와 HttpBody를 하나의 오브젝트에 담기
		HttpEntity<MultiValueMap<String,String>>kakaoTokenRequest =
				new HttpEntity<>(params,headers);
		
		//Http 요청하기 - POST방식으로 - 그리고 response 변수의 응답 받음
		ResponseEntity<String> response=rt.exchange( //httpEntity메소드를 담는 함수
				"https://kauth.kakao.com/oauth/token",
				HttpMethod.POST,
				kakaoTokenRequest,
				String.class
				);
		
		//Gson,Json Simple,ObjectMapper
		ObjectMapper objectMapper = new ObjectMapper();
		MemberOauthToken memberOauthToken = null;
		
		try {
			memberOauthToken = objectMapper.readValue(response.getBody(),MemberOauthToken.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		System.out.println("카카오 엑세스 토큰:" + memberOauthToken.getAccess_token());
		
		return response.getBody();
	}
}
