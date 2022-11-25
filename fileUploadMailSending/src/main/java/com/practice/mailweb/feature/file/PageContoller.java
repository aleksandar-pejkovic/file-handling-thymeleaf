package com.practice.mailweb.feature.file;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.practice.mailweb.feature.user.AppUser;
import com.practice.mailweb.feature.user.UserService;

@Controller
public class PageContoller {
	
	@Autowired
	private UserService userService;

	@GetMapping("/users")
	String getPeople(Model model) {
		List<AppUser> users = userService.loadAllUsers();
		model.addAttribute("users", users);
		return "show-users";
	}
	
	@GetMapping("/user")
	String getPerson(Model model, @RequestParam("username") String username) {
		Optional<AppUser> optionalUser = userService.loadUserByUsername(username);
		AppUser user = optionalUser.orElseThrow();
		model.addAttribute("user", user);
		return "show-user-image";
	}
	
}
