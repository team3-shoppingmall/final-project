package com.myspring.spring.notice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/notice")
public class NoticeController {
	private NoticeService noticeService;

	@Autowired
	public NoticeController(NoticeService noticeService) {
		this.noticeService = noticeService;
	}

	// 전체 개수 가져오기
	@GetMapping("/getCount")
	public ResponseEntity<?> getCount(@RequestParam("search") String search, @RequestParam("searchWord") String searchWord) {
		return noticeService.getCount(search, searchWord);
	}

	// 공지사항 목록 출력
	@GetMapping("/getNotice")
	public ResponseEntity<?> getNotice(@RequestParam("page") int page, @RequestParam("perPage") int perPage,
			@RequestParam("search") String search, @RequestParam("searchWord") String searchWord) {
		return noticeService.getNotice(page, perPage, search, searchWord);
	}
	
	// 공지사항 게시물 보기
	@GetMapping("/list/{noticeNo}")
	public ResponseEntity<?> getNoticeFindByID(@PathVariable("noticeNo") NoticeVO noticeNo) {
		return noticeService.getNoticeFindByID(noticeNo);
	}
	
	// 공지사항 게시물 작성
	@PostMapping("/insertNotice")
	public ResponseEntity<?> insertNotice(@RequestBody NoticeVO noticeVO) {
		return noticeService.insertNotice(noticeVO);
	}
		
	// 공지사항 수정
	@PatchMapping("/updateNotice")
	public ResponseEntity<?> updateNotice(@RequestParam int noticeNo, String title, String content, String image) {
		return noticeService.updateNotice(noticeNo, title, content, image);
	}
	
	// 공지사항 삭제
	@DeleteMapping("/deleteNotice")
	public ResponseEntity<?> deleteNotice(@RequestParam("noticeNo") int noticeNo) {
		return noticeService.deleteNotice(noticeNo);
	}
	
	
	
	// 공지사항 수정
//	@PatchMapping("/updateNotice")
//	public ResponseEntity<?> updateNotice(@RequestBody NoticeVO noticeVO) {
//		return noticeService.updateNotice(noticeVO);
//	}	
	
//	// 공지사항 목록 출력
//	@GetMapping("/getNotice")
//	public ResponseEntity<?> getNotice(@RequestBody NoticeVO noticeVO) {
//		return noticeService.getNotice(noticeVO);
//	}
	
//	// 공지사항 목록 출력
//	@GetMapping("/getNotice")
//	public List<NoticeVO> list(@RequestParam("page") int page, @RequestParam("perPage") int perPage) {
//		return noticeService.getAllMembers(page, perPage);
//	}

}
