package com.myspring.spring.wishList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class WishListService {
	private WishListMapper wishListMapper;

	@Autowired
	public WishListService(WishListMapper wishListMapper) {
		this.wishListMapper = wishListMapper;
	}

	// 관심 상품 추가
	public ResponseEntity<?> insertWishList(WishListVO wishList) {
		int res = wishListMapper.insertWishList(wishList);
		if (res == 0)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}
}
