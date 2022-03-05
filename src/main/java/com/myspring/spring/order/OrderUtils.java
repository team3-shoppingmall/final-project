package com.myspring.spring.order;

import org.apache.ibatis.jdbc.SQL;

public class OrderUtils {
	public String getOrderAll(int start, int perPage, String state, String search, String searchWord1,
			String searchWord2) {
		SQL sql = new SQL() {
			{
				SELECT("*");
				FROM("ordertable");
				if (!((searchWord1 == null || searchWord1.equals(""))
						&& (searchWord2 == null || searchWord2.equals("")))) {
					AND();
					String[] words = searchWord1.split(" ");

					switch (search) {
					case "orderIdx":
						for (int i = 0; i < words.length; i++) {
							if (i > 0) {
								OR();
							}
							WHERE("orderIdx = " + Integer.parseInt(words[i]));
						}
						break;
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
					case "orderDate":
						WHERE("orderDate >= '" + searchWord1 + "' and orderDate <= '" + searchWord2 + " 23:59:59'");
						break;
					}
				}
				if (state != null) {
					AND();
					WHERE("state = '" + state + "'");
				}

				LIMIT(perPage);
				OFFSET(start);
			}
		};
		System.out.println(sql.toString());
		return sql.toString();
	}

//	select * from producttable where price>=최소값 and mrice<=최대값 and productName like %검색어% and UPPER(type1) = 대분류 and UPPER(type2) = 소분류
	public String getOrderAllCount(String state, String search, String searchWord1, String searchWord2) {

		SQL sql = new SQL() {
			{
				SELECT("count(*)");
				FROM("producttable");
				if (!((searchWord1 == null || searchWord1.equals(""))
						&& (searchWord2 == null || searchWord2.equals("")))) {
					AND();
					String[] words = searchWord1.split(" ");

					switch (search) {
					case "orderIdx":
						for (int i = 0; i < words.length; i++) {
							if (i > 0) {
								OR();
							}
							WHERE("orderIdx = " + Integer.parseInt(words[i]));
						}
						break;
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
					case "orderDate":
						WHERE("orderDate >= '" + searchWord1 + "' and orderDate <= '" + searchWord2 + " 23:59:59'");
						break;
					}
				}
				if (state != null) {
					AND();
					WHERE("state = '" + state + "'");
				}
			}
		};
		System.out.println(sql.toString());
		return sql.toString();
	}
}
