package com.leinao.user.service.impl;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.leinao.user.dao.UserMapper;
import com.leinao.user.model.User;
import com.leinao.user.service.UserService;

/**
*
*
* created by wangshoufa
* 2018年5月23日 下午4:08:51
*
*/

@Service
public class UserServiceImpl implements UserService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UserMapper userMapper;

	@Override
	public List<User> getUser() {
		List<User> ls = Lists.newArrayList();
		try {
			ls = userMapper.getUser();
		} catch (Exception e) {
			return ls;
		}
		return ls;
	}

	@Transactional
	@Override
	public int update(Long id, String name) {
		try {
			userMapper.updateUserById(id,name);
			userMapper.insertUser(name+"ssssssss");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return 0;
		}
		return 1;
	}

	@Override
	public List<User> getInfo() {
		List<User> l = Lists.newArrayList();
		User u = new User();
		u.setId(1L);
		u.setName("ssssssss");
		l.add(u);
		return l;
	}
	

}
