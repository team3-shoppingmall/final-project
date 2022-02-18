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

	// 전체 개수 가져오기
	@Select("select count(*) from noticetable where ${search} like CONCAT('%',#{searchKeyword},'%')")
	public int getCount(@Param("search") String search, @Param("searchWord") String searchKeyword);

//	// 공지사항 목록 출력
//	@Select("select * from noticetable order by noticeno desc limit #{start}, #{perPage}")
//	public List<NoticeVO> getAllMembers(@Param("start") int start, @Param("perPage") int perPage);

//	현재 #{search} 부분에 오류 있으니 title로 바꿔서 확인할 것
	// 공지사항 목록 출력
	@Select("select * from noticetable where ${search} like CONCAT('%', #{searchKeyword}, '%') order by noticeno desc limit #{start}, #{perPage}")
	public List<NoticeVO> getNotice(NoticeVO noticeVO);

	// 공지사항 게시물 보기
	@Select("select * from noticetable where noticeNo = #{noticeNo}")
	public NoticeVO getNoticeFindByID(NoticeVO noticeVO);

	// 공지사항 삭제
	@Delete("delete from noticetable where noticeNo = #{noticeNo}")
	public int deleteNotice(@Param("noticeNo") int noticeNo);

	// 공지사항 게시물 작성
	@Insert("insert into noticetable(title, content, id, image) "
			+ "values(#{in.title}, #{in.content}, #{in.id}, #{in.image})")
	public int insertNotice(@Param("in") NoticeVO noticeVO);

//	// 공지사항 수정
//	@Update("update noticetable set title=#{title}, content=#{content}, image=#{image} where noticeNo=#{noticeNo}")
//	public int updateNotice( @Param("noticeNo") int noticeNo, @Param("title") String title, @Param("content") String content, @Param("image") String image);

	@Update("update noticetable set title=#{title}, content=#{content}, image=#{image} where noticeNo=#{noticeNo}")
	public int updateNotice(NoticeVO noticeVO);
	
	
	//**여기서부터 페이징 코드 추가중**
	
	// 게시글 페이징 출력
	@Select("select * from noticetable order by noticeno desc limit #{startPage}, #{recordsPerPage}")
	public List<NoticeVO> selectNoticeList(NoticeVO noticeVO);
	
	// 게시판 테이블에서 검색 조건에 해당하는 게시글의 개수를 확인하기 위한 용도
	@Select("select count(*) from noticetable")
	public int selectNoticeTotalCount();
}


