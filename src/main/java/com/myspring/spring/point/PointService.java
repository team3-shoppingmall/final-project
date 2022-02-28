package com.myspring.spring.point;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PointService {
	private PointMapper pointMapper;

	@Autowired
	public PointService(PointMapper productMapper) {
		this.pointMapper = productMapper;
	}
    
	// 전체 조회
	public ResponseEntity<?> getPointAll() {
       List<PointVO> res = pointMapper.getPointAll();
       if (res == null)
    	   return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
       else
    	   return new ResponseEntity<>(res, HttpStatus.OK);
	}


}
