package com.myspring.spring.banner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BannerService {
	private BannerMapper bannerMapper;

	@Autowired
	public BannerService(BannerMapper bannerMapper) {
		this.bannerMapper = bannerMapper;
	}

	public ResponseEntity<?> insertBanner(BannerVO data, List<MultipartFile> banner) {
		int res = bannerMapper.insertBanner(data);

		if (res == 0) {
			return new ResponseEntity<>("IMAGE NAME EXIST", HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			try {
				MultipartFile multipartFile = banner.get(0);
				FileOutputStream writer = new FileOutputStream("./images/banner/" + data.getImage());
				writer.write(multipartFile.getBytes());
				writer.close();
				return new ResponseEntity<>(HttpStatus.OK);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

//			System.out.println(data.getImage());
//			System.out.println(data.getLink());
//			System.out.println(data.getNum());

		return null;
	}

	public ResponseEntity<?> updateBanner(String old, BannerVO data, List<MultipartFile> banner) {
		
		try {
			File file = new File("./images/banner/" + old);
			if(!old.equals(data.getImage())){
				file.delete();
				try {
					MultipartFile multipartFile = banner.get(0);
					FileOutputStream writer = new FileOutputStream("./images/banner/" + data.getImage());
					writer.write(multipartFile.getBytes());
					writer.close();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			int res = 0;
			
			res = bannerMapper.updateBanner(old, data);
			if (res == 0) {
				return new ResponseEntity<>("IMAGE NAME EXIST", HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getStackTrace(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<?> getImage(String image) throws IOException {
		InputStream imageStream;
		try {
			imageStream = new FileInputStream("./images/banner/" + image);
		} catch (FileNotFoundException e) {
			imageStream = new FileInputStream("./images/error.png");
		}
		byte[] imageByteArray = IOUtils.toByteArray(imageStream);
		imageStream.close();
		return new ResponseEntity<byte[]>(imageByteArray, HttpStatus.OK);
	}

	public ResponseEntity<?> getBanners(int page, int perPage) {
		int start = (page - 1) * perPage;
		List<BannerVO> res = bannerMapper.getBanners(start, perPage);
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	public ResponseEntity<?> deleteBanner(String image) {
		try {
			File file;
			file = new File("./images/banner/" + image);
			file.delete();

			int res = bannerMapper.deleteBanner(image);
			if (res == 0) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e.getStackTrace(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<?> getAllBanners() {
		List<BannerVO> res = bannerMapper.getAllBanners();
		return new ResponseEntity<>(res, HttpStatus.OK);
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
