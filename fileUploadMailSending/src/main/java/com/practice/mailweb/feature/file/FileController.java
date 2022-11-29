package com.practice.mailweb.feature.file;

import java.awt.Color;
import java.awt.Graphics2D;
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
import com.practice.mailweb.settings.filesettings.ImageSettings;

@Controller
public class FileController {

	@Autowired
	UserService userService;

	private byte[] resizeImage(MultipartFile file, int newHeight) {
		try {
			BufferedImage bi = ImageIO.read(file.getInputStream());
			
			String extension = FilenameUtils.getExtension(file.getOriginalFilename());	
			int newWidth = getWidthToPerserveScale(newHeight, bi);
			
			BufferedImage resized = resizeImage(bi, newWidth, newHeight);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			if("png".equalsIgnoreCase(extension)) {
				ImageIO.write(paintBackground(resized), extension, baos);
			} else {
				ImageIO.write(resized, extension, baos);
			}
			
			return baos.toByteArray();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}

	}

	private BufferedImage paintBackground(BufferedImage image) {
		BufferedImage painted = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d =painted.createGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, painted.getWidth(), painted.getHeight());
		g2d.drawImage(image, 0, 0, null);
		g2d.dispose();
		return painted;
	}

	private int getWidthToPerserveScale(int newHeight, BufferedImage bi) {
		double oldWidth = bi.getWidth();
		double oldHeight = bi.getHeight();
		int newWidth = (int) (newHeight / oldHeight * oldWidth);
		return newWidth;
	}

	private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight)
			throws IOException {
		Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
		BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
		outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
		return outputImage;
	}

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

		userService.setUserImage(username, resizeImage(file, ImageSettings.getImageHeight()));
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
