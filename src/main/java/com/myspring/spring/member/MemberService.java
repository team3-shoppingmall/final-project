package com.myspring.spring.member;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
	private MemberMapper memberMapper;
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	@Autowired
	public MemberService(MemberMapper memberMapper) {
		this.memberMapper = memberMapper;
	}

	// 멤버 등록
	public ResponseEntity<?> insertMember(MemberVO member) {
		if (member.getAuthority() == null)
			member.setAuthority("ROLE_USER");
		String secPwd = encoder.encode(member.getPassword());
		member.setPassword(secPwd);

		int res = memberMapper.insertMember(member);

		if (res == 1) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 멤버 조회
	public ResponseEntity<?> getMembers(int page, int perPage, String condition, Object param) {
		int start = (page - 1) * perPage;
		List<MemberVO> res = memberMapper.getMembers(start, perPage, condition, param);
		int count = memberMapper.getMemberCount();
		Map<String, Object> resMap = new HashMap<>();
		resMap.put("res", res);
		resMap.put("count", count);
		return new ResponseEntity<>(resMap, HttpStatus.OK);
	}

	// 멤버 정보 조회
	public ResponseEntity<?> getMemberInfo(String id) {
		MemberVO res = memberMapper.getMemberInfo(id);
		res.setPassword(null);
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// 맴버 정보 수정
	public ResponseEntity<?> updateMember(MemberVO member) {
		if (member.getPassword() == null || member.getPassword().equals("")) {
			MemberVO temp = memberMapper.getMemberInfo(member.getId());
			String tempPwd = temp.getPassword();
			member.setPassword(tempPwd);
		} else {
			String secPwd = encoder.encode(member.getPassword());
			member.setPassword(secPwd);
		}
		int res = memberMapper.updateMember(member);

		if (res == 0)
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(HttpStatus.OK);
	}

	// 전체 조회
	public ResponseEntity<?> getMemberPoint() {
		List<MemberVO> res = memberMapper.getMemberPoint();

		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	public ResponseEntity<?> login(String id, String pwd) {
//		System.out.println(encoder.encode(pwd));
		MemberVO res = memberMapper.login(id);
		String resPwd;
//		if(encoder.matches(pwd, "$2a$10$wX6Q2YP5zDbTUQwJeMExFO2aB8SkxCBDFfUgfKY57QAQlxiynYe5G")){
//			System.out.println("true");
//		}
		// pwd 암호화 넣기
		if (res == null) {
			return new ResponseEntity<>("ID NOT FOUND", HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			resPwd = res.getPassword();
		}
//		System.out.println("1 " + pwd + " 2 " + resPwd);
		if (encoder.matches(pwd, res.getPassword())) {
			res.setPassword(null);
			return new ResponseEntity<>(res, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("PASSWORD NOT MATCHED", HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	public ResponseEntity<?> checkId(String id) {
		MemberVO res = memberMapper.getMemberInfo(id);

		if (res == null) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	public ResponseEntity<?> getNaverLogin(String token) {
		String header = "Bearer " + token; // Bearer 다음에 공백 추가

		String apiURL = "https://openapi.naver.com/v1/nid/me";

		Map<String, String> requestHeaders = new HashMap<>();
		requestHeaders.put("Authorization", header);
		String responseBody = get(apiURL, requestHeaders);

		return new ResponseEntity<>(responseBody, HttpStatus.OK);
	}

	private static String get(String apiUrl, Map<String, String> requestHeaders) {
		HttpURLConnection con = connect(apiUrl);
		try {
			con.setRequestMethod("GET");
			for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
				con.setRequestProperty(header.getKey(), header.getValue());
			}

			int responseCode = con.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
				return readBody(con.getInputStream());
			} else { // 에러 발생
				return readBody(con.getErrorStream());
			}
		} catch (IOException e) {
			throw new RuntimeException("API 요청과 응답 실패", e);
		} finally {
			con.disconnect();
		}
	}

	private static HttpURLConnection connect(String apiUrl) {
		try {
			URL url = new URL(apiUrl);
			return (HttpURLConnection) url.openConnection();
		} catch (MalformedURLException e) {
			throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
		} catch (IOException e) {
			throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
		}
	}

	private static String readBody(InputStream body) {
		InputStreamReader streamReader = new InputStreamReader(body);

		try (BufferedReader lineReader = new BufferedReader(streamReader)) {
			StringBuilder responseBody = new StringBuilder();

			String line;
			while ((line = lineReader.readLine()) != null) {
				responseBody.append(line);
			}

			return responseBody.toString();
		} catch (IOException e) {
			throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
		}
	}

	public ResponseEntity<?> deleteMember(String id) {
		int res = memberMapper.deleteMember(id);
		if (res == 0) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}

}
