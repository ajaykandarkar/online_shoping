package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.repository.DemoRepository;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class CustomerUserDetailsService implements UserDetailsService{

	@Autowired
	private DemoRepository repo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return repo.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));	
	}

}
