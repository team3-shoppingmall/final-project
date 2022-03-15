package com.myspring.spring.member;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

@Mapper
public interface MemberMapper {
	// 멤버 등록
	@Insert("insert into membertable(id,password,name,tel,email,zipcode,addr1,addr2, terms, authority) values (#{in.id},#{in.password},#{in.name},#{in.tel},#{in.email},#{in.zipcode},#{in.addr1},#{in.addr2}, #{in.terms}, #{in.authority})")
	int insertMember(@Param("in") MemberVO member);

	// 전체 멤버 조회
	// @Select("select id, name, tel, email, zipcode, addr1, addr2, terms, point
	// from membertable limit #{perPage} offset #{start}")
	// List<MemberVO> getMemberAll(@Param("start") int start, @Param("perPage") int
	// perPage);

	// 전체 멤버 수 조회
	@SelectProvider(type = MemberUtils.class, method = "getCount")
//	@Select("select count(id) from membertable")
	int getMemberCount(String condition, Object param);

	// 멤버 조회
	// select * from membertable where ? like %?%

	@SelectProvider(type = MemberUtils.class, method = "getMembers")
	List<MemberVO> getMembers(int start, int perPage, String condition, Object param);

	// 멤버 정보 조회
	// select ~ from membertable where id ='id'
	// pointService에서 멤버의 포인트 얻기 위해 호출도 함
	@Select("select * from membertable where id = #{id}")
	MemberVO getMemberInfo(@Param("id") String id);

	// 멤버 정보 수정
	// update membertable set ? = ?, ... where id = ?
	@UpdateProvider(type = MemberUtils.class, method = "updateMember")
	int updateMember(MemberVO member);

	// 로그인
	@Select("select * from membertable where id = #{id}")
	MemberVO login(String id);

	// 멤버 포인트 조회
	@Select("select point from membertable where id = 'tester';")
	List<MemberVO> getMemberPoint();

	@Delete("delete from membertable where id = #{id} and 0 = (select count(*) from ordertable where id = #{id} and STATE in ('입금전', '결제완료', '배송준비중', '배송중'))")
	int deleteMember(@Param("id") String id);

}
