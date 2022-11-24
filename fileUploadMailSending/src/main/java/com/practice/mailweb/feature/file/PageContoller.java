package com.practice.mailweb.feature.file;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
	
}
