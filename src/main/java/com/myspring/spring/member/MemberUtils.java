package com.myspring.spring.member;

import org.apache.ibatis.jdbc.SQL;

public class MemberUtils {
	public String getMembers(String condition, Object param) {
		SQL sql = new SQL() {
			{
				SELECT("*");
				FROM("membertable");
				WHERE(condition +" like "+ "'%" + param + "%'");
			}
		};
		System.out.println(sql.toString());
		return sql.toString();
	}
}
