package com.myspring.spring.point;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PointMapper {

	@Select("select * from pointtable where id = 'tester' order by pointdate desc;")
	List<PointVO> getPointAll();

//	포인트 내역 추가(orderService에서 호출)
	@Insert("insert into pointtable(id, point) values(#{in.id}, #{in.point})")
	int insertPoint(@Param("in") PointVO in);
}
