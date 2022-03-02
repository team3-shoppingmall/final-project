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
	@Insert("insert into baskettable(id, productNo, selectedColor, selectedSize, basketAmount) values (#{in.id}, #{in.productNo}, #{in.selectedColor}, #{in.selectedSize}, #{in.basketAmount})")
	int insertBakset(@Param("in") BasketVO basket);

//	장바구니 추가 전 중복 여부
	@Select("select count(*) from baskettable where id = #{in.id} and productNo = #{in.productNo} and selectedColor = #{in.selectedColor} and selectedSize = #{in.selectedSize}")
	int basketCheck(@Param("in") BasketVO basket);

//	장바구니 조회
	@Select("select * from baskettable left join producttable on baskettable.productNo = producttable.productNo where id = #{id}")
	List<BasketAndProductVO> getBasketById(@Param("id") String id);

//	장바구니 개수 조회
	@Select("select count(*) from baskettable where id = #{id}")
	int getBasketCountById(@Param("id") String id);

//	장바구니 개수 변경
	@Update("update baskettable set basketAmount = #{basketAmount} where basketIdx = #{basketIdx}")
	int updateBasketAmount(@Param("basketIdx") long basketIdx, @Param("basketAmount") int basketAmount);

//	장바구니 삭제
//	주문 시 장바구니 삭제도 이거로 작동(orderService에서 호출)
	@Delete("Delete from baskettable where basketIdx = #{basketIdx}")
	int deleteBasket(@Param("basketIdx") long basketIdx);
}
