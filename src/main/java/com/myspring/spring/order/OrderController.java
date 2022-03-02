package com.myspring.spring.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.myspring.spring.point.PointVO;

@RestController
@RequestMapping(value = "/api/order")
public class OrderController {
	private OrderService orderService;

	@Autowired
	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

//	주문 추가
	@PostMapping("/insertOrder")
	public ResponseEntity<?> insertOrder(@RequestPart(value = "orderList") List<OrderVO> orderList,
			@RequestPart(value = "basketIdxList") List<Long> basketIdxList,
			@RequestPart(value = "pointData") PointVO pointVO) {
		return orderService.insertOrder(orderList, basketIdxList, pointVO);
	}
}
