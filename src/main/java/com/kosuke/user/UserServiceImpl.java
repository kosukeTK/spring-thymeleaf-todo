package com.kosuke.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kosuke.token.ConfirmationToken;
import com.kosuke.token.ConfirmationTokenService;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

/**
 * The UserServiceImpl class
 *
 * @author kosuke takeuchi
 * @version 1.0 Date 2021/8/15.
 */
@Service
@Transactional(rollbackOn = (Exception.class))
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ConfirmationTokenService confirmationTokenService;

	@Override
	public User save(User user) {
		return userRepository.save(user);
	}

	@Override
	public Boolean delete(int id) {
		if (userRepository.existsById(id)) {
			userRepository.deleteById(id);
			return true;
		}
		return false;
	}

	@Override
	public User update(User user) {
		return userRepository.save(user);
	}

	@Override
	public User findById(int id) {
		return userRepository.findById(id).get();
	}

	@Override
	public User findByUserName(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public Collection<User> findAll() {
		Iterable<User> itr = userRepository.findAll();
		return (Collection<User>) itr;
	}
	
	@Override
	public int enableUser(String email) {
		return userRepository.enableUser(email);
	}

	@Override
	public String confirmToken(String token) {
		ConfirmationToken confirmationToken = confirmationTokenService
				.getToken(token)
				.orElseThrow(() -> 
						new IllegalStateException("token not found"));
		
		if(confirmationToken.getConfirmedAt() != null) {
			throw new IllegalStateException("email already comfirmed");
		}
		
		if(confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
			throw new IllegalStateException("email expires");
		}
		
		confirmationTokenService.setConfirmedAt(token);
		enableUser(confirmationToken.getUser().getEmail());
		
		return "confirmed";
	}
}
