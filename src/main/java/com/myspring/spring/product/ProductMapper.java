package com.myspring.spring.product;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

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

	@Insert("insert into producttable(productName, type1,type2, imageName, price, discount, color, size, amount, detailImageName, amount) "
			+ "values (#{in.productName}, #{in.type1}, #{in.type2}, #{in.imageName}, #{in.price}, #{in.discount}, #{in.color}, #{in.size},  #{in.amount}, #{in.detailimagename})")
	@Options(useGeneratedKeys = true, keyProperty = "result.productNo", keyColumn = "productNo")
	int insertProduct(@Param("in") ProductVO in, @Param("result") ProductVO result);

	@Update("update producttable set productName = #{in.productName}, type1 = #{in.type1}, type2 = #{in.type2}, price = #{in.discount}, "
			+ "discount = #{in.discount}, color = #{in.color}, size = #{in.size}, amount= #{in.amount} , "
			+ "imageName = #{in.imageName}, detailImagename = #{in.detailImagename} where productNo = #{in.productNo}")
	int updateProduct(@Param("in") ProductVO in, @Param("result") ProductVO result);

}