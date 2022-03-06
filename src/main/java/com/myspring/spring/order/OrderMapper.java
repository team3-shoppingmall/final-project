package com.myspring.spring.order;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface OrderMapper {

//	주문 등록(첫번째)
	@Insert("insert into ordertable(orderIdx, id, productNo, orderNo, selectedColor, selectedSize, orderAmount, totalPrice, orderMethod, name, tel, zipCode, address, detailAddr) "
			+ "values((select A.num from (SELECT MAX(orderIdx)+1 as num FROM ordertable) A), #{in.id}, #{in.productNo}, (select A.num from (SELECT MAX(orderIdx)+1 as num FROM ordertable) A), "
			+ "#{in.selectedColor}, #{in.selectedSize}, #{in.orderAmount}, #{in.totalPrice}, #{in.orderMethod}, #{in.name}, #{in.tel}, #{in.zipCode}, #{in.address}, #{in.detailAddr})")
	@Options(useGeneratedKeys = true, keyProperty = "result.orderIdx", keyColumn = "orderIdx")
	public int insertOrderFirst(@Param("in") OrderVO in, @Param("result") OrderVO result);

//	주문 등록(나머지)
	@Insert("insert into ordertable(id, productNo, orderNo, selectedColor, selectedSize, orderAmount, totalPrice, orderMethod, name, tel, zipCode, address, detailAddr) "
			+ "values(#{in.id}, #{in.productNo}, #{orderNo}, #{in.selectedColor}, #{in.selectedSize}, #{in.orderAmount}, #{in.totalPrice}, #{in.orderMethod}, #{in.name}, #{in.tel}, #{in.zipCode}, #{in.address}, #{in.detailAddr})")
	public int insertOrder(@Param("in") OrderVO in, @Param("orderNo") long orderNo);

//	주문 조회
	@SelectProvider(type = OrderUtils.class, method = "getOrder")
	public List<OrderAndProductVO> getOrder(int start, int perPage, String state, String search, String searchWord1,
			String searchWord2);

//	주문 개수 조회
	@SelectProvider(type = OrderUtils.class, method = "getOrderCount")
	int getOrderCount(String state, String search, String searchWord1, String searchWord2);

//	판매 정산 조회
	@SelectProvider(type = OrderUtils.class, method = "getSalesSettlement")
	public List<OrderAndProductVO> getSalesSettlement(int start, int perPage, String search, String searchWord1,
			String searchWord2);

//	판매 정산 개수 조회
	@SelectProvider(type = OrderUtils.class, method = "getSalesSettlementCount")
	public List<OrderAndProductVO> getSalesSettlementCount(String search, String searchWord1, String searchWord2);

//	주문 조회
	@SelectProvider(type = OrderUtils.class, method = "getOrderById")
	public List<OrderAndProductVO> getOrderById(int start, int perPage, String pageInfo, String state,
			String searchWord, String searchDate1, String searchDate2, String id);

//	주문 개수 조회
	@SelectProvider(type = OrderUtils.class, method = "getOrderByIdCount")
	int getOrderByIdCount(String pageInfo, String state, String searchWord, String searchDate1, String searchDate2,
			String id);

//	마이 페이지 메인 각각 주문 개수 조회
	@Select("select state, count(*) as orderNo from ordertable where id = #{id} group by state")
	List<OrderVO> getOrdersByIdGroupByState(@Param("id") String id);

//  주문 상태 변경
	@Update("update ordertable set state = #{state} where orderIdx = #{orderIdx}")
	int updateOrder(@Param("orderIdx") long orderIdx, @Param("state") String state);
}
