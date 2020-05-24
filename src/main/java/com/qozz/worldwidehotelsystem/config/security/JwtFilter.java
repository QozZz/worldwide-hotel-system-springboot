package com.qozz.worldwidehotelsystem.config.security;

import com.qozz.worldwidehotelsystem.exception.JwtAuthorizationException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider provider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String token = provider.resolveToken(request);
        try {
            if (token != null && provider.validateToken(token)) {
                Authentication authentication
                        = new UsernamePasswordAuthenticationToken(
                        provider.getUsername(token), null, provider.getAuthorities(token)
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (JwtAuthorizationException ex) {
            SecurityContextHolder.clearContext();
            response.sendError(ex.getHttpCode(), ex.getMessage());
            return;
        }
        chain.doFilter(request, response);
    }
}
