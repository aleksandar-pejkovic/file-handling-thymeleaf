package com.practice.mailweb.feature.file;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.practice.mailweb.feature.user.AppUser;
import com.practice.mailweb.feature.user.UserService;
import com.practice.mailweb.settings.filesettings.ImageSettings;

@Controller
public class FileController {

	@Autowired
	UserService userService;

	@Autowired
	FileService fileService;

	@PostMapping("/api/user/setimage")
	public String setUserImage(Model model, @RequestParam("username") String username,
			@RequestParam("image") MultipartFile file) throws IOException {

		Optional<AppUser> optionalUser = userService.loadUserByUsername(username);
		AppUser user = optionalUser.orElseThrow();
		model.addAttribute("user", user);

		if (file.isEmpty()) {
			model.addAttribute("msg", "You must select image!");
			return "show-user-image";
		}

		int maxUploadSizeInKb = ImageSettings.getMaxLoadSizeInKb();

		if (file.getSize() > maxUploadSizeInKb) {
			model.addAttribute("msg", "Max image size is " + maxUploadSizeInKb / 1024 + " kb!");
			return "show-user-image";
		}

		userService.setUserImage(username, fileService.resizeImage(file, ImageSettings.getImageHeight()));
		return "show-user-image";
	}

	@GetMapping("/api/user/getimage")
	public String getUserImage(Model model, @RequestParam("username") String username) {
		model.addAttribute("username", username);
//		model.addAttribute("image", userService.loadUserImageBase64(username));
		return "show-user-image";
	}

	@GetMapping("/api/user/getimagepage/{username}")
	public String getUserImagePage(Model model, @PathVariable("username") String username) {
		Optional<AppUser> optionalUser = userService.loadUserByUsername(username);
		AppUser user = optionalUser.orElseThrow();
		model.addAttribute("user", user);
		return "show-user-image";
	}

	@RequestMapping(method = RequestMethod.GET, path = "/api/user/getimage/{username}", produces = MediaType.IMAGE_PNG_VALUE)
	public @ResponseBody byte[] getUserImage(@PathVariable("username") String username) {
		return userService.loadUserImage(username);
	}

}
