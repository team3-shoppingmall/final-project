package com.myspring.spring.qna;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.jdbc.SQL;

public class QnaUtils {
//  select * from qnatable where ${search} like CONCAT('%', #{searchWord}, '%') AND type in ('general', 'product', 'productNotice', 'productReply', 'generalReply') order by originalNo desc, qnaNo asc limit ${start}, ${perPage}")
//	@Select("select * from qnatable where ${search} like CONCAT('%', #{searchWord}, '%') AND type in ('cancel', 'change', 'changeaddress', 'cancelNotice', 'cancelReply', 'changeReply', 'changeaddressReply') order by originalNo desc, qnaNo asc limit ${start}, ${perPage}") 
//	"select * from qnatable where ${search} like CONCAT('%', #{searchWord}, '%') AND type in('delivery', 'deliveryNotice', 'deliveryReply') order by originalNo desc, qnaNo asc limit #{start}, #{perPage}"
//	@Select("select * from qnatable where ${search} like CONCAT('%', #{searchWord}, '%') AND type in ('return', 'exchange', 'error', 'returnNotice', 'returnReply', 'exchangeReply', 'errorReply') order by originalNo desc, qnaNo asc limit #{start}, #{perPage}")

	public String getQnaListByType(int start, int perPage, String search, String searchWord, String type) {
		SQL sql = new SQL() {
			{
				SELECT("*");
				FROM("qnatable");
				LEFT_OUTER_JOIN("producttable on producttable.productNo = qnatable.productNo");
				switch (type) {
				case "product":
					WHERE("type in ('general', 'product', 'productNotice', 'productReply', 'generalReply')");
					break;
				case "delivery":
					WHERE("type in('delivery', 'deliveryNotice', 'deliveryReply')");
					break;
				case "beforeDelivery":
					WHERE("type in ('cancel', 'change', 'changeaddress', 'cancelNotice', 'cancelReply', 'changeReply', 'changeaddressReply')");
					break;
				case "afterDelivery":
					WHERE("type in ('return', 'exchange', 'error', 'returnNotice', 'returnReply', 'exchangeReply', 'errorReply')");
					break;
				}
				if (searchWord != null && !searchWord.equals("")) {
					AND();
					String[] words = searchWord.split(" ");
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
				ORDER_BY("originalNo desc, qnaNo asc");
				LIMIT(perPage);
				OFFSET(start);
			}
		};
//		System.out.println(sql.toString());
		return sql.toString();
	}

	public String getQnaCountByType(String search, String searchWord, String type) {
		SQL sql = new SQL() {
			{
				SELECT("count(*)");
				FROM("qnatable");
				LEFT_OUTER_JOIN("producttable on producttable.productNo = qnatable.productNo");
				switch (type) {
				case "product":
					WHERE("type in ('general', 'product', 'productNotice', 'productReply', 'generalReply')");
					break;
				case "delivery":
					WHERE("type in('delivery', 'deliveryNotice', 'deliveryReply')");
					break;
				case "beforeDelivery":
					WHERE("type in ('cancel', 'change', 'changeaddress', 'cancelNotice', 'cancelReply', 'changeReply', 'changeaddressReply')");
					break;
				case "afterDelivery":
					WHERE("type in ('return', 'exchange', 'error', 'returnNotice', 'returnReply', 'exchangeReply', 'errorReply')");
					break;
				}
				if (searchWord != null && !searchWord.equals("")) {
					AND();
					String[] words = searchWord.split(" ");
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
			}
		};
//		System.out.println(sql.toString());
		return sql.toString();
	}

	public String getQnaListByProductNo(int start, int perPage, String search, String searchWord, int productNo) {
		SQL sql = new SQL() {
			{
				SELECT("*");
				FROM("qnatable");
				WHERE("productNo = " + productNo);
				if (searchWord != null && !searchWord.equals("")) {
					AND();
					String[] words = searchWord.split(" ");
					for (int i = 0; i < words.length; i++) {
						if (i > 0) {
							OR();
						}
						WHERE(search + " like " + "'%" + words[i] + "%'");
					}
				}
				ORDER_BY("originalNo desc, qnaNo asc");
				LIMIT(perPage);
				OFFSET(start);
			}
		};
//		System.out.println(sql.toString());
		return sql.toString();
	}

	public String getQnaCountByProductNo(String search, String searchWord, int productNo) {
		SQL sql = new SQL() {
			{
				SELECT("count(*)");
				FROM("qnatable");
				WHERE("productNo=" + productNo);
				if (searchWord != null && !searchWord.equals("")) {
					AND();
					String[] words = searchWord.split(" ");
					for (int i = 0; i < words.length; i++) {
						if (i > 0) {
							OR();
						}
						WHERE(search + " like " + "'%" + words[i] + "%'");
					}
				}
			}
		};
//		System.out.println(sql.toString());
		return sql.toString();
	}

}