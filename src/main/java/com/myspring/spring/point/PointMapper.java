package com.myspring.spring.point;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PointMapper {
    
//	@Select("select * from pointtable where id = 'tester' order by pointdate desc;")
	@Select("select * from pointtable where id = #{id} order by pointdate desc;")
	List<PointVO> getPointAll(@Param("id") String id);

}
