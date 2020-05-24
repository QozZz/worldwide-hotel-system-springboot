package com.qozz.worldwidehotelsystem.config.security;

import com.qozz.worldwidehotelsystem.data.enumeration.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JwtProviderTest {

    private static final String TOKEN = "token";
    private static final String SECRET_KEY = "secretKey";
    private static final String USER_NAME = "Admin";
    private static final String ROLE = "ROLE_" + Role.ADMIN.name();
    private static final String VALIDITY_IN_HOURS = "4";

    private static JwtProvider jwtProvider;

    @Mock
    private HttpServletRequest request;

    @BeforeClass
    public static void setUp() {
        inputField();
    }

    @Test
    public void whenRoleIsPresent() {
        String token = jwtProvider.createToken(USER_NAME, Collections.singleton(ROLE));

        assertNotNull(token);
        assertEquals(USER_NAME, parseToken(token).get("sub"));
        assertTrue((parseToken(token).get("auth")).toString().contains(ROLE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenRoleIsEmpty(){
        jwtProvider.createToken(USER_NAME, Collections.emptyList());
    }

    @Test
    public void whenTokenIsNullAndTryResolve() {
        when(request.getHeader(anyString())).thenReturn(null);

        assertNull(jwtProvider.resolveToken(request));
    }

    @Test
    public void whenTokenDoNotStartWithPrefix() {
        when(request.getHeader(anyString())).thenReturn(TOKEN);

        assertNull(jwtProvider.resolveToken(request));
    }

    @Test
    public void whenTokenStartWithPrefix() {
        when(request.getHeader(anyString())).thenReturn(JwtProvider.TOKEN_PREFIX + TOKEN);

        assertEquals(TOKEN, jwtProvider.resolveToken(request));
    }

    private Claims parseToken(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private static void inputField() {
        jwtProvider = new JwtProvider();
        ReflectionTestUtils.setField(jwtProvider, "encodedSecretKey", SECRET_KEY);
        ReflectionTestUtils.setField(jwtProvider, "expirationTimeHours", VALIDITY_IN_HOURS);
    }
}
