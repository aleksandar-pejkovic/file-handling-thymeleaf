package com.practice.mailweb.feature.user;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<AppUser, Long>{

	Optional<AppUser> findByUsername(String username);
	Optional<AppUser> findByEmail(String email);
	
}
