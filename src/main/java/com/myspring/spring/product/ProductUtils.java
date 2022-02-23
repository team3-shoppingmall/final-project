package com.myspring.spring.product;

import org.apache.ibatis.jdbc.SQL;

public class ProductUtils {
	public String getProductListByType(int start, int perPage, String type1, String type2, String searchWord,
			int minPrice, int maxPrice, String searchOrder) {
		SQL sql = new SQL() {
			{
				SELECT("*");
				FROM("producttable");
				WHERE("price >= " + minPrice);
				AND();
				WHERE("price <= " + maxPrice);
				if (searchWord != null) {
					AND();
					String[] words = searchWord.split(" ");
					for (int i = 0; i < words.length; i++) {
						if (i > 0) {
							OR();
						}
						WHERE("productName like " + "'%" + words[i] + "%'");
					}
				}
				if (type1 != null) {
					AND();
					WHERE("UPPER(type1) = UPPER('" + type1 + "')");
				}
				if (type2 != null) {
					if (!type2.equals("all")) {
						AND();
						WHERE("UPPER(type2) = UPPER('" + type2 + "')");
					}
				}
				if (searchOrder != null) {
					ORDER_BY(searchOrder);
				}
				LIMIT(perPage);
				OFFSET(start);
			}
		};
//		System.out.println(sql.toString());
		return sql.toString();
	}

	public String getProductCountByType(String type1, String type2, String searchWord, int minPrice, int maxPrice,
			String searchOrder) {
		SQL sql = new SQL() {
			{
				SELECT("count(*)");
				FROM("producttable");
				WHERE("price >= " + minPrice);
				AND();
				WHERE("price <= " + maxPrice);
				if (searchWord != null) {
					AND();
					String[] words = searchWord.split(" ");
					for (int i = 0; i < words.length; i++) {
						if (i > 0) {
							OR();
						}
						WHERE("productName like " + "'%" + words[i] + "%'");
					}
				}
				if (type1 != null) {
					AND();
					WHERE("UPPER(type1) = UPPER('" + type1 + "')");
				}
				if (type2 != null) {
					if (!type2.equals("all")) {
						AND();
						WHERE("UPPER(type2) = UPPER('" + type2 + "')");
					}
				}
				if (searchOrder != null) {
					ORDER_BY(searchOrder);
				}
			}
		};
//		System.out.println(sql.toString());
		return sql.toString();
	}

	public String getBestProductListByType(String type1, String type2) {
		SQL sql = new SQL() {
			{
				SELECT("*");
				FROM("producttable p");
				LEFT_OUTER_JOIN("ordertable o ON p.productno = o.productno");
				if (type1 != null) {
					WHERE("UPPER(type1) = UPPER('" + type1 + "')");
				}
				if (type2 != null) {
					if (!type2.equals("all")) {
						AND();
						WHERE("UPPER(type2) = UPPER('" + type2 + "')");
					}
				}
				GROUP_BY("o.productno");
				ORDER_BY("sum(o.amount) desc");
				LIMIT(8);
				OFFSET(0);
			}
		};
//		System.out.println(sql.toString());
		return sql.toString();
	}

//	if (!search.equals("")) {
//		WHERE(search + " like " + "'%" + searchWord + "%'");
//		AND();
//	}
}
