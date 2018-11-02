package com.leinao.user.service;

import java.util.List;

import com.leinao.user.model.User;

/**
*
*
* created by wangshoufa
* 2018年5月23日 下午4:08:34
*
*/
public interface UserService {
	
	List<User> getUser();
	
	int update(Long id,String name);

	List<User> getInfo();

}
