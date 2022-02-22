package com.myspring.spring.member;

import java.util.List;

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
	public ResponseEntity<?> getAllmembers() {
		List<MemberVO> res = memberMapper.getAllMembers();
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// 아이디로 멤버 조회
	public ResponseEntity<?> getMember(String id) {
		MemberVO res = memberMapper.getMember(id);
		if (res != null)
			return new ResponseEntity<>(res, HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
