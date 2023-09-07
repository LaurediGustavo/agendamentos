package br.com.tcc.security.jwtConfig;

import br.com.tcc.security.securityConfig.UserDetailsServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException, java.io.IOException {

		final String requestTokenHeader = request.getHeader("Authorization");
		if (StringUtils.startsWith(requestTokenHeader, "Bearer ")) {
			String jwtToken = requestTokenHeader.substring(7);
			try {
				String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
				
				if (StringUtils.isNotEmpty(username)
						&& null == SecurityContextHolder.getContext().getAuthentication()) {
					UserDetails userDetails = userDetailsService.loadUserByUsername(username);
					
					if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
						UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
								userDetails, null, userDetails.getAuthorities());
						usernamePasswordAuthenticationToken
								.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
					}
				}
			} catch (IllegalArgumentException e) {
				logger.error("Unable to fetch JWT Token");
			} catch (ExpiredJwtException e) {
				logger.error("JWT Token is expired");
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		
		chain.doFilter(request, response);
	}

}
