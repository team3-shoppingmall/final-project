package com.myspring.spring.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.myspring.spring.basket.BasketMapper;
import com.myspring.spring.point.PointMapper;
import com.myspring.spring.point.PointVO;

@Service
public class OrderService {
	private OrderMapper orderMapper;
	private BasketMapper basketMapper;
	private PointMapper pointMapper;

	@Autowired
	public OrderService(OrderMapper orderMapper, BasketMapper basketMapper, PointMapper productMapper) {
		this.orderMapper = orderMapper;
		this.basketMapper = basketMapper;
		this.pointMapper = productMapper;
	}

//	주문 추가(주문 정보, 주문에 추가한 장바구니 번호들, 포인트 사용량 받아서 한번에 추가)
	public ResponseEntity<?> insertOrder(List<OrderVO> orderList, List<Long> basketIdxList, PointVO pointVO) {
		OrderVO result = new OrderVO();
		int res = 0;
		long orderNo = 0;
		for (int i = 0; i < orderList.size(); i++) {
			if (i == 0) {
				res = orderMapper.insertOrderFirst(orderList.get(i), result);
				orderNo = result.getOrderIdx();
			} else {
				res = orderMapper.insertOrder(orderList.get(i), orderNo);
			}
			if (res == 0)
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		for (long basketIdx : basketIdxList) {
			res = basketMapper.deleteBasket(basketIdx);
			if (res == 0)
				return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(pointVO.getPoint() != 0) {
			res = pointMapper.insertPoint(pointVO);
		}
		if (res == 0)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(HttpStatus.OK);
	}
}
