package com.myspring.spring.product;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ProductMapper {

	// 상품 관리용 조회
	@SelectProvider(type = ProductUtils.class, method = "getProductAll")
	public List<ProductVO> getProductAll(int start, int perPage, String type1, String type2, String search,
			String searchWord1, String searchWord2);

	// 상품 관리용 전체 개수 조회
	@SelectProvider(type = ProductUtils.class, method = "getProductAllCount")
	int getProductAllCount(String type1, String type2, String search, String searchWord1, String searchWord2);

	// 상품 리스트 조회
	@SelectProvider(type = ProductUtils.class, method = "getProductList")
	public List<ProductVO> getProductList(int start, int perPage, String type1, String type2, String searchWord,
			int minPrice, int maxPrice, String searchOrder);

	// 상품 리스트 전체 개수 조회
	@SelectProvider(type = ProductUtils.class, method = "getProductCount")
	int getProductCount(String type1, String type2, String searchWord, int minPrice, int maxPrice);

	// 많이 팔린 상품 조회
	@SelectProvider(type = ProductUtils.class, method = "getBestProductList")
	List<ProductVO> getBestProductList(String type1, String type2);

	// 상품 정보 조회
	@Select("select * from producttable where productNo = #{productNo}")
	public ProductVO getProductByNo(@Param("productNo") int productNo);

//	메인 화면 이벤트 상품 조회
	@Select("select * from producttable where onSale = true and amount > 0 and discount > 0 order by rand() limit 4")
	public List<ProductVO> getProductEvent();

//	메인 화면 주간 베스트 상품 조회
	@Select("select * from producttable p left join ordertable o on p.productNo = o.productNo where p.onSale = true and p.amount > 0 and "
			+ "(orderDate BETWEEN DATE_ADD(NOW(), INTERVAL -1 week) AND NOW()) group by o.productNo order by sum(o.orderAmount) desc limit 8")
	public List<ProductAndOrderVO> getProductWeeklyBest();

//	메인 화면 신상품 조회(상품 수정된 경우도 신상품 취급)
	@Select("select * from producttable where onSale = true and amount > 0 order by regDate desc limit 8")
	public List<ProductVO> getProductNew();

//	메인 화면 베스트 상품 조회
	@Select("select * from producttable p left join ordertable o on p.productNo = o.productNo where p.onSale = true and p.amount > 0 group by o.productNo order by sum(o.orderAmount) desc limit 8")
	public List<ProductAndOrderVO> getProductBest();

//	상품 추가
	@Insert("insert into producttable(productName, type1, type2, imageName, price, color, size, amount, detailImageName) "
			+ "values (#{in.productName}, #{in.type1}, #{in.type2}, #{in.imageName}, #{in.price}, #{in.color}, #{in.size},  #{in.amount}, #{in.detailImageName})")
	@Options(useGeneratedKeys = true, keyProperty = "result.productNo", keyColumn = "productNo")
	int insertProduct(@Param("in") ProductVO in, @Param("result") ProductVO result);

//	상품 수정
	@Update("update producttable set productName = #{in.productName}, type1 = #{in.type1}, type2 = #{in.type2}, price = #{in.price}, "
			+ "discount = #{in.discount}, color = #{in.color}, size = #{in.size}, amount= #{in.amount} , "
			+ "imageName = #{in.imageName}, regDate = current_timestamp, detailImageName = #{in.detailImageName} where productNo = #{in.productNo}")
	int updateProduct(@Param("in") ProductVO in);

//	상품 판매 여부 변경
	@Update("update producttable set onSale = not onSale where productNo = #{productNo}")
	int updateOnSale(@Param("productNo") int productNo);

//	상품 삭제
	@Delete("delete from producttable where productNo = #{productNo}")
	int deleteProduct(@Param("productNo") int productNo);

}