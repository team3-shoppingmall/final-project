package com.myspring.spring.member;

import org.apache.ibatis.jdbc.SQL;

public class MemberUtils {
//	@Select("select id, name, tel, email, zipcode, addr1, addr2, terms, point from membertable limit #{perPage} offset #{start}")
	public String getMembers(int start, int perPage, String condition, Object param, boolean role) {
		SQL sql = new SQL() {
			{
				SELECT("id, name, tel, email, zipcode, addr1, addr2, terms, point, authority");
				FROM("membertable");
				if (role) {
					WHERE("authority != 'ROLE_USER'");
				} else {
					WHERE("authority = 'ROLE_USER'");
				}
				if (!(param == null || param.equals(""))) {
					AND();
					WHERE(condition + " like " + "'%" + param + "%'");
				}
				ORDER_BY("authority, name");
				LIMIT(perPage);
				OFFSET(start);
			}
		};

//		System.out.println(sql.toString());
		return sql.toString();
	}

//	@Select("select count(id) from membertable")
	public String getCount(String condition, Object param, boolean role) {
		SQL sql = new SQL() {
			{
				SELECT("count(id)");
				FROM("membertable");
				if (role) {
					WHERE("authority != 'ROLE_USER'");
				} else {
					WHERE("authority = 'ROLE_USER'");
				}
				if (!(param == null || param.equals(""))) {
					AND();
					WHERE(condition + " like " + "'%" + param + "%'");
				}
			}
		};

//		System.out.println(sql.toString());
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
