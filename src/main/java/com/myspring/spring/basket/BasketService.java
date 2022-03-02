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
		int count = 0;
		for (BasketVO basket : basketList) {
			int res = basketMapper.basketCheck(basket);
			if (res > 0) {
				count++;
			} else {
				res = basketMapper.insertBakset(basket);
				if (res == 0)
					return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		System.out.println(count);
		return new ResponseEntity<>(count, HttpStatus.OK);
	}

	// 장바구니 조회
	public ResponseEntity<?> getBasketById(String id) {
		List<BasketAndProductVO> res = basketMapper.getBasketById(id);
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// 장바구니 개수 조회
	public ResponseEntity<?> getBasketCountById(String id) {
		int res = basketMapper.getBasketCountById(id);
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

//	장바구니 개수 변경
	public ResponseEntity<?> updateBasketAmount(long basketIdx, int basketAmount) {
		int res = basketMapper.updateBasketAmount(basketIdx, basketAmount);
		if (res == 0)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(res, HttpStatus.OK);

	}

//	장바구니 삭제
	public ResponseEntity<?> deleteBasket(List<Long> deletes) {
		for (long basketIdx : deletes) {
			int res = basketMapper.deleteBasket(basketIdx);
			if (res == 0)
				return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
