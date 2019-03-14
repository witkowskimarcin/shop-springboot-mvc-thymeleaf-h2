package com.example.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.entity.Role;
import com.example.entity.User;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRespository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public void saveUser(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setActive(1);
		Role userRole = roleRespository.findByRole("USER");
		user.setRoles(new ArrayList<Role>(Arrays.asList(userRole)));
		userRepository.save(user);
	}
	
	@Override
	public void saveAdmin(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setActive(1);
		Role userRole = roleRespository.findByRole("ADMIN");
		user.setRoles(new ArrayList<Role>(Arrays.asList(userRole)));
		userRepository.save(user);
	}
}
