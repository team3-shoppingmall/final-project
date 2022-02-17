package com.myspring.spring.faq;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface FaqMapper {
    @Select("select * from faq")
	List<FaqVO> getFaqAll();
    
    @Select("select * from faq where type = #{type}")
	List<FaqVO> getFaqByType(@Param("type") String type);
    
    @Select("select * from faq where type in ('product', 'productNotice')")
	List<FaqVO> getFaqProductAll();
    
    @Select("select * from faq where type in ('delivery', 'deliveryNotice')")
	List<FaqVO> getFaqDeliveryAll();
    
    @Select("select * from faq where type in ('exchage', 'exchangeNotice')")
	List<FaqVO> getFaqExchangeAll();
    
    @Select("select * from faq where type in ('etc', 'etcNotice')")
	List<FaqVO> getFaqEtcAll();
    
    @Insert("insert into faq values(#{in.faqNo}, #{in.type}, #{in.title}, #{in.content}")
	int insertFaq(@Param("in") FaqVO faqVO);
     
    @Update("update faq set type = #{type}, title = #{title}, content = #{content}")
	int updateFaq(@Param("faqNo") int faqNo, @Param("type") String type, @Param("title") String title, 
			@Param("content") String content);
   
    @Delete("delete from faq where faqNo = #{faqNo}")
	int deleteFaq(int faqNo);



}
