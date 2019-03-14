package com.example.service;

import com.example.entity.User;

public interface UserService {

	public User findUserByEmail(String email);
	public void saveUser(User user);
	public void saveAdmin(User user);
}
