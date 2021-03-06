package com.myspring.spring.review;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.myspring.spring.qna.QnaUtils;

@Mapper
public interface ReviewMapper {

	// 전체 개수 가져오기(Utils)
	@SelectProvider(type = ReviewUtils.class, method = "getReviewCount")
	int getReviewCount(String search, String searchWord, int productNo, String id);
	
	// 전체 개수 가져오기
	@Select("select count(*) from reviewtable where ${search} like CONCAT('%',#{searchWord},'%')")
	public int getCount(@Param("search") String search, @Param("searchWord") String searchWord);
	
	//리뷰 목록 조회(Utils)
	@SelectProvider(type = ReviewUtils.class, method = "getReviewList")
	List<ReviewAndProductVO> getReivewList(int start, int perPage, String search, String searchWord, int productNo, String id);
	
	// 리뷰 전체보기
	@Select("select * from reviewtable left join producttable on producttable.productNo = reviewtable.productNo where ${search} like CONCAT('%', #{searchWord}, '%') order by reviewno desc limit #{start}, #{perPage}")
	public List<ReviewAndProductVO> getAllReviews(@Param("start") int start, @Param("perPage") int perPage,
			@Param("search") String search, @Param("searchWord") String searchWord);

	// 리뷰 상세보기
	@Select("select * from reviewtable where reviewNo = #{reviewNo}")
	public ReviewVO getReview(int reviewNo);

	// 리뷰 작성
	@Insert("insert into reviewtable(productNo, content, id, image, star) values(#{in.productNo}, #{in.content}, #{in.id}, #{in.image}, #{in.star})")
	@Options(useGeneratedKeys = true, keyProperty = "result.reviewNo", keyColumn = "reviewNo")
	public int insertReview(@Param("in") ReviewVO in, @Param("result") ReviewVO result);

	// 리뷰 삭제
	@Delete("delete from reviewtable where reviewNo = #{reviewNo}")
	public int deleteReview(@Param("reviewNo") int reviewNo);

	// 리뷰 수정
	@Update("update reviewtable set content=#{in.content}, image=#{in.image}, star=#{in.star} where reviewNo = #{in.reviewNo}")
	public int updateReview(@Param("in") ReviewVO in);
}
