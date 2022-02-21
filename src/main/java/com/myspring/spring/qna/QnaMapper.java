package com.myspring.spring.qna;


import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


@Mapper
public interface QnaMapper {
	// 전체 개수 가져오기
	@Select("select count(*) from qnatable where ${search} like CONCAT('%',#{searchWord},'%')")
	int getCount(@Param("search") String search, @Param("searchWord") String searchWord);
	
	// 게시판 목록 출력
	@Select("select * from qnatable where qnaNo = #{qnaNo}")
	QnaVO getQna(@Param("qnaNo") int qnaNo);
	
	// 문의 게시판 목록 조회
	@Select("select * from qnatable where ${search} like CONCAT('%', #{searchWord}, '%') order by qnaNo desc limit #{start}, #{perPage}")
	List<QnaVO> getQnaWithSearch(@Param("start") int start, @Param("perPage") int perPage, @Param("search") String search, @Param("searchWord") String searchWord);
	
	
	// 문의 전체 조회
	@Select("select * from qnatable order by qnaNo desc")
	List<QnaVO> getQnaAll();
	
	// qnaNo로 1개 문의 가져오기
	@Select("select * from qna where qnaNo = #{qnaNo}")
	QnaVO getQnaByQnaNo(@Param("qnaNo") int qnaNo);
		
	// type별 문의 조회
	@Select("select * from qnatable where type = #{type} order by qnaNo desc")
	List<QnaVO> getQnaByType(@Param("type") String type);
	
	//상품문의 카테고리 전체 조회 & 아이디, 내용, 상품명으로 조회
	@Select("select * from qnatable where ${search} like CONCAT('%', #{searchWord}, '%') AND type in ('general', 'product', 'productNotice', 'reply') order by qnaNo desc limit #{start}, #{perPage}")
	List<QnaVO> getQnaProductAll(@Param("start") int start, @Param("perPage") int perPage, @Param("search") String search, @Param("searchWord") String searchWord);
		
//	// 상품문의 카테고리 전체 조회
//	@Select("select * from qnatable where type in ('general', 'product', 'productNotice', 'reply') order by qnaNo desc")
//	List<QnaVO> getQnaProductAll();
	
	// 배송 문의 카테고리 전체 조회
	@Select("select * from qnatable where ${search} like CONCAT('%', #{searchWord}, '%') AND type in ('cancel', 'change', 'changeaddress', 'cancelNotice', 'reply') order by qnaNo desc")
	List<QnaVO> getQnaBeforeDeliveryAll(@Param("start") int start, @Param("perPage") int perPage, @Param("search") String search, @Param("searchWord") String searchWord);
	
	// 배송 전 변경&취소 카테고리 전체 조회
	@Select("select * from qnatable where ${search} like CONCAT('%', #{searchWord}, '%') AND type in ('delivery', 'deliveryNotice', 'reply') order by qnaNo desc")
	List<QnaVO> getQnaDelieveryAll(@Param("start") int start, @Param("perPage") int perPage, @Param("search") String search, @Param("searchWord") String searchWord);
	
	// 배송 후 교환&반품 카테고리 전체 조회
	@Select("select * from qnatable where ${search} like CONCAT('%', #{searchWord}, '%') AND type in ('return', 'exchange', 'error', 'returnNotice', 'reply') order by qnaNo desc")
	List<QnaVO> getQnaAfterDeliveryAll(@Param("start") int start, @Param("perPage") int perPage, @Param("search") String search, @Param("searchWord") String searchWord);
	
	// 문의 등록 & 댓글 등록
	@Insert("insert into qnatable(type, originalNo, reply, content, id, secret, image) values(#{in.type}, #{in.originalNo}, #{in.reply}, #{in.content}, #{in.id}, #{in.secret}, #{in.image})")
	int insertQna(@Param("in") QnaVO qnaVO);
	
	// 댓글 등록시 reply 업데이트
	@Update("update qnatable set reply = true where qnaNo = #{originalNo}")
	int updateReplyTrue(int originalNo);

	// 문의 수정 & 댓글 수정
	@Update("update qnatable set type=#{type}, content = #{content}, secret = #{secret}, image = #{image} where qnaNo=#{qnaNo}")
	int updateQna(@Param("qnaNo") int qnaNo, @Param("type")String type, @Param("content") String content, 
				  @Param("secret") boolean secret, @Param("image") String image);

	// 문의 삭제 & 댓글 삭제
	@Delete("delete from qnatable where qnaNo = #{qnaNo}")
	int deleteQna(@Param("qnaNo")int qnaNo);

	// 댓글 삭제시 reply 업데이트
	@Update("update qnatable set reply = false where qnaNo = #{originalNo}")
	int updateReplyFalse(@Param("originalNo") int originalNo);
		
	// 아이디로 문의 검색
	@Select("select * from qnatable where id = #{id} order by qnaNo desc")
	List<QnaVO> searchQnaById(@Param("id") String id);

	// 내용으로 문의 검색
	@Select("select * from qnatable where content like CONCAT('%', #{content}, '%') order by qnaNo desc")
	List<QnaVO> searchQnaByContent(@Param("content") String content);

//	//기간으로 문의 검색(최근 일주일)
//	@Select("select * from qnatable where regDate between DATE_ADD(NOW(), INTERVAL -1 WEEK) and NOW()")
//	List<QnaVO> searchQnaByWeek();
//
//	//기간으로 문의 검색(최근 한달)
//	@Select("select * from qnatable where regDate between DATE_ADD(NOW(), INTERVAL -1 MONTH) and NOW()")
//	List<QnaVO> searchQnaByMonth();
//
//	//기간으로 문의 검색(최근 세달)
//	@Select("select * from qnatable where regDate between DATE_ADD(NOW(), INTERVAL -3 MONTH) and NOW()")
//	List<QnaVO> searchQnaByMonths();
	

	

	

	
	

	

	



	

	



}
