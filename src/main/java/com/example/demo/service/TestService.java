package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.TestMapper;
import com.example.demo.vo.TestVO;

@Service
public class TestService {
	private TestMapper testMapper;

	@Autowired
	public TestService(TestMapper testMapper) {
		this.testMapper = testMapper;
	}

	public List<TestVO> getAllMembers() {
		return testMapper.getAllMembers();
	}

	public TestVO getMemberFindByID(String id) {
		return testMapper.getMemberFindByID(id);
	}

	public int insertMember1(String id, String pwd) {
		return testMapper.insertMember1(id, pwd);
	}

	public int insertMember2(TestVO testVO) {
		return testMapper.insertMember2(testVO);
	}

	public ResponseEntity<?> getAllMembers2() {
		List<TestVO> res = testMapper.getAllMembers();
		if (res == null)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}

	public ResponseEntity<?> deleteMember(String id) {
		int res = testMapper.deleteMember(id);
//		return res == 0 ? new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR) : new ResponseEntity<>(HttpStatus.OK);
		if (res == 0)
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(HttpStatus.OK);
	}

	public ResponseEntity<?> updateMember(String id, String pwd) {
		int res = testMapper.updateMember(id, pwd);
//		return res == 0 ? new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR) : new ResponseEntity<>(HttpStatus.OK);
		if (res == 0)
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(HttpStatus.OK);
	}
}
