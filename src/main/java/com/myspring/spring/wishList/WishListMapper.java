package com.myspring.spring.wishList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface WishListMapper {

	// 관심 상품 추가
	@Insert("insert into wishlisttable(id, productNo) values (#{in.id}, #{in.productNo})")
	int insertWishList(@Param("in") WishListVO wishList);
		
	// 관심 상품 가져오기
	@Select("select * from wishlisttable where id = #{id}")
	WishListVO getWishListById(@Param("id") String id);
}
