package com.myspring.spring.review;

import org.apache.ibatis.jdbc.SQL;

public class ReviewUtils {

//	@Select("select count(*) from reviewtable where ${search} like CONCAT('%',#{searchWord},'%')")
//	리뷰 개수 가져오기
	public String getReviewCount(String search, String searchWord, int productNo, String id) {
		SQL sql = new SQL() {
			{
				SELECT("count(*)");
				FROM("reviewtable r");
				LEFT_OUTER_JOIN("producttable p on p.productNo = r.productNo");
				if (searchWord != null && !searchWord.equals("")) {
					AND();
					String[] words = searchWord.split(",");
					String temp = null;
					for (int i = 0; i < words.length; i++) {
						if (i == 0) {
							temp = words[i];
						} else {
							temp = temp + "|" + words[i];
						}
					}
					switch (search) {
					case "id":
						WHERE("REGEXP_LIKE(r.id, '" + temp + "')");
						break;
					case "productName":
						WHERE("REGEXP_LIKE(p.productName, '" + temp + "')");
						break;
					}
				}
				if (productNo != 0) {
					AND();
					WHERE("p.productNo = " + productNo);
				}
				if (id != null) {
					AND();
					WHERE("r.id = '" + id + "'");
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
	public String getReviewList(int start, int perPage, String search, String searchWord, int productNo, String id) {
		SQL sql = new SQL() {
			{
				SELECT("*");
				FROM("reviewtable r");
				LEFT_OUTER_JOIN("producttable p on p.productNo = r.productNo");
				if (searchWord != null && !searchWord.equals("")) {
					AND();
					String[] words = searchWord.split(",");
					String temp = null;
					for (int i = 0; i < words.length; i++) {
						if (i == 0) {
							temp = words[i];
						} else {
							temp = temp + "|" + words[i];
						}
					}
					switch (search) {
					case "id":
						WHERE("REGEXP_LIKE(r.id, '" + temp + "')");
						break;
					case "productName":
						WHERE("REGEXP_LIKE(p.productName, '" + temp + "')");
						break;
					}
				}
				if (productNo != 0) {
					AND();
					WHERE("p.productNo = " + productNo);
				}
				if (id != null) {
					AND();
					WHERE("r.id = '" + id + "'");
				}
				ORDER_BY("r.reviewNo desc");
				LIMIT(perPage);
				OFFSET(start);
			}
		};
//		System.out.println(sql.toString());
		return sql.toString();
	}
}
