package com.myspring.spring.point;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.myspring.spring.member.MemberMapper;
import com.myspring.spring.member.MemberVO;

@Service
public class PointService {
	private PointMapper pointMapper;
	private MemberMapper memberMapper;

	@Autowired
	public PointService(PointMapper productMapper, MemberMapper memberMapper) {
		this.pointMapper = productMapper;
		this.memberMapper = memberMapper;
	}

	public ResponseEntity<?> getPointList(int page, int perPage, String id) {
		int start = (page - 1) * perPage;
		List<PointVO> pointList = pointMapper.getPointList(start, perPage, id);
		int count = pointMapper.getPointListCount(id);
		int point = memberMapper.getMemberInfo(id).getPoint();

		Map<String, Object> resMap = new HashMap<>();
		resMap.put("pointList", pointList);
		resMap.put("count", count);
		resMap.put("point", point);
		return new ResponseEntity<>(resMap, HttpStatus.OK);
	}
}
