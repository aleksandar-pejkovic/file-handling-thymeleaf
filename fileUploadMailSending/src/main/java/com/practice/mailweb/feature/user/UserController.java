package com.practice.mailweb.feature.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	UserService userService;

	@PostMapping
	public AppUser addNewUser(@RequestBody AppUser user) {
		return userService.createNewUser(user);
	}

	@PutMapping
	public AppUser updateUser(@RequestBody AppUser user) {
		return userService.updateUser(user);
	}

	@DeleteMapping("/delete/{username}")
	public String deleteUser(@PathVariable String username) {
		return userService.deleteUser(username);
	}

	@GetMapping
	public List<AppUser> getAllUsers() {
		return userService.loadAllUsers();
	}

	@GetMapping("/username/{username}")
	public AppUser getUserByUsername(@PathVariable String username) {
		return userService.loadUserByUsername(username);
	}
	
	@GetMapping("/email/{email}")
	public AppUser getUserByEmail(@PathVariable String email) {
		return userService.loadUserByEmail(email);
	}
	
	@GetMapping("/verify")
	public boolean verifyCredentials(@RequestBody UserCredentials creds) {
		return userService.verifyCredentials(creds.getUsername(), creds.getPassword());
	}

}
