package com.myspring.spring.member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
	private MemberMapper memberMapper;

	@Autowired
	public MemberService(MemberMapper memberMapper) {
		this.memberMapper = memberMapper;
	}

	// 멤버 등록
	public ResponseEntity<?> insertMember(MemberVO member) {
		memberMapper.insertMember(member);
		return null;
	}

	// 전체 멤버 조회
	public ResponseEntity<?> getMemberAll(int page, int perPage) {
		int start = (page - 1) * perPage;
		List<MemberVO> res = memberMapper.getMemberAll(start, perPage);
		int count = memberMapper.getMemberCount();
		Map<String, Object> resMap = new HashMap<>();
		resMap.put("res", res);
		resMap.put("count", count);
		return new ResponseEntity<>(resMap, HttpStatus.OK);
	}

	// 아이디로 멤버 조회
	public ResponseEntity<?> getMembers(String condition, Object param) {
		List<MemberVO> res = memberMapper.getMembers(condition, param);
		if (res != null)
			return new ResponseEntity<>(res, HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// 맴버 정보 수정
	public ResponseEntity<?> updateMember(MemberVO member) {
		int res = memberMapper.updateMember(member);
		if (res == 0)
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(HttpStatus.OK);
	}

}
