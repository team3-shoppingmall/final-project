package com.myspring.spring.notice;

import java.util.List;

import org.apache.ibatis.annotations.Param;
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
@RequestMapping(value="/api/notice")
public class NoticeController {
	private NoticeService noticeService;
	
	@Autowired
	public NoticeController(NoticeService noticeService) {
		this.noticeService = noticeService;
	}
	
	//공지사항 목록 출력
	@GetMapping("/notice/list")
	public List<NoticeVO> list() {
		return noticeService.getAllMembers();
	}
	
	//공지사항 게시물 보기
	@GetMapping("/notice/list/{noticeNo}")
	public NoticeVO getMemberFindByID(@PathVariable("noticeNo") int noticeNo) {
		return noticeService.getMemberFindByID(noticeNo);
	}
	
	//공지사항 삭제
	@DeleteMapping("/notice/deleteMember")
	public ResponseEntity<?> deleteMamber(@RequestParam("noticeNo") int noticeNo) {
		return noticeService.deleteMember(noticeNo);
	}
	
	//공지사항 게시물 작성
	@PostMapping("/notice/insertMember")
	public ResponseEntity<?> insertMember(@RequestBody NoticeVO noticeVO) {
		return noticeService.insertMember(noticeVO);
	}

	//공지사항 수정
	@PatchMapping("/notice/updateMember")
	public ResponseEntity<?> updateMember(@RequestParam("noticeNo") int noticeNo,@RequestParam("title") String title, @RequestParam("content") String content, @RequestParam("image") String image) {
		return noticeService.updateMember(noticeNo, title, content, image);
	}
	
}
