package com.myspring.spring.banner;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.myspring.spring.product.ProductVO;

@Service
public class BannerService {

	public ResponseEntity<?> insertBanner(BannerVO data, List<MultipartFile> banner) {
		try {
			System.out.println(data.getImage());
			System.out.println(data.getLink());
			System.out.println(data.getNum());
			
		} 
		catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

//	ProductVO result = new ProductVO();
//	ResponseEntity<?> entity = null;
//
//	try {
//		productMapper.insertProduct(requestData, result);
//		int productNo = result.getProductNo();
//		String[] imageName = requestData.getImageName().split(";");
//		String[] detailImageName = requestData.getDetailImageName().split(";");
//		File file = new File("./images/product/" + productNo + "/");
//		file.mkdir();
//		file = new File("./images/product/" + productNo + "/product/");
//		file.mkdir();
//		file = new File("./images/product/" + productNo + "/detail/");
//		file.mkdir();
//
//		for (int i = 0; i < imageName.length; i++) {
//			MultipartFile multipartFile = fileList.get(i);
//			FileOutputStream writer = new FileOutputStream(
//					"./images/product/" + productNo + "/product/" + multipartFile.getOriginalFilename());
//			System.out.println(multipartFile.getOriginalFilename());
//			writer.write(multipartFile.getBytes());
//			writer.close();
//		}
//		for (int i = imageName.length; i < detailImageName.length + imageName.length; i++) {
//			MultipartFile multipartFile = fileList.get(i);
//			FileOutputStream writer = new FileOutputStream(
//					"./images/product/" + productNo + "/detail/" + multipartFile.getOriginalFilename());
//			System.out.println(multipartFile.getOriginalFilename());
//			writer.write(multipartFile.getBytes());
//			writer.close();
//		}
//		entity = new ResponseEntity<>(HttpStatus.OK);
//
//	} catch (Exception e) {
//		e.printStackTrace();
//		entity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//	}
//	return entity;
}
