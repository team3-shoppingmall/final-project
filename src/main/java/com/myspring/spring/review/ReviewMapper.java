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
	
	// 전체 개수 가져오기
	@Select("select count(*) from noticetable where ${search} like CONCAT('%',#{searchWord},'%')")
	public int getCount(@Param("search") String search, @Param("searchWord") String searchWord);
		
	//리뷰 전체보기
	@Select("select * from reviewtable where ${search} like CONCAT('%', #{searchWord}, '%') order by reviewno desc limit #{start}, #{perPage}")
	public List<ReviewVO> getAllreviews(@Param("start") int start, @Param("perPage") int perPage, 
				@Param("search") String search, @Param("searchWord") String searchWord);
	
	//리뷰 작성
	@Insert("insert into reviewtable(content, id, image, star) values(#{in.content}, #{in.id}, #{in.image}, #{in.star})")
	public int insertReview(@Param("in") ReviewVO reviewVO);
	
	//리뷰 삭제
	@Delete("delete from reviewtable where id = #{id}")
	public int deleteReview(@Param("id") String id);
	
	//리뷰 상세보기
	@Select("select * from reviewtable where reviewNo = #{reviewNo}")
	public List<ReviewVO> getFindByReviewNo(int reviewNo);
	
	//리뷰검색 by content
	@Select("select * from reviewtable where content like '%${content}%'")
	public List<ReviewVO> searchReviewByContent(String content);
	
	//리뷰검색 by id
	@Select("select * from reviewtable where id = #{id}")
	public List<ReviewVO> searchReviewById(String id);


}
