package com.myspring.spring.banner;

import java.util.List;

import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.myspring.spring.product.ProductVO;

@RestController
@RequestMapping(value = "/api/banner")
public class BannerController {
	private BannerService bannerService;

	@Autowired
	public BannerController(BannerService bannerService) {
		this.bannerService = bannerService;
	}

	@PostMapping(value = "insertBanner")
	public ResponseEntity<?> insertBanner(@RequestPart(value = "data") BannerVO data,
			@RequestParam("banner") List<MultipartFile> banner) throws NotFoundException {
		return bannerService.insertBanner(data, banner);
	}
}
