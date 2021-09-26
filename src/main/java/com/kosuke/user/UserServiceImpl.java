package com.kosuke.user;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kosuke.token.ConfirmationToken;
import com.kosuke.token.ConfirmationTokenService;

import lombok.AllArgsConstructor;

/**
 * The UserServiceImpl class
 *
 * @author kosuke takeuchi
 * @version 1.0 Date 2021/8/15.
 */
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	private final ConfirmationTokenService confirmationTokenService;
	
	private final MessageSource messagesource;
    /**
     * ユーザ登録
     * @param user
     * @return
     */
	@Override
	public User save(User user) {
		return userRepository.save(user);
	}

	/**
	 * ユーザ削除
	 */
	@Override
	public Boolean delete(int id) {
		if (userRepository.existsById(id)) {
			userRepository.deleteById(id);
			return true;
		}
		return false;
	}

	/**
	 * ユーザ更新
	 */
	@Override
	public User update(User user) {
		return userRepository.save(user);
	}

	/**
	 * ID検索
	 */
	@Override
	public User findById(int id) {
		return userRepository.findById(id).get();
	}

	/**
	 * ユーザ名検索
	 */
	@Override
	public User findByUserName(String username) {
		return userRepository.findByUsername(username);
	}

	/**
	 * メール検索
	 */
	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	/**
	 * 全件検索
	 */
	@Override
	public Collection<User> findAll() {
		Iterable<User> itr = userRepository.findAll();
		return (Collection<User>) itr;
	}

	/**
	 * ユーザ有効化登録
	 */
	@Override
	public int enableUser(String email) {
		return userRepository.enableUser(email);
	}
	
	/**
	 * トークンを確認し、確認日時登録
	 */
	@Override
	public String confirmToken(String token) {
		ConfirmationToken confirmationToken = confirmationTokenService.getToken(token)
				.orElseThrow(() -> new IllegalStateException("token not found"));

		if (confirmationToken.getConfirmedAt() != null) {
//			throw new IllegalStateException("email already comfirmed");
			return messagesource.getMessage("hoge", new String[]{"fuga"}, Locale.JAPAN);
		}

		if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
			throw new IllegalStateException("email expires");
		}

		confirmationTokenService.setConfirmedAt(token);
		enableUser(confirmationToken.getUser().getEmail());
		return "success";
	}
}
