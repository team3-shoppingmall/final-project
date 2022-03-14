package com.myspring.spring.banner;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface BannerMapper {

	@Insert("call bannerInsert(#{in.image}, #{in.link}, #{in.num})")
	int insertBanner(@Param("in") BannerVO data);

//	@Update("update bannertable set link = #{in.link}, num = #{in.num} where image = #{in.image}")
	@Insert("call bannerUpdate(#{old}, #{new.image}, #{new.link}, #{new.num})")
	int updateBanner(@Param("old") String old, @Param("new") BannerVO data);
	
	@Select("Select * from bannertable  order by num limit #{perPage} offset #{start}")
	List<BannerVO> getBanners(@Param("start") int start, @Param("perPage") int perPage);

	@Insert("call bannerDelete (#{image})")
	int deleteBanner(@Param("image") String image);

	@Select("Select * from bannertable order by num")
	List<BannerVO> getAllBanners();
	
}
