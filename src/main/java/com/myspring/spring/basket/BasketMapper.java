package com.myspring.spring.basket;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface BasketMapper {

	// 장바구니 추가
	@Insert("insert into baskettable(id, productNo, selectedColor, selectedSize, amount) values (#{in.id}, #{in.productNo}, #{in.selectedColor}, #{in.selectedSize}, #{in.amount})")
	int insertBakset(@Param("in") BasketVO basket);

//	장바구니 조회
	@Select("select * from baskettable left join producttable on baskettable.productNo = producttable.productNo where id = #{id}")
	List<BasketAndProductVO> getBasketById(@Param("id") String id);

//	장바구니 개수 변경
	@Update("update baskettable set basketAmount = #{basketAmount} where basketIdx = #{basketIdx}")
	int updateBasketAmount(@Param("basketIdx") long basketIdx, @Param("basketAmount") int basketAmount);

//	장바구니 삭제
	@Delete("Delete from baskettable where basketIdx = #{basketIdx}")
	int deleteBasket(@Param("basketIdx") long basketIdx);
}
