package com.myspring.spring.qna;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface QnaMapper {
	// 전체 개수 가져오기
	@SelectProvider(type = QnaUtils.class, method = "getQnaCountByType")
	int getQnaCountByType(String search, String searchWord, String type);

	// 카테고리별 글목록조회
	@SelectProvider(type = QnaUtils.class, method = "getQnaListByType")
	List<QnaAndProductVO> getQnaListByType(int start, int perPage, String search, String searchWord, String type);

	// 게시판 목록 출력
	@Select("select * from qnatable where qnaNo = #{qnaNo}")
	QnaVO getQna(@Param("qnaNo") int qnaNo);

	// 문의 게시판 목록 조회
	@Select("select * from qnatable where ${search} like CONCAT('%', #{searchWord}, '%') by originalNo desc, qnaNo asc limit #{start}, #{perPage}")
	List<QnaVO> getQnaWithSearch(@Param("start") int start, @Param("perPage") int perPage,
			@Param("search") String search, @Param("searchWord") String searchWord);

	// 문의 전체 조회
	@Select("select * from qnatable order by qnaNo desc")
	List<QnaVO> getQnaAll();

	// qnaNo로 1개 문의 가져오기
	@Select("select * from qnatable where qnaNo = #{qnaNo}")
	QnaVO getQnaByQnaNo(@Param("qnaNo") int qnaNo);

	// type별 문의 조회
	@Select("select * from qnatable where type = #{type} order by qnaNo desc")
	List<QnaVO> getQnaByType(@Param("type") String type);

	// originalNo로 조회
	@Select("select * from qnatable where originalNo = #{orignalNo} AND reply = false")
	QnaVO getQnaByOriginalNo(@Param("originalNo") int originalNo);

//	//상품문의 카테고리 전체 조회 & 아이디, 내용, 상품명으로 조회
//	@Select("select * from qnatable where ${search} like CONCAT('%', #{searchWord}, '%') AND type in ('general', 'product', 'productNotice', 'productReply', 'generalReply') order by originalNo desc, qnaNo asc limit ${start}, ${perPage}")
//	List<QnaVO> getQnaProductAll(@Param("start") int start, @Param("perPage") int perPage, @Param("search") String search, @Param("searchWord") String searchWord);
//	
//	//상품문의 카테고리 갯수 가져오기
//	//@Select("select count(*) from qnatable where ${search} like CONCAT('%',#{searchWord},'%') AND type in ('general', 'product', 'productNotice', 'productReply', 'generalReply')")
//	@SelectProvider(type = QnaUtils.class, method = "getQnaCountByType")
//	int getQnaProductCount(@Param("search") String search, @Param("searchWord") String searchWord, @Param("type") String type);
//	
//	// 배송 전 변경&취소 카테고리 전체 조회 & 아이디, 내용으로 조회 
//	@Select("select * from qnatable where ${search} like CONCAT('%', #{searchWord}, '%') AND type in ('cancel', 'change', 'changeaddress', 'cancelNotice', 'cancelReply', 'changeReply', 'changeaddressReply') order by originalNo desc, qnaNo asc limit ${start}, ${perPage}") 
//	List<QnaVO> getQnaBeforeDeliveryAll(@Param("start") int start, @Param("perPage") int perPage, @Param("search") String search, @Param("searchWord") String searchWord);
//	
//	// 배송 전 변경&취소 카테고리 전체 갯수 가져오기
//	@SelectProvider(type = QnaUtils.class, method = "getQnaCountByType")
//	int getQnaBeforeDeliveryCount(@Param("search") String search, @Param("searchWord") String searchWord, @Param("type") String type);
//	
//	// 배송 문의 카테고리 전체 조회 & 아이디, 내용으로 조회
//	@Select("select * from qnatable where ${search} like CONCAT('%', #{searchWord}, '%') AND type in('delivery', 'deliveryNotice', 'deliveryReply') order by originalNo desc, qnaNo asc limit #{start}, #{perPage}")
//	List<QnaVO> getQnaDelieveryAll(@Param("start") int start, @Param("perPage") int perPage, @Param("search") String search, @Param("searchWord") String searchWord);
//	
//	// 배송 문의 카테고리 전체 갯수 가져오기
//	@Select("select count(*) from qnatable where ${search} like CONCAT('%',#{searchWord},'%') AND type in ('delivery', 'deliveryNotice', 'deliveryReply')")
//	int getQnaDeliveryCount(@Param("search") String search, @Param("searchWord") String searchWord);
//	
//	// 배송 후 교환&반품 카테고리 전체 조회
//	@Select("select * from qnatable where ${search} like CONCAT('%', #{searchWord}, '%') AND type in ('return', 'exchange', 'error', 'returnNotice', 'returnReply', 'exchangeReply', 'errorReply') order by originalNo desc, qnaNo asc limit #{start}, #{perPage}")
//	List<QnaVO> getQnaAfterDeliveryAll(@Param("start") int start, @Param("perPage") int perPage, @Param("search") String search, @Param("searchWord") String searchWord);
//	
//	// 배송 후 교환&반품 카테고리 전체 갯수 가져오기
//	@Select("select count(*) from qnatable where ${search} like CONCAT('%',#{searchWord},'%') AND type in ('return', 'exchange', 'error', 'returnNotice', 'returnReply', 'exchangeReply', 'errorReply')")
//	int getQnaAfterDeliveryCount(@Param("search") String search, @Param("searchWord") String searchWord);
//	
	// 문의 등록
	@Insert("insert into qnatable(productNo, type, reply, content, id, secret, image) values(#{in.productNo}, #{in.type}, #{in.reply}, #{in.content}, #{in.id}, #{in.secret}, #{in.image})")
	int insertQna(@Param("in") QnaVO qnaVO);

	// 댓글 등록
	@Insert("insert into qnatable(type, originalNo, content, id, secret, image) values(#{in.type}, #{in.originalNo}, #{in.content}, #{in.id}, #{in.secret}, #{in.image})")
	int insertReply(@Param("in") QnaVO qnaVO);

	// 댓글 등록시 reply 업데이트
	@Update("update qnatable set reply = true where qnaNo = #{originalNo}")
	int updateReplyTrue(int originalNo);

	// 문의 수정 & 댓글 수정
	@Update("update qnatable set type=#{type}, content = #{content}, secret = #{secret}, image = #{image} where qnaNo=#{qnaNo}")
	int updateQna(@Param("qnaNo") int qnaNo, @Param("type") String type, @Param("content") String content,
			@Param("secret") boolean secret, @Param("image") String image);

	// 문의 삭제 & 댓글 삭제
	@Delete("delete from qnatable where qnaNo = #{qnaNo}")
	int deleteQna(@Param("qnaNo") int qnaNo);

	// 댓글 삭제시 reply 업데이트
	@Update("update qnatable set reply = false where qnaNo = #{originalNo}")
	int updateReplyFalse(@Param("originalNo") int originalNo);

	// 아이디로 문의 검색
	@Select("select * from qnatable where id = #{id} order by qnaNo desc")
	List<QnaVO> searchQnaById(@Param("id") String id);

	// 내용으로 문의 검색
	@Select("select * from qnatable where content like CONCAT('%', #{content}, '%') order by qnaNo desc")
	List<QnaVO> searchQnaByContent(@Param("content") String content);

	// 원글삭제시 댓글도 삭제
	@Delete("delete from qnatable where originalNo = #{qnaNo}")
	int deleteReply(@Param("qnaNo") int qnaNo);

	// 상품관련 qna리스트 가져오기
	@SelectProvider(type = QnaUtils.class, method = "getQnaListByProductNo")
	List<QnaVO> getQnaListByProductNo(int start, int perPage, String search, String searchWord, int productNo);

	// 전체 개수 가져오기
	@SelectProvider(type = QnaUtils.class, method = "getQnaCountByProductNo")
	int getQnaCountByProductNo(String search, String searchWord, int productNo);

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
