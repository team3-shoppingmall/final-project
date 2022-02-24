package com.myspring.spring.basket;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BasketMapper {
	
	// 장바구니 추가
	@Insert("insert into baskettable(id, productNo, selectedColor, selectedSize, amount) values (#{in.id}, #{in.productNo}, #{in.selectedColor}, #{in.selectedSize}, #{in.amount})")
	int insertBakset(@Param("in") BasketVO basket);
	
	@Select("select * from baskettable where id = #{id}")
	BasketVO getBasketById(@Param("id") String id);
}
