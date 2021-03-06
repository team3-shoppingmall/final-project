package com.myspring.spring.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.myspring.spring.basket.BasketMapper;
import com.myspring.spring.member.MemberMapper;
import com.myspring.spring.point.PointMapper;
import com.myspring.spring.point.PointVO;
import com.myspring.spring.product.ProductVO;
import com.myspring.spring.review.ReviewMapper;

@Service
public class OrderService {
	private OrderMapper orderMapper;
	private BasketMapper basketMapper;
	private PointMapper pointMapper;
	private MemberMapper memberMapper;

	@Autowired
	public OrderService(OrderMapper orderMapper, BasketMapper basketMapper, PointMapper productMapper,
			MemberMapper memberMapper) {
		this.orderMapper = orderMapper;
		this.basketMapper = basketMapper;
		this.pointMapper = productMapper;
		this.memberMapper = memberMapper;
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
		if (basketIdxList.get(0) != null) {
			for (long basketIdx : basketIdxList) {
				System.out.println(basketIdx);
				res = basketMapper.deleteBasket(basketIdx);
				if (res == 0)
					return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		if (pointVO.getPoint() != 0) {
			res = pointMapper.insertPoint(pointVO);
		}
		if (res == 0)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(HttpStatus.OK);
	}

//  주문 조회
	public ResponseEntity<?> getOrder(int page, int perPage, String state, String search, String searchWord1,
			String searchWord2) {
		int start = (page - 1) * perPage;
		List<OrderAndProductVO> orderList = orderMapper.getOrder(start, perPage, state, search, searchWord1,
				searchWord2);
		int count = orderMapper.getOrderCount(state, search, searchWord1, searchWord2);
		Map<String, Object> resMap = new HashMap<>();
		resMap.put("orderList", orderList);
		resMap.put("count", count);
		return new ResponseEntity<>(resMap, HttpStatus.OK);
	}

//  판매 정산 조회
	public ResponseEntity<?> getSalesSettlement(int page, int perPage, String search, String searchWord1,
			String searchWord2) {
		int start = (page - 1) * perPage;
		List<OrderAndProductVO> salesList = orderMapper.getSalesSettlement(start, perPage, search, searchWord1,
				searchWord2);
		List<Long> countList = orderMapper.getSalesSettlementCount(search, searchWord1, searchWord2);
		Map<String, Object> resMap = new HashMap<>();
		resMap.put("salesList", salesList);
		resMap.put("countList", countList);
		return new ResponseEntity<>(resMap, HttpStatus.OK);
	}

//  마이 페이지 주문 조회
	public ResponseEntity<?> getOrderById(int page, int perPage, String pageInfo, String state, String searchWord,
			String searchDate1, String searchDate2, String id) {
		int start = (page - 1) * perPage;
		List<OrderAndProductVO> orderList = orderMapper.getOrderById(start, perPage, pageInfo, state, searchWord,
				searchDate1, searchDate2, id);
		int count = orderMapper.getOrderByIdCount(pageInfo, state, searchWord, searchDate1, searchDate2, id);
		Map<String, Object> resMap = new HashMap<>();
		resMap.put("orderList", orderList);
		resMap.put("count", count);
		return new ResponseEntity<>(resMap, HttpStatus.OK);
	}

//	마이 페이지 메인 각각 주문 개수 조회
	public ResponseEntity<?> getOrdersByIdGroupByState(String id) {
		List<OrderVO> orderCounts = orderMapper.getOrdersByIdGroupByState(id);
		int point = memberMapper.getMemberInfo(id).getPoint();
		Map<String, Object> resMap = new HashMap<>();
		resMap.put("orderCounts", orderCounts);
		resMap.put("point", point);
		return new ResponseEntity<>(resMap, HttpStatus.OK);
	}

//  주문 번호로 조회
	public ResponseEntity<?> getOrderByNo(int orderIdx) {
		OrderVO res = orderMapper.getOrderByNo(orderIdx);
		return new ResponseEntity<>(res.getState(), HttpStatus.OK);
	}

//  리뷰 가능 주문 개수 조회
	public ResponseEntity<?> getCountToReview(String id, int productNo) {
		OrderVO res = orderMapper.getOrderToReview(id, productNo);
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

//	주문 변경
	public ResponseEntity<?> updateOrder(List<OrderVO> states) {
		List<Long> orderIdxList = new ArrayList<Long>();
		for (OrderVO orderVO : states) {
			int res = orderMapper.updateOrder(orderVO.getOrderIdx(), orderVO.getState());
			if (res == 0) {
				orderIdxList.add(orderVO.getOrderIdx());
			}

		}
		return new ResponseEntity<>(orderIdxList, HttpStatus.OK);
	}
}
