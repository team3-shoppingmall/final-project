package com.myspring.spring.point;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PointMapper {
//	포인트 내역 추가(orderService에서 호출)
	@Insert("insert into pointtable(id, point, content) values(#{in.id}, #{in.point}, #{in.content})")
	int insertPoint(@Param("in") PointVO in);
	
//	포인트 내역 조회
	@Select("select * from pointtable where id = #{id} order by num desc limit #{start}, #{perPage}")
	List<PointVO> getPointList(@Param("start") int start, @Param("perPage") int perPage, @Param("id") String id);

//	소멸 예정 포인트 조회
	@Select("select sum(point) from pointtable where id = #{id} BETWEEN DATE_ADD (NOW(), INTERVAL -6 MONTH) AND DATE_ADD (NOW(), INTERVAL -5 MONTH)")
	List<PointVO> getExpirePoint(@Param("id") String id);

	// 관심 상품 개수 가져오기
	@Select("select count(*) from pointtable where id = #{id}")
	int getPointListCount(@Param("id") String id);

}
