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
	@Select("select * from qna")
	List<QnaVO> getQnaAll();
	
	@Select("select * from qna where type = #{type}")
	List<QnaVO> getQnaByType(@Param("type") String type);
	
	@Select("select * from qna where type in ('general', 'product', 'productNotice')")
	List<QnaVO> getQnaProductAll();
	
	@Select("select * from qna where type in ('cancel', 'change', 'changeaddress', 'cancelNotice')")
	List<QnaVO> getQnaBeforeDeliveryAll();

	@Select("select * from qna where type in ('delivery', 'deliveryNotice')")
	List<QnaVO> getQnaDelieveryAll();
	
	@Select("select * from qna where type in ('return', 'exchange', 'error', 'returnNotice')")
	List<QnaVO> getQnaAfterDeliveryAll();
	
	@Insert("insert into qna values(#{in.qnaNo}, #{in.productNo}, #{in.type}, #{in.originalNo}, #{in.reply}, #{in.content}, #{in.id}, #{in.regdate}, #{in.secret}, #{in.image})")
	int insertQna(@Param("in") QnaVO qnaVO);

	@Update("update qna set type=#{type}, content = #{content}, secret = #{secret}, image = #{image} where qnaNo=#{qnaNo}")
	int updateQna(@Param("qnaNo") int qnaNo, @Param("type")String type, @Param("content") String content, 
				  @Param("secret") boolean secret, @Param("image") String image);

	@Delete("delete from qna where qnaNo = #{qnaNo}")
	int deleteQna(int qnaNo);

	

	

	
	

	

	



	

	



}
