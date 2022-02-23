package com.myspring.spring.wishList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/wishList")
public class WishListController {
	private WishListService wishListService;

	@Autowired
	public WishListController(WishListService wishListService) {
		this.wishListService = wishListService;
	}

	// 관심 상품 추가
	@PostMapping(value = "/insert")
	public ResponseEntity<?> insertWishList(@RequestBody WishListVO wishList) {
		return wishListService.insertWishList(wishList);
	}

}
