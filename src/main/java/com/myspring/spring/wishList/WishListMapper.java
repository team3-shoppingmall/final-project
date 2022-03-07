package com.myspring.spring.wishList;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
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
	@Select("select * from wishlisttable left join producttable on wishlisttable.productNo = producttable.productNo where id = #{id} limit #{start}, #{perPage}")
	List<WishListAndProductVO> getWishListById(@Param("start") int start, @Param("perPage") int perPage,
			@Param("id") String id);

	// 관심 상품 개수 가져오기
	@Select("select count(*) from wishlisttable left join producttable on wishlisttable.productNo = producttable.productNo where id = #{id}")
	int getWishListCountById(@Param("id") String id);

//	관심 상품 삭제
	@Delete("Delete from wishlisttable where id = #{in.id} and productNo = #{in.productNo}")
	int deleteWishList(@Param("in") WishListVO in);
}
