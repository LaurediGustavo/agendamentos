package br.com.tcc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.com.tcc.dto.UsuarioDto;
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
        Map<String, Object> responseMap = new HashMap<>();
        HttpStatus status;
        
        try {
            Authentication auth = authenticationManager
            		.authenticate(new UsernamePasswordAuthenticationToken(usuarioDto.getUserName(), 
            					  usuarioDto.getPassword()));
            
            if (auth.isAuthenticated()) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(usuarioDto.getUserName());
                String token = jwtTokenUtil.generateToken(userDetails);
                
        		List<String> roles = userDetails.getAuthorities().stream()
        				.map(item -> item.getAuthority())
        				.collect(Collectors.toList());
                
                responseMap.put("message", "Usuário logado com sucesso");
                responseMap.put("jwt_token", token);
                responseMap.put("roles", roles);
                status = HttpStatus.OK;
            } else {
                responseMap.put("message", "Credenciáis inválidas");
                status = HttpStatus.UNAUTHORIZED;
            }
        } catch (DisabledException e) {
            responseMap.put("message", "Usuário bloqueado");
            status = HttpStatus.UNAUTHORIZED;
            logger.error(e);
        } catch (BadCredentialsException e) {
            responseMap.put("message", "Credenciáis inválidas");
            status = HttpStatus.UNAUTHORIZED;
            logger.error(e);
        } catch (Exception e) {
            responseMap.put("message", "Erro inespesado! Contate o administrador");
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logger.error(e);
        }
        
        return ResponseEntity.status(status).body(responseMap);
    }
	
}
