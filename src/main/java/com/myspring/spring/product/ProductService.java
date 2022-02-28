package com.myspring.spring.product;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductService {
	private ProductMapper productMapper;

	@Autowired
	public ProductService(ProductMapper productMapper) {
		this.productMapper = productMapper;
	}

	public ResponseEntity<?> getProductAll(int page, int perPage, String type1, String type2, String search,
			String searchWord1, String searchWord2) {
		int start = (page - 1) * perPage;
		List<ProductVO> productList = productMapper.getProductAll(start, perPage, type1, type2, search, searchWord1,
				searchWord2);
		int count = productMapper.getProductAllCount(type1, type2, search, searchWord1, searchWord2);
		Map<String, Object> resMap = new HashMap<>();
		resMap.put("productList", productList);
		resMap.put("count", count);
		return new ResponseEntity<>(resMap, HttpStatus.OK);
	}

	// 상품 리스트 조회
	public ResponseEntity<?> getProductList(int page, int perPage, String type1, String type2, String searchWord,
			int minPrice, int maxPrice, String searchOrder) {
		int start = (page - 1) * perPage;
		List<ProductVO> productList = productMapper.getProductList(start, perPage, type1, type2, searchWord, minPrice,
				maxPrice, searchOrder);
		int count = productMapper.getProductCount(type1, type2, searchWord, minPrice, maxPrice);
		Map<String, Object> resMap = new HashMap<>();
		resMap.put("productList", productList);
		resMap.put("count", count);
		return new ResponseEntity<>(resMap, HttpStatus.OK);

	}

	// 많이 팔린 상품 조회
	public ResponseEntity<?> getBestProductList(String type1, String type2) {
		List<ProductVO> res = productMapper.getBestProductList(type1, type2);
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// 상품 정보 조회
	public ResponseEntity<?> getProductByNo(int productNo) {
		ProductVO res = productMapper.getProductByNo(productNo);
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
//insert
	public ResponseEntity<?> insertProduct(ProductVO requestData, List<MultipartFile> fileList) {
		ProductVO result = new ProductVO();
		ResponseEntity<?> entity = null;

		try {
			productMapper.insertProduct(requestData, result);
			int productNo = result.getProductNo();
			String[] imageName = requestData.getImageName().split(";");
			String[] detailImageName = requestData.getDetailImageName().split(";");
			File file = new File("./images/product/" + productNo + "/");
			file.mkdir();
			file = new File("./images/product/" + productNo + "/product/");
			file.mkdir();
//			file = new File("./images/product/" + productNo + "/detail/");
//			file.mkdir();

			for (int i = 0; i < imageName.length; i++) {
				MultipartFile multipartFile = fileList.get(i);
				FileOutputStream writer = new FileOutputStream(
						"./images/product/" + productNo + "/product/" + multipartFile.getOriginalFilename());
//				System.out.println(multipartFile.getOriginalFilename());
				writer.write(multipartFile.getBytes());
				writer.close();
			}
//			for (int i = imageName.length; i < detailImageName.length + imageName.length; i++) {
//				MultipartFile multipartFile = fileList.get(i);
//				FileOutputStream writer = new FileOutputStream(
//						"./images/product/" + productNo + "/detail/" + multipartFile.getOriginalFilename());
////				System.out.println(multipartFile.getOriginalFilename());
//				writer.write(multipartFile.getBytes());
//				writer.close();
//			}
			entity = new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return entity;
	}
	
//update
	public ResponseEntity<?> updateProduct(ProductVO requestData, List<MultipartFile> fileList) {
		ResponseEntity<?> entity = null;

		try {
			int res = productMapper.updateProduct(requestData);
			if (res == 0) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			File file;
			File[] underDir;

//			폴더 내 모든 파일 삭제
			file = new File("./images/product/" + requestData.getProductNo() + "/product/");
			underDir = file.listFiles();
			for (int i = 0; i < underDir.length; i++) {
				underDir[i].delete();
			}
			file = new File("./images/product/" + requestData.getProductNo() + "/detail/");
			underDir = file.listFiles();
			for (int i = 0; i < underDir.length; i++) {
				underDir[i].delete();
			}

			String[] imageName = requestData.getImageName().split(";");
			String[] detailImageName = requestData.getDetailImageName().split(";");

			for (int i = 0; i < imageName.length; i++) {
				MultipartFile multipartFile = fileList.get(i);
				FileOutputStream writer = new FileOutputStream("./images/product/" + requestData.getProductNo()
						+ "/product/" + multipartFile.getOriginalFilename());
//				System.out.println(multipartFile.getOriginalFilename());
				writer.write(multipartFile.getBytes());
				writer.close();
			}
			for (int i = imageName.length; i < detailImageName.length + imageName.length; i++) {
				MultipartFile multipartFile = fileList.get(i);
				FileOutputStream writer = new FileOutputStream("./images/product/" + requestData.getProductNo()
						+ "/detail/" + multipartFile.getOriginalFilename());
//				System.out.println(multipartFile.getOriginalFilename());
				writer.write(multipartFile.getBytes());
				writer.close();
			}

			entity = new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return entity;
	}

	public ResponseEntity<?> updateOnSale(int productNo) {
		int res = productMapper.updateOnSale(productNo);
		if (res == 0)
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(HttpStatus.OK);

	}
	
	public ResponseEntity<?> deleteProduct(int productNo) {
		int res = productMapper.deleteProduct(productNo);
		if (res == 0)
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(HttpStatus.OK);

	}
}
