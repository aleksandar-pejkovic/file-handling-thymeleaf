package com.practice.mailweb.feature.user;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.practice.mailweb.feature.email.EmailService;

@Service
public class UserService implements IUserService {

	@Autowired
	private EmailService mailService;

	@Autowired
	private UserRepository userRepository;

	public AppUser createNewUser(AppUser user) {
		mailService.send("User " + user.getUsername() + " created");
		return userRepository.save(user);
	}

	public AppUser updateUser(AppUser user) {
		return userRepository.save(user);
	}

	public String deleteUser(String username) {
		Optional<AppUser> optionalUser = userRepository.findByUsername(username);
		userRepository.delete(optionalUser.orElseThrow());
		mailService.send("User " + username + " deleted");
		return username + " deleted!";
	}

	public List<AppUser> loadAllUsers() {
		return (List<AppUser>) userRepository.findAll();
	}

	@Override
	public Optional<AppUser> loadUserByUsername(String username) {
		return Optional.ofNullable(userRepository.findByUsername(username)).orElseThrow();
	}

	@Override
	public Optional<AppUser> loadUserByEmail(String email) {
		return Optional.ofNullable(userRepository.findByEmail(email)).orElseThrow();
	}

	public boolean verifyCredentials(String username, String password) {
		Optional<AppUser> optionalUser = userRepository.findByUsername(username);
		return password.equals(optionalUser.map(AppUser::getPassword).orElse(""));
	}
	
	public void setUserImage(String username, MultipartFile file) throws IOException {
		Optional<AppUser> optionalUser = userRepository.findByUsername(username);
		AppUser user = optionalUser.orElseThrow();
		user.setImage(file.getBytes());
		userRepository.save(user);
	}
	
	public void setUserImage(String username, byte[] file) throws IOException {
		Optional<AppUser> optionalUser = userRepository.findByUsername(username);
		AppUser user = optionalUser.orElseThrow();
		user.setImage(file);
		userRepository.save(user);
	}
	
	public String loadUserImageBase64(String username) {
		Optional<AppUser> optionalUser = userRepository.findByUsername(username);
		AppUser user = optionalUser.orElseThrow();
		return new String(Base64.getEncoder().encode(user.getImage()));
	}
	
	public byte[] loadUserImage(String username) {
		Optional<AppUser> optionalUser = userRepository.findByUsername(username);
		AppUser user = optionalUser.orElseThrow();
		return user.getImage();
	}

}
