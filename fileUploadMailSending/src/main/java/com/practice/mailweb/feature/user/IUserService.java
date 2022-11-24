package com.practice.mailweb.feature.user;

import java.util.Optional;

public interface IUserService {

	Optional<AppUser> loadUserByUsername(String username);
	Optional<AppUser> loadUserByEmail(String email);

	
}
