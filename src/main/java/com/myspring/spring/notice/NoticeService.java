package com.myspring.spring.notice;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class NoticeService {
	private NoticeMapper noticeMapper;

	@Autowired
	public NoticeService(NoticeMapper noticeMapper) {
		this.noticeMapper = noticeMapper;
	}

//	// 전체 개수 가져오기
//	public ResponseEntity<?> getCount(String search, String searchWord) {
//		int res = noticeMapper.getCount(search, searchWord);
//		if (res == 0)
//			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
//		else
//			return new ResponseEntity<>(res, HttpStatus.OK);
//	}
//	
//	// 게시물 목록
//	public ResponseEntity<?> getNotice(int page, int perPage, String search, String searchWord) {
//		int start = (page - 1) * perPage;
//		List<NoticeVO> res = noticeMapper.getNotice(start, perPage, search, searchWord);
//		if (res == null)
//			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
//		else
//			return new ResponseEntity<>(res, HttpStatus.OK);
//	}
	
	//게시물 목록과 개수 가져오기
	public ResponseEntity<?> getNotice(int page, int perPage, String search, String searchWord) {
		int start = (page - 1) * perPage;
		int count = noticeMapper.getCount(search, searchWord);
		List<NoticeVO> noticeList = noticeMapper.getNotice(start, perPage, search, searchWord);
		
			Map<String, Object> resMap = new HashMap<>();
			resMap.put("noticeList", noticeList);
			resMap.put("count", count);
			return new ResponseEntity<>(resMap, HttpStatus.OK);
		
	}
	

	// 공지사항 게시물 보기
	public ResponseEntity<?> getNoticeFindByID(int noticeNo) {
		 NoticeVO res = noticeMapper.getNoticeFindByID(noticeNo);
		 
		 if (res == null)
			 return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		 else
			 return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// 공지사항 게시물 작성
//	public ResponseEntity<?> insertNotice(NoticeVO noticeVO) {
//		int res = noticeMapper.insertNotice(noticeVO);
//		
//		if (res == 0)
//			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
//		else
//			return new ResponseEntity<>(res, HttpStatus.OK);
//	}
	
	// 공지사항 수정
//	public ResponseEntity<?> updateNotice(int noticeNo, String title, String content, String image) {
//		int res = noticeMapper.updateNotice( noticeNo, title, content, image);
//
//		if (res == 0)
//			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
//		else
//			return new ResponseEntity<>(res, HttpStatus.OK);
//	}
		
	// 공지사항 삭제
	public ResponseEntity<?> deleteNotice(int noticeNo) {
		System.out.println(noticeNo);
		int res = noticeMapper.deleteNotice(noticeNo);

		if (res == 0)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// 공지사항 등록 + 파일
	public ResponseEntity<?> insertNotice(NoticeVO requestData, List<MultipartFile> fileList) {
		NoticeVO result = new NoticeVO();
		ResponseEntity<?> entity = null;
		
		try {
			noticeMapper.insertNotice(requestData, result);
			int noticeNo = result.getNoticeNo();
			String[] image = requestData.getImage().split(";");
			File file = new File("./images/notice/" + noticeNo + "/");
			file.mkdir();
			file = new File("./images/notice/" + noticeNo + "/");
			file.mkdir();
			
			if (fileList != null) {
				for (int i = 0; i<fileList.size(); i++) {
					MultipartFile multipartFile = fileList.get(i);
					FileOutputStream writer = new FileOutputStream(
							"./images/notice/" + noticeNo + "/" + multipartFile.getOriginalFilename());
					writer.write(multipartFile.getBytes());
					writer.close();
							
				}
			}
			entity = new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return entity;
	}

	// 공지사항 수정 + 파일
	public ResponseEntity<?> updateNotice(NoticeVO requestData, List<MultipartFile> fileList) {
		ResponseEntity<?> entity = null;

		try {
			int res = noticeMapper.updateNotice(requestData);
			if (res == 0) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			File file;
			File[] underDir;

//			폴더 내 모든 파일 삭제
			file = new File("./images/notice/" + requestData.getNoticeNo() + "/");
			underDir = file.listFiles();
			for (int i = 0; i < underDir.length; i++) {
				underDir[i].delete();
			}

			String[] image = requestData.getImage().split(";");

			if (fileList != null) {
				for (int i = 0; i<fileList.size(); i++) {
					MultipartFile multipartFile = fileList.get(i);
					FileOutputStream writer = new FileOutputStream("./images/notice/" + requestData.getNoticeNo()
							+ "/" + multipartFile.getOriginalFilename());
	//				System.out.println(multipartFile.getOriginalFilename());
					writer.write(multipartFile.getBytes());
					writer.close();
				}
			}

			entity = new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return entity;
	}

	
	
	
//	//공지사항 수정
//		public ResponseEntity<?> updateNotice(NoticeVO noticeVO) {
//			int res = noticeMapper.updateNotice(noticeVO);
//			
//			if (res == 0)
//				return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
//			else
//				return new ResponseEntity<>(res, HttpStatus.OK);
//		}


	// 공지사항 목록 출력
//	public ResponseEntity<?> getNotice(NoticeVO noticeVO) {
//		int start = (noticeVO.getPage() - 1) * noticeVO.getPerPage();
//		noticeVO.setStart(start);
//		List<NoticeVO> res = noticeMapper.getNotice(noticeVO);
//		if (res == null)
//			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
//		else
//			return new ResponseEntity<>(res, HttpStatus.OK);
//	}
	
//	// 공지사항 목록 출력
//	public List<NoticeVO> getAllMembers(int page, int perPage) {
//		int start = (page - 1) * perPage;
//		return noticeMapper.getAllMembers(start, perPage);
//	}

	
}
