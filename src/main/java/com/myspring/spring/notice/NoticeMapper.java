package com.myspring.spring.notice;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface NoticeMapper {

	// 전체 개수 가져오기
	@Select("select count(*) from noticetable where ${search} like CONCAT('%',#{searchWord},'%')")
	public int getCount(@Param("search") String search, @Param("searchWord") String searchWord);

	// 공지사항 목록 출력
	@Select("select * from noticetable where ${search} like CONCAT('%', #{searchWord}, '%') order by noticeno desc limit #{start}, #{perPage}")
	public List<NoticeVO> getNotice(@Param("start") int start, @Param("perPage") int perPage,
			@Param("search") String search, @Param("searchWord") String searchWord);
	
	// 공지사항 게시물 보기
	@Select("select * from noticetable where noticeNo = #{noticeNo}")
	public NoticeVO getNoticeFindByID(@Param("noticeNo") int noticeNo);

	// 공지사항 삭제
	@Delete("delete from noticetable where noticeNo = #{noticeNo}")
	public int deleteNotice(@Param("noticeNo") int noticeNo);
	
	// 공지사항 작성 + 파일
	@Insert("insert into noticetable(title, content, id, image) values(#{in.title}, #{in.content}, #{in.id}, #{in.image})")
	@Options(useGeneratedKeys = true, keyProperty = "result.noticeNo", keyColumn = "noticeNo")
	public void insertNotice(@Param("in") NoticeVO in, @Param("result") NoticeVO result);

	//공지사항 수정 + 파일
	@Update("update noticetable set title=#{in.title}, content=#{in.content}, image=#{in.image} where noticeNo=#{in.noticeNo}")
	public int updateNotice(@Param("in") NoticeVO in);

}


