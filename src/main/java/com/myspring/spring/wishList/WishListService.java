package com.myspring.spring.wishList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	// 관심 상품 가져오기
	public ResponseEntity<?> getWishListById(int page, int perPage, String id) {
		int start = (page - 1) * perPage;
		List<WishListAndProductVO> wishLists = wishListMapper.getWishListById(start, perPage, id);
		int count = wishListMapper.getWishListCountById(id);

		Map<String, Object> resMap = new HashMap<>();
		resMap.put("wishLists", wishLists);
		resMap.put("count", count);
		return new ResponseEntity<>(resMap, HttpStatus.OK);
	}

//	관심상품 삭제
	public ResponseEntity<?> deleteWishList(List<WishListVO> deletes) {
		for (WishListVO wishListVO : deletes) {
			int res = wishListMapper.deleteWishList(wishListVO);
			if (res == 0)
				return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
