package com.myspring.spring.basket;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BasketService {
	private BasketMapper basketMapper;

	@Autowired
	public BasketService(BasketMapper basketMapper) {
		this.basketMapper = basketMapper;
	}

	// 장바구니 추가
	public ResponseEntity<?> insertBakset(List<BasketVO> basketList) {
		for (BasketVO basket : basketList) {
			int res = basketMapper.insertBakset(basket);
			if (res == 0)
				return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// 장바구니 조회
	public ResponseEntity<?> getBasketById(String id) {
		BasketVO res = basketMapper.getBasketById(id);
		if (res == null)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(HttpStatus.OK);
	}
}
