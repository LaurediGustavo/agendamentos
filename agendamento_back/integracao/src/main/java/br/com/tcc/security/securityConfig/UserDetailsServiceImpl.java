package br.com.tcc.security.securityConfig;

import br.com.tcc.entity.Ususario;
import br.com.tcc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired(required=true)
	private UserRepository userRepository;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Ususario usuario = userRepository.findByUserName(username)
                .orElseThrow(() -> new BadCredentialsException("User Not Found"));
		
		return new User(usuario.getUsername(), 
						usuario.getPassword(), 
						true, 
						true, 
						true, 
						true, 
						usuario.getAuthorities());
	}
	
}
