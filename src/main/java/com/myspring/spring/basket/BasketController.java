package com.myspring.spring.basket;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/basket")
public class BasketController {
	private BasketService basketService;

	@Autowired
	public BasketController(BasketService basketService) {
		this.basketService = basketService;
	}

	// 장바구니 추가
	@PostMapping(value = "/insert")
	public ResponseEntity<?> insertBakset(@RequestBody List<BasketVO> basketList) {
		return basketService.insertBakset(basketList);
	}

	// 장바구니 조회
	@PostMapping(value = "/getBasket/{id}")
	public ResponseEntity<?> getBasketById(@PathVariable("id") String id) {
		return basketService.getBasketById(id);
	}
}
