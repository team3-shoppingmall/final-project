package com.myspring.spring.product;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

//	 상품 리스트 조회
	@GetMapping(value = "/getProductAll")
	public ResponseEntity<?> getProductAll(@RequestParam("page") int page, @RequestParam("perPage") int perPage,
			@RequestParam(value = "type1", required = false) String type1,
			@RequestParam(value = "type2", required = false) String type2,
			@RequestParam(value = "search", required = false) String search,
			@RequestParam(value = "searchWord1", required = false) String searchWord1,
			@RequestParam(value = "searchWord2", required = false) String searchWord2) {
		return productService.getProductAll(page, perPage, type1, type2, search, searchWord1, searchWord2);
	}

//	 상품 리스트 조회
	@GetMapping(value = "/getProductList")
	public ResponseEntity<?> getProductList(@RequestParam("page") int page, @RequestParam("perPage") int perPage,
			@RequestParam(value = "type1", required = false) String type1,
			@RequestParam(value = "type2", required = false) String type2,
			@RequestParam(value = "searchWord", required = false) String searchWord,
			@RequestParam("minPrice") int minPrice, @RequestParam("maxPrice") int maxPrice,
			@RequestParam(value = "searchOrder", required = false) String searchOrder) {
		return productService.getProductList(page, perPage, type1, type2, searchWord, minPrice, maxPrice, searchOrder);
	}

//	 많이 팔린 상품 조회
	@GetMapping(value = "/getBestProductList")
	public ResponseEntity<?> getBestProductList(@RequestParam("type1") String type1,
			@RequestParam("type2") String type2) {
		return productService.getBestProductList(type1, type2);
	}

//	 상품 정보 조회
	@GetMapping(value = "/getProduct/{productNo}")
	public ResponseEntity<?> getProductByNo(@PathVariable("productNo") int productNo) {
		return productService.getProductByNo(productNo);
	}

//	상품 추가
	@PostMapping("/insertProduct")
	public ResponseEntity<?> insertProduct(@RequestPart(value = "data") ProductVO requestData,
			@RequestParam("fileList") List<MultipartFile> fileList) throws NotFoundException {
		return productService.insertProduct(requestData, fileList);
	}

//	상품 수정
	@PutMapping("/updateProduct")
	public ResponseEntity<?> updateProduct(@RequestPart(value = "data") ProductVO requestData,
			@RequestParam(value = "file1", required = false) List<MultipartFile> file1,
			@RequestParam(value = "file2", required = false) List<MultipartFile> file2) throws NotFoundException {
		return productService.updateProduct(requestData, file1, file2);
	}

//	판매 여부 변경
	@PatchMapping("/updateOnSale/{productNo}")
	public ResponseEntity<?> updateOnSale(@PathVariable("productNo") int proudctNo) {
		return productService.updateOnSale(proudctNo);
	}

//	서버에서 이미지 가져오기
	@GetMapping("/productImage/{productNo}/{image}")
	public ResponseEntity<?> productimage(@PathVariable("productNo") int productNo, @PathVariable("image") String image)
			throws IOException {
		InputStream imageStream;
		try {
			imageStream = new FileInputStream("./images/product/" + productNo + "/product/" + image);
		} catch (FileNotFoundException e) {
			imageStream = new FileInputStream("./images/error.png");
		}
		byte[] imageByteArray = IOUtils.toByteArray(imageStream);
		imageStream.close();
		return new ResponseEntity<byte[]>(imageByteArray, HttpStatus.OK);
	}

	@GetMapping("/detailImage/{productNo}/{image}")
	public ResponseEntity<?> detailimage(@PathVariable("productNo") int productNo, @PathVariable("image") String image)
			throws IOException {
		InputStream imageStream;
		try {
			imageStream = new FileInputStream("./images/product/" + productNo + "/detail/" + image);
		} catch (FileNotFoundException e) {
			imageStream = new FileInputStream("./images/error.png");
		}
		byte[] imageByteArray = IOUtils.toByteArray(imageStream);
		imageStream.close();
		return new ResponseEntity<byte[]>(imageByteArray, HttpStatus.OK);
	}

}
