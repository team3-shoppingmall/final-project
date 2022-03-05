package com.myspring.spring.order;

import org.apache.ibatis.jdbc.SQL;

public class OrderUtils {
	public String getProductAll(int start, int perPage, String type1, String type2, String search, String searchWord1,
			String searchWord2) {
		SQL sql = new SQL() {
			{
				SELECT("*");
				FROM("producttable");
				if (!((searchWord1 == null || searchWord1.equals(""))
						&& (searchWord2 == null || searchWord2.equals("")))) {
					AND();
					String[] words = searchWord1.split(" ");

					switch (search) {
					case "productNo":
						for (int i = 0; i < words.length; i++) {
							if (i > 0) {
								OR();
							}
							WHERE("productNo = " + Integer.parseInt(words[i]));
						}
						break;
					case "productName":
						for (int i = 0; i < words.length; i++) {
							if (i > 0) {
								OR();
							}
							WHERE("productName like '%" + words[i] + "%'");
						}
						break;
					case "price":
						WHERE("price >= " + Integer.parseInt(searchWord1) + " and price <= "
								+ Integer.parseInt(searchWord2));
						break;
					case "amount":
						WHERE("amount >= " + Integer.parseInt(searchWord1) + " and amount <= "
								+ Integer.parseInt(searchWord2));
						break;
					case "regDate":
						WHERE("regDate >= '" + searchWord1 + "' and regDate <= '" + searchWord2 + " 23:59:59'");
						break;
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
				LIMIT(perPage);
				OFFSET(start);
			}
		};
//		System.out.println(sql.toString());
		return sql.toString();
	}

//	select * from producttable where price>=최소값 and mrice<=최대값 and productName like %검색어% and UPPER(type1) = 대분류 and UPPER(type2) = 소분류
	public String getProductAllCount(String type1, String type2, String search, String searchWord1,
			String searchWord2) {

		SQL sql = new SQL() {
			{
				SELECT("count(*)");
				FROM("producttable");
				if (!((searchWord1 == null || searchWord1.equals(""))
						&& (searchWord2 == null || searchWord2.equals("")))) {
					AND();
					String[] words = searchWord1.split(" ");

					switch (search) {
					case "productNo":
						for (int i = 0; i < words.length; i++) {
							if (i > 0) {
								OR();
							}
							WHERE("productNo = " + Integer.parseInt(words[i]));
						}
						break;
					case "productName":
						for (int i = 0; i < words.length; i++) {
							if (i > 0) {
								OR();
							}
							WHERE("productName like '%" + words[i] + "%'");
						}
						break;
					case "price":
						WHERE("price >= " + Integer.parseInt(searchWord1) + " and price <= "
								+ Integer.parseInt(searchWord2));
						break;
					case "amount":
						WHERE("amount >= " + Integer.parseInt(searchWord1) + " and amount <= "
								+ Integer.parseInt(searchWord2));
						break;
					case "regDate":
						WHERE("regDate >= '" + searchWord1 + "' and regDate <= '" + searchWord2 + " 23:59:59'");
						break;
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
			}
		};
//		System.out.println(sql.toString());
		return sql.toString();
	}
}
