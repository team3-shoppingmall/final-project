package com.myspring.spring.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

//	주문 조회
	@GetMapping(value = "/getOrder")
	public ResponseEntity<?> getOrder(@RequestParam("page") int page, @RequestParam("perPage") int perPage,
			@RequestParam(value = "state", required = false) String state,
			@RequestParam(value = "search", required = false) String search,
			@RequestParam(value = "searchWord1", required = false) String searchWord1,
			@RequestParam(value = "searchWord2", required = false) String searchWord2) {
		return orderService.getOrder(page, perPage, state, search, searchWord1, searchWord2);
	}

//	판매 정산 조회
	@GetMapping(value = "/getSalesSettlement")
	public ResponseEntity<?> getSalesSettlement(@RequestParam("page") int page, @RequestParam("perPage") int perPage,
			@RequestParam(value = "search", required = false) String search,
			@RequestParam(value = "searchWord1", required = false) String searchWord1,
			@RequestParam(value = "searchWord2", required = false) String searchWord2) {
		return orderService.getSalesSettlement(page, perPage, search, searchWord1, searchWord2);
	}

//	마이 페이지 주문 조회
	@GetMapping(value = "/getOrderById")
	public ResponseEntity<?> getOrderById(@RequestParam("page") int page, @RequestParam("perPage") int perPage,
			@RequestParam("pageInfo") String pageInfo, @RequestParam(value = "state", required = false) String state,
			@RequestParam(value = "searchWord", required = false) String searchWord,
			@RequestParam(value = "searchDate1", required = false) String searchDate1,
			@RequestParam(value = "searchDate2", required = false) String searchDate2,
			@RequestParam(value = "id", required = false) String id) {
		return orderService.getOrderById(page, perPage, pageInfo, state, searchWord, searchDate1, searchDate2, id);
	}

//	마이 페이지 주문 조회
	@GetMapping(value = "/getOrderGroupByState/{id}")
	public ResponseEntity<?> getOrdersByIdGroupByState(@PathVariable("id") String id) {
		return orderService.getOrdersByIdGroupByState(id);
	}

//	주문 상태 변경
	@PatchMapping("/update")
	public ResponseEntity<?> updateOrder(@RequestBody List<OrderVO> states) {
		return orderService.updateOrder(states);
	}
}
