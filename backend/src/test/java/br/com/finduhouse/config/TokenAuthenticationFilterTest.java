package br.com.finduhouse.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.finduhouse.service.TokenService;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class TokenAuthenticationFilterTest {

    private TokenAuthenticationFilter filter;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        filter = new TokenAuthenticationFilter(
                mock(TokenService.class),
                new ObjectMapper().findAndRegisterModules());
        filterChain = mock(FilterChain.class);
    }

    @Test
    void devePermitirPreflightNoLogin() throws Exception {
        MockHttpServletResponse response = executar("OPTIONS", "/api/auth/login");

        verify(filterChain).doFilter(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any());
        assertEquals(200, response.getStatus());
    }

    @Test
    void devePermitirPreflightEmVagas() throws Exception {
        MockHttpServletResponse response = executar("OPTIONS", "/api/vagas");

        verify(filterChain).doFilter(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any());
        assertEquals(200, response.getStatus());
    }

    @Test
    void deveRejeitarVagasSemAutorizacao() throws Exception {
        MockHttpServletResponse response = executar("GET", "/api/vagas");

        assertEquals(401, response.getStatus());
    }

    @Test
    void deveRejeitarInteresseSemAutorizacao() throws Exception {
        MockHttpServletResponse response = executar("POST", "/api/vagas/1/interesse");

        assertEquals(401, response.getStatus());
    }

    @Test
    void devePermitirLoginPorPost() throws Exception {
        MockHttpServletResponse response = executar("POST", "/api/auth/login");

        verify(filterChain).doFilter(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any());
        assertEquals(200, response.getStatus());
    }

    private MockHttpServletResponse executar(String method, String path) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest(method, path);
        MockHttpServletResponse response = new MockHttpServletResponse();
        filter.doFilter(request, response, filterChain);
        return response;
    }
}
