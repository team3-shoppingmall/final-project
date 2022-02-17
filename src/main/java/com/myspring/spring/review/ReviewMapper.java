package com.myspring.spring.review;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.http.ResponseEntity;

@Mapper
public interface ReviewMapper {
	
	//리뷰 리스트 보기
	@Select("select * from reviewtable")
	public List<ReviewVO> getAllreviews();
	
	//리뷰 작성
	@Insert("insert into reviewtable(content, id, image, star) values(#{in.content}, #{in.id}, #{in.image}, #{in.star})")
	int insertReview(@Param("in") ReviewVO reviewVO);
	
	//리뷰 삭제
	@Delete("delete from reviewtable where id = #{id}")
	public int deleteReview(@Param("id") String id);
	
	//리뷰 상세 보기
	@Select("select * from reviewtable where reviewNo = #{reviewNo}")
	public ReviewVO getReviewFindByID(int reviewNo);


}
