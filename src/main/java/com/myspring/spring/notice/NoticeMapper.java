package com.myspring.spring.notice;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.http.ResponseEntity;

@Mapper
public interface NoticeMapper {

	//공지사항 목록 출력
	@Select("select * from noticetable")
	public List<NoticeVO> getAllMembers();
	
	//공지사항 게시물 보기
	@Select("select * from noticetable where noticeNo = #{noticeNo}")
	public NoticeVO getMemberFindByID(@Param("noticeNo") int noticeNo);

	//공지사항 삭제
	@Delete("delete from noticetable where noticeNo = #{noticeNo}")
	public int deleteMember(@Param("noticeNo") int noticeNo);

	//공지사항 게시물 작성
	@Insert("insert into noticetable(title, content, id, image) "
			+ "values(#{in.title}, #{in.content}, #{in.id}, #{in.image})")
	public ResponseEntity<?> insertMember(@Param("in") NoticeVO noticeVO);

	//공지사항 수정
	@Update("update noticetable set title=#{title}, content=#{content}, image=#{image} where noticeNo=#{noticeNo}")
	public int updateMember(@Param("noticeNo") int noticeNo, @Param("title") String title, @Param("content") String content, @Param("image") String image);

	
}
