package com.myspring.spring.product;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ProductMapper {

	// 상품 리스트 조회, 대분류만
	@Select("select * from producttable where UPPER(type1) = UPPER(#{type1}) limit #{start}, #{perPage}")
	public List<ProductVO> getProductListByType1(@Param("start") int start, @Param("perPage") int perPage, @Param("type1") String type1);

	// 상품 리스트 조회, 소분류도
	@Select("select * from producttable where UPPER(type1) = UPPER(#{type1}) and UPPER(type2) = UPPER(#{type2}) limit #{start}, #{perPage}")
	public List<ProductVO> getProductListByTypeAll(@Param("start") int start, @Param("perPage") int perPage, @Param("type1") String type1, @Param("type2") String type2);

	// 상품 리스트 전체 개수 조회, 대분류만
	@Select("select count(*) from producttable where UPPER(type1) = UPPER(#{type1})")
	int getProductCountByType1(@Param("type1") String type1);

	// 상품 리스트 전체 개수 조회, 소분류도
	@Select("select count(*) from producttable where UPPER(type1) = UPPER(#{type1}) and UPPER(type2) = UPPER(#{type2})")
	int getCountByTypeAll(@Param("type1") String type1, @Param("type2") String type2);

	// 많이 팔린 상품 조회
	@Select("select * from producttable p left join ordertable o on p.productno = o.productno "
			+ "where UPPER(type1) = UPPER(#{type1}) group by o.productno order by sum(o.amount) desc limit 0,8")
	public List<ProductVO> getBestProductListByType1(@Param("type1") String type1);

	// 많이 팔린 상품 조회
	@Select("select * from producttable p left join ordertable o on p.productno = o.productno "
			+ "where UPPER(type1) = UPPER(#{type1}) and UPPER(type2) = UPPER(#{type2}) group by o.productno order by sum(o.amount) desc limit 0,8")
	public List<ProductVO> getBestProductListByTypeAll(@Param("type1") String type1, @Param("type2") String type2);

	// 상품 정보 조회
	@Select("select * from producttable where productNo = #{productNo}")
	public ProductVO getProductByNo(@Param("productNo") int productNo);

}
