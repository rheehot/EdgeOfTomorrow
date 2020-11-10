package com.oracle.eot.login;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.oracle.eot.dao.User;
import com.oracle.eot.exception.EotException;
import com.oracle.eot.repo.UserRepository;

@Service
public class EotUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {

		Optional<User> userOpt = userRepository.findById(userid);
		if (!userOpt.isPresent()) {
//          throw new IllegalArgumentException();
			throw new EotException(9001, userid + " is not exist");
		}
		
		User user = userOpt.get();

		EotUserDetails userDetails = new EotUserDetails();
		userDetails.setUsername(user.getUserid());
		userDetails.setPassword(user.getPassword());
		userDetails.setAuthorities(getAuthorities(userid));
		userDetails.setEnabled(true);
		userDetails.setAccountNonExpired(true);
		userDetails.setAccountNonLocked(true);
		userDetails.setCredentialsNonExpired(true);

		return userDetails;
	}

	public Collection<GrantedAuthority> getAuthorities(String username) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("USER"));
		return authorities;
	}

}
