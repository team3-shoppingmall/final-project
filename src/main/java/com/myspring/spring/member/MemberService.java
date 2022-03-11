package com.myspring.spring.member;

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
		if(member.getAuthority() == null)
			member.setAuthority("ROLE_USER");
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
		System.out.println(encoder.encode(pwd));
		MemberVO res = memberMapper.login(id);
		String resPwd;

		// pwd 암호화 넣기

		if (res == null) {
			return new ResponseEntity<>("ID NOT FOUND", HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			resPwd = res.getPassword();
		}
		System.out.println("1 " + pwd + "2 " + resPwd);
		if (pwd.equals(resPwd)) {
			res.setPassword(null);
			return new ResponseEntity<>(res, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("PASSWORD NOT MATCHED", HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	public ResponseEntity<?> checkId(String id) {
		MemberVO res = memberMapper.getMemberInfo(id);

		if (res == null) {
			System.out.println("OK");
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
