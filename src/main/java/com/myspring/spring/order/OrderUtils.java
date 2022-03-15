package com.myspring.spring.order;

import org.apache.ibatis.jdbc.SQL;

public class OrderUtils {
//	select * from ordertable where ~ limit perPage offset start
	public String getOrder(int start, int perPage, String state, String search, String searchWord1,
			String searchWord2) {
		SQL sql = new SQL() {
			{
				SELECT("*");
				FROM("ordertable o");
				LEFT_OUTER_JOIN("producttable p on o.productNo = p.productNo");
				if (!((searchWord1 == null || searchWord1.equals(""))
						&& (searchWord2 == null || searchWord2.equals("")))) {
					AND();
					String[] words = searchWord1.split(" ");
					String temp = null;
					switch (search) {
					case "orderIdx":
						for (int i = 0; i < words.length; i++) {
							if (i == 0) {
								temp = words[i];
							} else {
								temp = temp + ", " + words[i];
							}
						}
						WHERE("o.orderIdx in (" + temp + ")");
						break;
					case "productNo":
						for (int i = 0; i < words.length; i++) {
							if (i == 0) {
								temp = words[i];
							} else {
								temp = temp + ", " + words[i];
							}
						}
						WHERE("o.productNo in (" + temp + ")");
						break;
					case "productName":
						for (int i = 0; i < words.length; i++) {
							if (i == 0) {
								temp = words[i];
							} else {
								temp = temp + "|" + words[i];
							}
						}
						WHERE("REGEXP_LIKE(p.productName, '" + temp + "')");
						break;
					case "id":
						WHERE("id = '" + searchWord1 + "'");
						break;
					case "orderDate":
						WHERE("o.orderDate >= '" + searchWord1 + "' and o.orderDate <= '" + searchWord2 + " 23:59:59'");
						break;
					}
				}
				if (state != null) {
					AND();
					WHERE("o.state = '" + state + "'");
				}
				ORDER_BY("orderidx desc");
				LIMIT(perPage);
				OFFSET(start);
			}
		};
//		System.out.println(sql.toString());
		return sql.toString();
	}

//	select count(*) from ordertable where ~
	public String getOrderCount(String state, String search, String searchWord1, String searchWord2) {

		SQL sql = new SQL() {
			{
				SELECT("count(*)");
				FROM("ordertable o");
				LEFT_OUTER_JOIN("producttable p on o.productNo = p.productNo");
				if (!((searchWord1 == null || searchWord1.equals(""))
						&& (searchWord2 == null || searchWord2.equals("")))) {
					AND();
					String[] words = searchWord1.split(" ");
					String temp = null;
					switch (search) {
					case "orderIdx":
						for (int i = 0; i < words.length; i++) {
							if (i == 0) {
								temp = words[i];
							} else {
								temp = temp + ", " + words[i];
							}
						}
						WHERE("o.orderIdx in (" + temp + ")");
						break;
					case "productNo":
						for (int i = 0; i < words.length; i++) {
							if (i == 0) {
								temp = words[i];
							} else {
								temp = temp + ", " + words[i];
							}
						}
						WHERE("o.productNo in (" + temp + ")");
						break;
					case "productName":
						for (int i = 0; i < words.length; i++) {
							if (i == 0) {
								temp = words[i];
							} else {
								temp = temp + "|" + words[i];
							}
						}
						WHERE("REGEXP_LIKE(p.productName, '" + temp + "')");
						break;
					case "id":
						WHERE("id = '" + searchWord1 + "'");
						break;
					case "orderDate":
						WHERE("o.orderDate >= '" + searchWord1 + "' and o.orderDate <= '" + searchWord2 + " 23:59:59'");
						break;
					}
				}
				if (state != null) {
					AND();
					WHERE("o.state = '" + state + "'");
				}
			}
		};
//		System.out.println(sql.toString());
		return sql.toString();
	}

//	select sum(o.totalPrice) as priceSum, o.productNo, p.productName from ordertable o left join producttable p on p.productNo = o.productNo where ~ group by o.productNo limit perPage offset start
	public String getSalesSettlement(int start, int perPage, String search, String searchWord1, String searchWord2) {
		SQL sql = new SQL() {
			{
				SELECT("o.productNo, p.productName, p.imageName, sum(o.totalPrice) as priceSum");
				FROM("ordertable o");
				LEFT_OUTER_JOIN("producttable p on o.productNo = p.productNo");
				WHERE("o.state in ('배송완료', '교환완료', '구매확정')");
				if (!((searchWord1 == null || searchWord1.equals(""))
						&& (searchWord2 == null || searchWord2.equals("")))) {
					AND();
					String[] words = searchWord1.split(" ");
					String temp = null;
					switch (search) {
					case "productNo":
						for (int i = 0; i < words.length; i++) {
							if (i == 0) {
								temp = words[i];
							} else {
								temp = temp + ", " + words[i];
							}
						}
						WHERE("o.productNo in (" + temp + ")");
						break;
					case "productName":
						for (int i = 0; i < words.length; i++) {
							if (i == 0) {
								temp = words[i];
							} else {
								temp = temp + "|" + words[i];
							}
						}
						WHERE("REGEXP_LIKE(p.productName, '" + temp + "')");
						break;
					case "orderDate":
						WHERE("o.orderDate >= '" + searchWord1 + "' and o.orderDate <= '" + searchWord2 + " 23:59:59'");
						break;
					}
				}
				GROUP_BY("o.productNo");
				if (search.equals("priceSum")) {
					HAVING("sum(o.totalPrice) >= " + Integer.parseInt(searchWord1) + " and sum(o.totalPrice) <= "
							+ Integer.parseInt(searchWord2));
				}
				LIMIT(perPage);
				OFFSET(start);
			}
		};
//		System.out.println(sql.toString());
		return sql.toString();
	}

//	select count(*) from ordertable o left join producttable p on p.productNo = o.productNo where ~ group by o.productNo
//	select count(*) from (SELECT sum(o.totalPrice) FROM ordertable o LEFT OUTER JOIN producttable p on o.productNo = p.productNo GROUP BY o.productNo) as c;
	public String getSalesSettlementCount(String search, String searchWord1, String searchWord2) {

		SQL sql = new SQL() {
			{
				SELECT("sum(o.totalPrice)");
				FROM("ordertable o");
				LEFT_OUTER_JOIN("producttable p on o.productNo = p.productNo");
				WHERE("o.state in ('배송완료', '교환완료', '구매확정')");
				if (!((searchWord1 == null || searchWord1.equals(""))
						&& (searchWord2 == null || searchWord2.equals("")))) {
					AND();
					String[] words = searchWord1.split(" ");
					String temp = null;
					switch (search) {
					case "productNo":
						for (int i = 0; i < words.length; i++) {
							if (i == 0) {
								temp = words[i];
							} else {
								temp = temp + ", " + words[i];
							}
						}
						WHERE("o.productNo in (" + temp + ")");
						break;
					case "productName":
						for (int i = 0; i < words.length; i++) {
							if (i == 0) {
								temp = words[i];
							} else {
								temp = temp + "|" + words[i];
							}
						}
						WHERE("REGEXP_LIKE(p.productName, '" + temp + "')");
						break;
					case "orderDate":
						WHERE("o.orderDate >= '" + searchWord1 + "' and o.orderDate <= '" + searchWord2 + " 23:59:59'");
						break;
					}
				}
				GROUP_BY("o.productNo");
				if (search.equals("priceSum")) {
					HAVING("sum(o.totalPrice) >= " + Integer.parseInt(searchWord1) + " and sum(o.totalPrice) <= "
							+ Integer.parseInt(searchWord2));
				}
			}
		};
//		System.out.println(sql.toString());
		return sql.toString();
	}

//	select * from ordertable where id= and orderdate and state in and ~ limit perPage offset start
	public String getOrderById(int start, int perPage, String pageInfo, String state, String searchWord,
			String searchDate1, String searchDate2, String id) {
		SQL sql = new SQL() {
			{
				SELECT("*");
				FROM("ordertable o");
				LEFT_OUTER_JOIN("producttable p on o.productNo = p.productNo");
				WHERE("id = '" + id + "'");
				AND();
				WHERE("o.orderDate >= '" + searchDate1 + "' and o.orderDate <= '" + searchDate2 + " 23:59:59'");
				AND();
				switch (pageInfo) {
				case "orders":
					WHERE("o.state in ('입금전', '결제완료', '배송준비중', '배송중', '배송완료', '구매확정')");
					break;
				case "returns":
					WHERE("o.state in ('취소완료', '교환완료', '환불완료')");
					break;
				}
				if (!((searchWord == null || searchWord.equals("")))) {
					AND();
					String[] words = searchWord.split(" ");
					String temp = null;
					for (int i = 0; i < words.length; i++) {
						if (i == 0) {
							temp = words[i];
						} else {
							temp = temp + "|" + words[i];
						}
					}
					WHERE("REGEXP_LIKE(p.productName, '" + temp + "')");
				}
				if (state != null) {
					AND();
					WHERE("o.state = '" + state + "'");
				}
				ORDER_BY("orderidx desc");
				LIMIT(perPage);
				OFFSET(start);
			}
		};
//		System.out.println(sql.toString());
		return sql.toString();
	}

//	select count(*) from ordertable where id= and orderdate and state in and ~
	public String getOrderByIdCount(String pageInfo, String state, String searchWord, String searchDate1,
			String searchDate2, String id) {

		SQL sql = new SQL() {
			{
				SELECT("count(*)");
				FROM("ordertable o");
				LEFT_OUTER_JOIN("producttable p on o.productNo = p.productNo");
				WHERE("id = '" + id + "'");
				AND();
				WHERE("o.orderDate >= '" + searchDate1 + "' and o.orderDate <= '" + searchDate2 + " 23:59:59'");
				AND();
				switch (pageInfo) {
				case "orders":
					WHERE("o.state in ('입금전', '결제완료', '배송준비중', '배송중', '배송완료', '구매확정')");
					break;
				case "returns":
					WHERE("o.state in ('취소완료', '교환완료', '환불완료')");
					break;
				}
				if (!((searchWord == null || searchWord.equals("")))) {
					AND();
					String[] words = searchWord.split(" ");
					String temp = null;
					for (int i = 0; i < words.length; i++) {
						if (i == 0) {
							temp = words[i];
						} else {
							temp = temp + "|" + words[i];
						}
					}
					WHERE("REGEXP_LIKE(p.productName, '" + temp + "')");
				}
				if (state != null) {
					AND();
					WHERE("o.state = '" + state + "'");
				}
			}
		};
//		System.out.println(sql.toString());
		return sql.toString();
	}
}
