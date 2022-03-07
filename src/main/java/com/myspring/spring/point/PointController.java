package com.myspring.spring.point;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/point")
public class PointController {
	private PointService pointService;

	@Autowired
	public PointController(PointService pointService) {
		this.pointService = pointService;
	}

	@GetMapping("/getPointList")
	public ResponseEntity<?> getPointList(@RequestParam("page") int page, @RequestParam("perPage") int perPage,
			@RequestParam("id") String id) {
		return pointService.getPointList(page, perPage, id);
	}

}
