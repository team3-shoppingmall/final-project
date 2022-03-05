package com.myspring.spring.order;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

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
	
//	주문 조회(주문 관리)
	
}
