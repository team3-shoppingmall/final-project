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

	// 상품 리스트 조회
	public ResponseEntity<?> getProductList(int page, int perPage, String type1, String type2, String searchWord,
			int minPrice, int maxPrice, String searchOrder) {
		int start = (page - 1) * perPage;
		List<ProductVO> productList = productMapper.getProductList(start, perPage, type1, type2, searchWord, minPrice,
				maxPrice, searchOrder);
		int count = productMapper.getProductCount(type1, type2, searchWord, minPrice, maxPrice);
		if (productList == null || count == 0)
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		else {
			Map<String, Object> resMap = new HashMap<>();
			resMap.put("productList", productList);
			resMap.put("count", count);
			return new ResponseEntity<>(resMap, HttpStatus.OK);
		}

	}

	// 많이 팔린 상품 조회
	public ResponseEntity<?> getBestProductList(String type1, String type2) {
		List<ProductVO> res = productMapper.getBestProductList(type1, type2);
		if (res == null)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}

	// 상품 정보 조회
	public ResponseEntity<?> getProductByNo(int productNo) {
		ProductVO res = productMapper.getProductByNo(productNo);
		if (res == null)
			return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
		else
			return new ResponseEntity<>(res, HttpStatus.OK);
	}

	public ResponseEntity<?> insertProduct(ProductVO requestData, List<MultipartFile> fileList) {
		ProductVO result = new ProductVO();
		ResponseEntity<?> entity = null;

		try {
			productMapper.insertProduct(requestData, result);
			int productno = result.getProductNo();
			File file = new File("./src/main/resources/images/product/" + productno + "/");
			file.mkdir();
			String[] path = { "/product/", "/detail/" };
			file = new File("./images/product/" + productno + path[0]);
			file.mkdir();
			file = new File("./images/product/" + productno + path[1]);
			file.mkdir();

			for (int i = 0; i < fileList.size(); i++) {
				MultipartFile multipartFile = fileList.get(i);
				FileOutputStream writer = new FileOutputStream(
						"./images/product/" + productno + path[i] + multipartFile.getOriginalFilename());
				System.out.println(multipartFile.getOriginalFilename());
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

	public ResponseEntity<?> updateProduct(ProductVO requestData, List<MultipartFile> file1,
			List<MultipartFile> file2) {
		ProductVO result = new ProductVO();
		ResponseEntity<?> entity = null;

		try {
			productMapper.updateProduct(requestData, result);
//			int productno = result.getProductno();
			File file;
			File[] underDir;
			FileOutputStream writer;
			String[] path = { "/product/", "/detail/" };

			if (file1 != null) {
				file = new File("./images/product/" + requestData.getProductNo() + path[0]);
				underDir = file.listFiles();
				for (int i = 0; i < underDir.length; i++) {
					underDir[i].delete();
				}
				writer = new FileOutputStream("./src/main/resources/images/product/" + requestData.getProductNo()
						+ path[0] + file1.get(0).getOriginalFilename());
				writer.write(file1.get(0).getBytes());
				writer.close();
			}
			if (file2 != null) {
				file = new File("./images/product/" + requestData.getProductNo() + path[1]);
				underDir = file.listFiles();
				for (int i = 0; i < underDir.length; i++) {
					underDir[i].delete();
				}
				writer = new FileOutputStream("./images/product/" + requestData.getProductNo() + path[1]
						+ file2.get(0).getOriginalFilename());
				writer.write(file2.get(0).getBytes());
				writer.close();
			}

			entity = new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return entity;
	}
}
