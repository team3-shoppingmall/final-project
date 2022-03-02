package com.myspring.spring.review;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.jdbc.SQL;

public class ReviewUtils {

//	@Select("select count(*) from reviewtable where ${search} like CONCAT('%',#{searchWord},'%')")
//	리뷰 개수 가져오기
	public String getReviewCount(String search, String searchWord, int productNo) {
		SQL sql = new SQL() {
			{
				SELECT("count(*)");
				FROM("reviewtable");
				LEFT_OUTER_JOIN("producttable on producttable.productNo = reviewtable.productNo");
				if (searchWord != null && !searchWord.equals("")) {
					AND();
					String[] words = searchWord.split(" "); // 띄어쓰기
					for (int i = 0; i < words.length; i++) {
						if (i > 0) {
							OR();
						}
						if (search.equals("productName")) {
							WHERE("producttable.productName like " + "'%" + words[i] + "%'");
						} else {
							WHERE(search + " like " + "'%" + words[i] + "%'");
						}
					}
				}
				if(productNo != 0) {
					AND();
					WHERE("producttable.productNo = " + productNo);
				}
			}
		};
		return sql.toString();
	}

	// @Select("select * from reviewtable left join producttable on
	// producttable.productNo =
	// reviewtable.productNo where ${search} like CONCAT('%', #{searchWord}, '%')
	// order by reviewno
	// desc limit #{start}, #{perPage}")

//	리뷰 목록보기
	public String getReviewList(int start, int perPage, String search, String searchWord, int productNo) {
		SQL sql = new SQL() {
			{
				SELECT("*");
				FROM("reviewtable");
				LEFT_OUTER_JOIN("producttable on producttable.productNo = reviewtable.productNo");

				if (searchWord != null && !searchWord.equals("")) {
					AND();
					String[] words = searchWord.split(" "); // 공백이 아니고 띄어쓰기
					for (int i = 0; i < words.length; i++) {
						if (i > 0) {
							OR();
						}
						if (search.equals("productName")) {
							WHERE("producttable.productName like " + "'%" + words[i] + "%'");
						} else {
							WHERE(search + " like " + "'%" + words[i] + "%'");
						}
					}
				}
				if(productNo != 0) {
					AND();
					WHERE("producttable.productNo = " + productNo);
				}
				ORDER_BY("reviewNo desc");
				LIMIT(perPage);
				OFFSET(start);
			}
		};
//		System.out.println(sql.toString());
		return sql.toString();
	}
}
