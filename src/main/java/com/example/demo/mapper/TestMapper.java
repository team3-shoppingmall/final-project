package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.demo.vo.TestVO;

@Mapper
public interface TestMapper {

	@Select("select * from testtable")
	public List<TestVO> getAllMembers();

	@Select("select * from testtable where id = #{name}")
	public TestVO getMemberFindByID(@Param("name") String id);

	@Insert("insert into testtable values (#{id}, #{pwd})")
	public int insertMember1(@Param("id") String id, @Param("pwd") String pwd);

	@Insert("insert into testtable values (#{in.id}, #{in.pwd})")
	public int insertMember2(@Param("in") TestVO testVO);

	@Delete("delete from testtable where id = #{id}")
	public int deleteMember(@Param("id") String id);

	@Update("Update testtable set pwd = #{pwd} where id = #{id}")
	public int updateMember(@Param("id") String id, @Param("pwd") String pwd);
}
