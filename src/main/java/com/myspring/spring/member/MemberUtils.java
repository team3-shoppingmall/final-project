package com.myspring.spring.member;

import org.apache.ibatis.jdbc.SQL;

public class MemberUtils {
	public String getMembers(String condition, Object param) {
		SQL sql = new SQL() {
			{
				SELECT("*");
				FROM("membertable");
				WHERE(condition + " like " + "'%" + param + "%'");
			}
		};

		System.out.println(sql.toString());
		return sql.toString();
	}

	public String updateMember(MemberVO member) {
		SQL sql = new SQL() {
			{
				UPDATE("membertable");
				if (member.getPassword() != null)
					SET("password = '" + member.getPassword() + "'");
				if (member.getName() != null)
					SET("name = '" + member.getName() + "'");
				if (member.getEmail() != null)
					SET("email = '" + member.getEmail() + "'");
				if (member.getZipcode() != null)
					SET("zipcode = '" + member.getZipcode() + "'");
				if (member.getAddr1() != null)
					SET("addr1 = '" + member.getAddr1() + "'");
				if (member.getAddr2() != null)
					SET("addr2 = '" + member.getAddr2() + "'");
				if (member.getTel() != null)
					SET("tel = '" + member.getTel() + "'");
				if (member.getPoint() != 0)
					SET("point = '" + Integer.toString(member.getPoint()) + "'");
				WHERE("id = '" + member.getId() + "'");
			}
		};
		System.out.println(sql.toString());
		return sql.toString();
	}
}
