package com.myspring.spring.product;

import java.util.List;

import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/product")
public class ProductController {
	private ProductService productService;

	@Autowired
	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	// 상품 리스트 조회
	@GetMapping(value = "/getProductList")
	public ResponseEntity<?> getProductList(@RequestParam("page") int page, @RequestParam("perPage") int perPage,
			@RequestParam(value = "type1", required = false) String type1,
			@RequestParam(value = "type2", required = false) String type2,
			@RequestParam(value = "searchWord", required = false) String searchWord,
			@RequestParam("minPrice") int minPrice, @RequestParam("maxPrice") int maxPrice,
			@RequestParam(value = "searchOrder", required = false) String searchOrder) {
		return productService.getProductList(page, perPage, type1, type2, searchWord, minPrice, maxPrice, searchOrder);
	}

	// 많이 팔린 상품 조회
	@GetMapping(value = "/getBestProductList")
	public ResponseEntity<?> getBestProductList(@RequestParam("type1") String type1,
			@RequestParam("type2") String type2) {
		return productService.getBestProductList(type1, type2);
	}

	// 상품 정보 조회
	@GetMapping(value = "/getProduct/{productNo}")
	public ResponseEntity<?> getProductByNo(@PathVariable("productNo") int productNo) {
		return productService.getProductByNo(productNo);
	}

	@PostMapping("/insertProduct")
	public ResponseEntity<?> insertProduct(@RequestPart(value = "data") ProductVO requestData,
			@RequestParam("fileList") List<MultipartFile> fileList) throws NotFoundException {
		return productService.insertProduct(requestData, fileList);
	}

	@PutMapping("/updateProduct")
	public ResponseEntity<?> updateProduct(@RequestPart(value = "data") ProductVO requestData,
			@RequestParam(value = "file1", required = false) List<MultipartFile> file1,
			@RequestParam(value = "file2", required = false) List<MultipartFile> file2) throws NotFoundException {
		return productService.updateProduct(requestData, file1, file2);
	}

}
