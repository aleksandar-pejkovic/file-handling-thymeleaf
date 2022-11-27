package com.practice.mailweb.feature.user;

public interface IUserService {

	AppUser loadUserByUsername(String username);
	AppUser loadUserByEmail(String email);

	
}
