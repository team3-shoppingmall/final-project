package com.myspring.spring.qna;


import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.myspring.spring.notice.NoticeVO;

@Mapper
public interface QnaMapper {
	// 전체 개수 가져오기
	@Select("select count(*) from qna where ${search} like CONCAT('%',#{searchWord},'%')")
	int getCount(@Param("search") String search, @Param("searchWord") String searchWord);
	
	@Select("select * from qna where qnaNo = #{qnaNo}")
	QnaVO getQna(@Param("qnaNo") int qnaNo);
	
	// 문의 게시판 목록 조회
	@Select("select * from qna where ${search} like CONCAT('%', #{searchWord}, '%') order by qnaNo desc limit #{start}, #{perPage}")
	List<NoticeVO> getQna(int start, int perPage, String search, String searchWord);
	
	// 문의 전체 조회
	@Select("select * from qna order by qnaNo desc")
	List<QnaVO> getQnaAll();
	
	// type별 문의 조회
	@Select("select * from qna where type = #{type} order by qnaNo desc")
	List<QnaVO> getQnaByType(@Param("type") String type);
	
	// 상품문의 카테고리 전체 조회
	@Select("select * from qna where type in ('general', 'product', 'productNotice') order by qnaNo desc")
	List<QnaVO> getQnaProductAll();
	
	// 배송 문의 카테고리 전체 조회
	@Select("select * from qna where type in ('cancel', 'change', 'changeaddress', 'cancelNotice') order by qnaNo desc")
	List<QnaVO> getQnaBeforeDeliveryAll();

	// 배송 전 변경&취소 카테고리 전체 조회
	@Select("select * from qna where type in ('delivery', 'deliveryNotice') order by qnaNo desc")
	List<QnaVO> getQnaDelieveryAll();
	
	// 배송 후 교환&반품 카테고리 전체 조회
	@Select("select * from qna where type in ('return', 'exchange', 'error', 'returnNotice') order by qnaNo desc")
	List<QnaVO> getQnaAfterDeliveryAll();
	
	// 문의 등록 & 댓글 등록
	@Insert("insert into qna values(#{in.qnaNo}, #{in.productNo}, #{in.type}, #{in.originalNo}, #{in.reply}, #{in.content}, #{in.id}, #{in.regdate}, #{in.secret}, #{in.image})")
	int insertQna(@Param("in") QnaVO qnaVO);
	
	// 댓글 등록시 reply 업데이트
	@Update("update qna set reply = true where qnaNo = #{originalNo}")
	int updateReplyTrue(int originalNo);

	// 문의 수정 & 댓글 수정
	@Update("update qna set type=#{type}, content = #{content}, secret = #{secret}, image = #{image} where qnaNo=#{qnaNo}")
	int updateQna(@Param("qnaNo") int qnaNo, @Param("type")String type, @Param("content") String content, 
				  @Param("secret") boolean secret, @Param("image") String image);

	// 문의 삭제 & 댓글 삭제
	@Delete("delete from qna where qnaNo = #{qnaNo}")
	int deleteQna(@Param("qnaNo")int qnaNo);

	// 댓글 삭제시 reply 업데이트
	@Update("update qna set reply = false where qnaNo = #{originalNo}")
	int updateReplyFalse(@Param("originalNo") int originalNo);
		
	// 아이디로 문의 검색
	@Select("select * from qna where id = #{id} order by qnaNo desc")
	List<QnaVO> searchQnaById(@Param("id") String id);

	// 내용으로 문의 검색
	@Select("select * from qna where content like '%${content}%' order by qnaNo desc")
	List<QnaVO> searchQnaByContent(@Param("content") String content);

//	//기간으로 문의 검색(최근 일주일)
//	@Select("select * from qna where regdate between DATE_ADD(NOW(), INTERVAL -1 WEEK) and NOW()")
//	List<QnaVO> searchQnaByWeek();
//
//	//기간으로 문의 검색(최근 한달)
//	@Select("select * from qna where regdate between DATE_ADD(NOW(), INTERVAL -1 MONTH) and NOW()")
//	List<QnaVO> searchQnaByMonth();
//
//	//기간으로 문의 검색(최근 세달)
//	@Select("select * from qna where regdate between DATE_ADD(NOW(), INTERVAL -3 MONTH) and NOW()")
//	List<QnaVO> searchQnaByMonths();
	

	

	

	
	

	

	



	

	



}
