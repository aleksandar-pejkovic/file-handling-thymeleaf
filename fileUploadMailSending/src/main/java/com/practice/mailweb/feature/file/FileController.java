package com.practice.mailweb.feature.file;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
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

@Controller
public class FileController {
	
	@Autowired
	UserService userService;
	
	private byte[] resizeImage(MultipartFile file, int height) {
		try {
			int maxUploadSizeInMb = 100 * 1024;
			if(file.getSize() > maxUploadSizeInMb) {
				throw new RuntimeException();
			}
			BufferedImage bi = ImageIO.read(file.getInputStream());
			String extension = FilenameUtils.getExtension(file.getOriginalFilename());
			double oldWidth = bi.getWidth();
			double oldHeight = bi.getHeight();
			int newHeight = height;
			int newWidth = (int) (newHeight/oldHeight * oldWidth);
			BufferedImage resized = resizeImage(bi, newWidth, newHeight);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        ImageIO.write(resized, extension, baos);
	        return baos.toByteArray();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		
	}
	
	private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
	    Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
	    BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
	    outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
	    return outputImage;
	}
	
	@PostMapping("/api/user/setimage")
	public String setUserImage(Model model, @RequestParam("username") String username, @RequestParam("image") MultipartFile file) throws IOException {
		userService.setUserImage(username, resizeImage(file, 128));
		return "index";
	}
	
	@GetMapping("/api/user/getimage")
	public String getUserImage(Model model, @RequestParam("username") String username){	
		model.addAttribute("username", username);
//		model.addAttribute("image", userService.loadUserImageBase64(username));
		return "show-user-image";
	}
	
	@GetMapping("/api/user/getimagepage/{username}")
	public String getUserImagePage(Model model, @PathVariable("username") String username){
		Optional<AppUser> optionalUser = userService.loadUserByUsername(username);
		AppUser user = optionalUser.orElseThrow();
		model.addAttribute("user", user);
		return "show-user-image";
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/api/user/getimage/{username}", produces = MediaType.IMAGE_PNG_VALUE)
	public @ResponseBody byte[] getUserImage(@PathVariable("username") String username){
		return userService.loadUserImage(username);
	}

}
