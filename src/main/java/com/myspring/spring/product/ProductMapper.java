package com.myspring.spring.product;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.myspring.spring.member.MemberUtils;
import com.myspring.spring.member.MemberVO;

@Mapper
public interface ProductMapper {

	// 상품 리스트 조회
	@SelectProvider(type = ProductUtils.class, method = "getProductListByType")
	public List<ProductVO> getProductListByType(int start, int perPage, String type1, String type2, String searchWord,
			int minPrice, int maxPrice, String searchOrder);

	// 상품 리스트 전체 개수 조회
	@SelectProvider(type = ProductUtils.class, method = "getProductCountByType")
	int getProductCountByType(String type1, String type2, String searchWord, int minPrice, int maxPrice,
			String searchOrder);

	// 많이 팔린 상품 조회
	@SelectProvider(type = ProductUtils.class, method = "getBestProductListByType")
	List<ProductVO> getBestProductListByType(String type1, String type2);

	// 상품 정보 조회
	@Select("select * from producttable where productNo = #{productNo}")
	public ProductVO getProductByNo(@Param("productNo") int productNo);

}