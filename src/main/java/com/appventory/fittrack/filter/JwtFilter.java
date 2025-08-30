package com.appventory.fittrack.filter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.appventory.fittrack.model.User;
import com.appventory.fittrack.repository.UserRepository;
import com.appventory.fittrack.security.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final UserRepository userRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// Extract JWT from cookies
		String token = extractTokenFromCookies(request);

		if (token != null && jwtUtil.validateToken(token)) {
			String email = jwtUtil.getUsernameFromToken(token);

			Optional<User> userOpt = userRepository.findByEmail(email);
			if (userOpt.isPresent()) {
				User user = userOpt.get();

				Set<String> roles = user.getRoles();
				List<SimpleGrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new)
						.toList();

				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email,
						null, authorities);

				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}

		filterChain.doFilter(request, response);
	}

	private String extractTokenFromCookies(HttpServletRequest request) {
		if (request.getCookies() == null)
			return null;
		for (Cookie cookie : request.getCookies()) {
			if ("JWT_TOKEN".equals(cookie.getName())) {
				return cookie.getValue();
			}
		}
		return null;
	}
}
