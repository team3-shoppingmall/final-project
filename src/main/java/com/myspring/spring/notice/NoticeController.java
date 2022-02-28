package com.myspring.spring.notice;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/notice")
public class NoticeController {
	private NoticeService noticeService;

	@Autowired
	public NoticeController(NoticeService noticeService) {
		this.noticeService = noticeService;
	}

//	// 전체 개수 가져오기
//	@GetMapping("/getCount")
//	public ResponseEntity<?> getCount(@RequestParam("search") String search, @RequestParam("searchWord") String searchWord) {
//		return noticeService.getCount(search, searchWord);
//	}

	// 공지사항 목록 출력
	@GetMapping("/getNotice")
	public ResponseEntity<?> getNotice(@RequestParam("page") int page, @RequestParam("perPage") int perPage,
			@RequestParam("search") String search, @RequestParam("searchWord") String searchWord) {
		return noticeService.getNotice(page, perPage, search, searchWord);
	}

	// 공지사항 게시물 보기
	@GetMapping("/list/{noticeNo}")
	public ResponseEntity<?> getNoticeFindByID(@PathVariable("noticeNo") int noticeNo) {
		return noticeService.getNoticeFindByID(noticeNo);
	}

	// 공지사항 게시물 작성
//	@PostMapping("/insertNotice")
//	public ResponseEntity<?> insertNotice(@RequestBody NoticeVO noticeVO) {
//		return noticeService.insertNotice(noticeVO);
//	}

	// 공지사항 수정
//	@PatchMapping("/updateNotice")
//	public ResponseEntity<?> updateNotice(@RequestParam int noticeNo, String title, String content, String image) {
//		return noticeService.updateNotice(noticeNo, title, content, image);
//	}

	// 공지사항 삭제
	@DeleteMapping("/deleteNotice/{noticeNo}")
	public ResponseEntity<?> deleteNotice(@PathVariable("noticeNo") int noticeNo) {
		return noticeService.deleteNotice(noticeNo);
	}


	// 공지사항 추가 + 파일
	@PostMapping("/insertNotice")
	public ResponseEntity<?> insertNotice(@RequestPart(value = "data") NoticeVO requestData,
					@RequestParam(value = "fileList", required = false) List<MultipartFile> fileList) throws NotFoundException {
		return noticeService.insertNotice(requestData, fileList);
	}
	
	// 공지사항 수정 + 파일
	@PatchMapping("/updateNotice")
	public ResponseEntity<?> updataNotice(@RequestPart(value = "data") NoticeVO requestData,
					@RequestParam(value = "fileList", required = false) List<MultipartFile> fileList) throws NotFoundException {
		return noticeService.updateNotice(requestData, fileList);
	}
	
	// 서버에서 이미지 가져오기
	@GetMapping("/noticeImage/{noticeNo}/{image}")
	public ResponseEntity<?> noticeImage(@PathVariable("noticeNo") int noticeNo, @PathVariable("image") String image)
				throws IOException {
		InputStream imageStream;
		
		try {
			imageStream = new FileInputStream("./images/notice/" + noticeNo + "/" + image);
		} catch (FileNotFoundException e) {
			imageStream = new FileInputStream("./images/err.png");
		}
		byte[] imageByteArray = IOUtils.toByteArray(imageStream);
		imageStream.close();
		
		return new ResponseEntity<byte[]> (imageByteArray, HttpStatus.OK);
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
