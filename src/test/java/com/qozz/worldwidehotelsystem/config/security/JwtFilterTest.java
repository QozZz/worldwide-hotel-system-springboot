package com.qozz.worldwidehotelsystem.config.security;

import com.qozz.worldwidehotelsystem.exception.JwtAuthorizationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JwtFilterTest {

    private static final String TOKEN = "token";
    private JwtAuthorizationException authorizationException;

    @InjectMocks
    private JwtFilter jwtFilter;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    @Before
    public void setUp(){
        authorizationException = createException();
    }

    @Test
    public void whenTokenIsNull() throws ServletException, IOException {
        when(jwtProvider.resolveToken(any(HttpServletRequest.class))).thenReturn(null);

        jwtFilter.doFilterInternal(request, response, chain);

        verify(jwtProvider).resolveToken(request);
        verify(chain).doFilter(request, response);
        verify(response, never()).sendError(anyInt(), anyString());
        verify(jwtProvider, never()).validateToken(anyString());
    }

    @Test
    public void whenTokenIsInvalidAndThrowException() throws ServletException, IOException {
        when(jwtProvider.resolveToken(any(HttpServletRequest.class))).thenReturn(TOKEN);
        when(jwtProvider.validateToken(anyString())).thenThrow(authorizationException);

        jwtFilter.doFilterInternal(request, response, chain);

        verify(jwtProvider).resolveToken(request);
        verify(jwtProvider).validateToken(TOKEN);
        verify(response).sendError(anyInt(), anyString());
        verify(chain, never()).doFilter(request, response);
    }

    @Test
    public void whenTokenIsInvalidAndReturnFalse() throws ServletException, IOException {
        when(jwtProvider.resolveToken(any(HttpServletRequest.class))).thenReturn(TOKEN);
        when(jwtProvider.validateToken(anyString())).thenReturn(false);

        jwtFilter.doFilterInternal(request, response, chain);

        verify(jwtProvider).resolveToken(request);
        verify(jwtProvider).validateToken(TOKEN);
        verify(chain).doFilter(request, response);
        verify(response, never()).sendError(anyInt(), anyString());
    }

    @Test
    public void whenTokenIsValid() throws ServletException, IOException {
        when(jwtProvider.resolveToken(any(HttpServletRequest.class))).thenReturn(TOKEN);
        when(jwtProvider.validateToken(anyString())).thenReturn(true);

        jwtFilter.doFilterInternal(request, response, chain);

        verify(jwtProvider).resolveToken(request);
        verify(jwtProvider).validateToken(TOKEN);
        verify(chain).doFilter(request, response);
        verify(response, never()).sendError(anyInt(), anyString());
    }

    private static JwtAuthorizationException createException() {
        return new JwtAuthorizationException(new RuntimeException("Invalid Token"));
    }
}
