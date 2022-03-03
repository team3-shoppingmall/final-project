package com.myspring.spring.wishList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

	// 관심 상품 가져오기
	@GetMapping(value = "/getWishList")
	public ResponseEntity<?> getWishListById(@RequestParam("page") int page, @RequestParam("perPage") int perPage,
			@RequestParam("id") String id) {
		return wishListService.getWishListById(page, perPage, id);
	}
	
	// 관심 상품 가져오기
	@DeleteMapping(value = "/deleteWishList")
	public ResponseEntity<?> deleteWishList(@RequestBody List<WishListVO> deletes) {
		return wishListService.deleteWishList(deletes);
	}
}
