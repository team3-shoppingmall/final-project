package com.myspring.spring.banner;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface BannerMapper {

	@Insert("insert into bannertable(image, link) values(#{in.image}, #{in.link})")
	int insertBanner(@Param("in") BannerVO data);

	@Update("update bannertable set link = #{in.link}, num = #{in.num}, image = #{in.image} where idx = #{in.idx}")
	int updateBanner(@Param("in") BannerVO data);
	
	@Select("Select * from bannertable limit #{perPage} offset #{start}")
	List<BannerVO> getBanners(@Param("start") int start, @Param("perPage") int perPage);
	
}
