package com.myspring.spring.faq;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.myspring.spring.qna.QnaVO;

@Mapper
public interface FaqMapper {
	//전체 조회시 최신순으로 나오게 역순으로 조회
    @Select("select * from faqtable order by faqNo desc limit #{limit}")
	List<FaqVO> getFaqAll(@Param("limit") int limit);
    
    @Select("select count(*) from faqtable")
	int getFaqCountAll();
    
    @Select("select * from faqtable where type = #{type} limit #{limit}")
	List<FaqVO> getFaqByType(@Param("type") String type, @Param("limit") int limit);

    @Select("select count(*) from faqtable where type = #{type}")
	int getFaqCountByType(@Param("type") String type);

    @Select("select * from faqtable where title like CONCAT('%',#{searchWord},'%') or content like CONCAT('%',#{searchWord},'%') order by type")
	List<FaqVO> getFaqBySearch(@Param("searchWord") String searchWord);
    
    @Select("select * from faqtable where faqNo = #{faqNo}")
	FaqVO getFaqByFaqNo(@Param("faqNo") int faqNo);
    
    @Insert("insert into faqtable(type, title, content) values(#{in.type}, #{in.title}, #{in.content})")
	int insertFaq(@Param("in") FaqVO faqVO);
     
    @Update("update faqtable set type = #{type}, title = #{title}, content = #{content} where faqNo = #{faqNo}") 
	int updateFaq(@Param("faqNo") int faqNo, @Param("type") String type, @Param("title") String title, 
			@Param("content") String content);
   
    @Delete("delete from faqtable where faqNo = #{faqNo}")
	int deleteFaq(int faqNo);

	



}
