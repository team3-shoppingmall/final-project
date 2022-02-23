package com.myspring.spring.member;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

@Mapper
public interface MemberMapper {
	// 멤버 등록
	@Insert("insert into membertable(id,password,name,tel,email,zipcode,addr1,addr2,authority) values (#{in.id},#{in.password},#{in.name},#{in.tel},#{in.email},#{in.zipcode},#{in.addr1},#{in.addr2},#{in.authority})")
	int insertMember(@Param("in") MemberVO member);

	// 전체 멤버 조회
	@Select("select id, name, tel, email, zipcode, addr1, addr2, terms, point from membertable")
	List<MemberVO> getMemberAll();

	// 멤버 조회
	@SelectProvider(type = MemberUtils.class, method = "getMembers")
	List<MemberVO> getMembers(String condition, Object param);

}
