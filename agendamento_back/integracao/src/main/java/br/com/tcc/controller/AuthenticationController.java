package br.com.tcc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.com.tcc.dto.UsuarioDto;
import br.com.tcc.model.response.LoginResponse;
import br.com.tcc.security.jwtConfig.JwtTokenUtil;
import br.com.tcc.security.securityConfig.UserDetailsServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

	private final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
    private AuthenticationManager authenticationManager;
    
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
    
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UsuarioDto usuarioDto) {
        LoginResponse loginResponse = new LoginResponse();
        HttpStatus status;

        Authentication auth = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(usuarioDto.getUserName(),
                              usuarioDto.getPassword()));

        if (auth.isAuthenticated()) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(usuarioDto.getUserName());
            String token = jwtTokenUtil.generateToken(userDetails);

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            loginResponse.setTokenJwt(token);
            loginResponse.setRoles(roles);
            status = HttpStatus.OK;
        } else {
            loginResponse.setMessage("{credenciais.invalidas}");
            status = HttpStatus.UNAUTHORIZED;
        }

        return ResponseEntity.status(status).body(loginResponse);
    }
	
}
