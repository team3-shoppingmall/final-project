package com.myspring.spring.wishList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class WishListService {
	private WishListMapper productMapper;

	@Autowired
	public WishListService(WishListMapper productMapper) {
		this.productMapper = productMapper;
	}

	// 멤버 등록
	public ResponseEntity<?> insertMember(WishListVO member) {
		productMapper.insertMember(member);
		return null;
	}

	// 전체 멤버 조회
	public ResponseEntity<?> getAllmembers() {
		List<WishListVO> res = productMapper.getAllMembers();
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// 아이디로 멤버 조회
	public ResponseEntity<?> getMember(String id) {
		WishListVO res = productMapper.getMember(id);
		if (res != null)
			return new ResponseEntity<>(res, HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
