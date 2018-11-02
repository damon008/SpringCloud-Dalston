package com.leinao.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.leinao.user.model.User;

/**
*
*
* created by wangshoufa
* 2018年5月23日 下午4:05:21
*
*/
@Repository
public interface UserMapper {
	
	List<User> getUser();

	void updateUserById(Long id, String name);

	void insertUser(@Param(value="name")String name);
}
