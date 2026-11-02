package br.com.finduhouse.config;

import java.io.IOException;
import java.time.LocalDateTime;

import br.com.finduhouse.dto.ErrorResponse;
import br.com.finduhouse.service.AuthenticatedUser;
import br.com.finduhouse.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    public static final String AUTHENTICATED_USER_ATTRIBUTE =
            AuthenticatedUser.class.getName();

    private final TokenService tokenService;
    private final ObjectMapper objectMapper;

    public TokenAuthenticationFilter(TokenService tokenService, ObjectMapper objectMapper) {
        this.tokenService = tokenService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        if (isPublic(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            unauthorized(response, "Token de autenticação não informado");
            return;
        }

        String token = authorization.substring("Bearer ".length()).trim();
        var tokenData = tokenService.validar(token);
        if (tokenData.isEmpty()) {
            unauthorized(response, "Token de autenticação inválido");
            return;
        }

        TokenService.TokenData data = tokenData.get();
        request.setAttribute(
                AUTHENTICATED_USER_ATTRIBUTE,
                new AuthenticatedUser(data.usuarioId(), data.perfil()));
        filterChain.doFilter(request, response);
    }

    private boolean isPublic(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();
        return "/api/health".equals(path)
                || ("POST".equals(method) && "/api/auth/login".equals(path))
                || ("POST".equals(method) && "/api/estudantes".equals(path))
                || ("POST".equals(method) && "/api/proprietarios".equals(path))
                || !path.startsWith("/api/");
    }

    private void unauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(
                response.getWriter(),
                new ErrorResponse("TOKEN_INVALIDO", message, LocalDateTime.now()));
    }
}
