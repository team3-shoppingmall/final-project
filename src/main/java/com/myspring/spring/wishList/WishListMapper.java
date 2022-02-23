package com.myspring.spring.wishList;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface WishListMapper {
	// 멤버 등록
	@Insert("insert into membertable(id,password,name,tel,email,zipcode,addr1,addr2,authority) values (#{in.id},#{in.password},#{in.name},#{in.tel},#{in.email},#{in.zipcode},#{in.addr1},#{in.addr2},#{in.authority})")
	int insertMember(@Param("in") WishListVO member);

	// 전체 멤버 조회
	@Select("select id, name, tel, email, zipcode, addr1, addr2, terms, point, authority from membertable")
	List<WishListVO> getAllMembers();

	// 아이디로 멤버 조회
	@Select("select * from membertable where id = #{id}")
	WishListVO getMember(@Param("id") String id);

}
