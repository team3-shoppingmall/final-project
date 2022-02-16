package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.example.demo.service.TestService;
import com.example.demo.vo.TestVO;

@RestController
@RequestMapping(value = "/api")
public class TestController {
	private TestService testService;

	@Autowired
	public TestController(TestService testService) {
		this.testService = testService;
	}

	@GetMapping("/test")
	public Map<String, String> test() {
		Map<String, String> testMap = new HashMap<>();

		testMap.put("id", "test");
		testMap.put("pwd", "1234");

		return testMap;
	}

	@GetMapping("/test1")
	public List<TestVO> test1() {
		return testService.getAllMembers();
	}

	@GetMapping("/getID/{id}")
	public TestVO getMemberFindByID1(@PathVariable("id") String id) {
		return testService.getMemberFindByID(id);
	}

	@GetMapping("/getID2")
	public TestVO getMemberFindByID2(@RequestParam("id") String id) {
		return testService.getMemberFindByID(id);
	}

	@GetMapping("/getID3")
	public TestVO getMemberFindByID3(@RequestBody TestVO testVO) {
		System.out.println("id " + testVO.getId());
		System.out.println("pwd " + testVO.getPwd());
		return null;
//		return testService.getMemberFindByID(testVO.getId());
	}

	@PostMapping("/insertMember1")
	public int insertMember1(@RequestParam(value = "id") String id,
			@RequestParam(value = "pwd", required = false) String pwd) {
		return testService.insertMember1(id, pwd);
	}

	@PostMapping("/insertMember2")
	public int insertMember2(@RequestBody TestVO testVO) {
		return testService.insertMember2(testVO);
	}

	@GetMapping("/test2")
	public ResponseEntity<?> test2() {
		return testService.getAllMembers2();
	}
	
	@DeleteMapping("/deleteMember")
	public ResponseEntity<?> deleteMember(@RequestParam("id") String id){
		return testService.deleteMember(id);
	}
	
	
	@PatchMapping("/updateMember")
	public ResponseEntity<?> updateMember(@RequestParam("id") String id, @RequestParam("pwd") String pwd){
		return testService.updateMember(id,pwd);
	} 
		
}
