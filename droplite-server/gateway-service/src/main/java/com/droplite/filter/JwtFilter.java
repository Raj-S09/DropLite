package com.droplite.filter;

import com.droplite.dto.ResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.auth.AuthenticationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Component
public class JwtFilter implements WebFilter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("#{'${security.paths-to-skip}'.split(',')}")
    private String[] pathsToSkip;

    public static final String HEADER_AUTHORIZATION_PREFIX = "Bearer ";
    public static final String KEY_AUTHORITY = "role";
    public static final String GUEST_ACCESS = "guest";

    @NonNull
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        return ServerWebExchangeMatchers.pathMatchers(pathsToSkip).matches(exchange)
                .flatMap(matchResult -> authenticate(matchResult, exchange, chain));
    }

    private Mono<Void> authenticate(ServerWebExchangeMatcher.MatchResult matchResult, ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (matchResult.isMatch()) {
            return successHandler(exchange, chain, GUEST_ACCESS, GUEST_ACCESS);
        }
        if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            return failureHandler(exchange, new AuthenticationException("Authentication failed - Insufficient details."));
        }
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(HEADER_AUTHORIZATION_PREFIX)) {
            return failureHandler(exchange, new AuthenticationException("Authentication not supported"));
        }
        String token = authHeader.substring(HEADER_AUTHORIZATION_PREFIX.length());
        Claims claims = validateToken(token);
        if (claims == null) {
            return failureHandler(exchange, new AuthenticationException("Authentication failed - Please login again."));
        }
        String username = claims.getSubject();
        String role = claims.get(KEY_AUTHORITY, String.class);
        return successHandler(exchange, chain, username, role);
    }

    private Mono<Void> successHandler(ServerWebExchange exchange, WebFilterChain chain, String username, String role) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                username, null, List.of(new SimpleGrantedAuthority(role)));
        SecurityContext securityContext = new SecurityContextImpl(authentication);
        return chain.filter(exchange)
                .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
    }

    private Mono<Void> failureHandler(ServerWebExchange exchange, Exception exception) {
        ResponseDto responseDto = new ResponseDto(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.name(), exception.getMessage());
        ServerHttpResponse response = exchange.getResponse();
        try {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return response
                    .writeWith(Mono.just(response.bufferFactory().wrap(objectMapper.writeValueAsBytes(responseDto))))
                    .contextWrite(ReactiveSecurityContextHolder.clearContext());
        } catch (JsonProcessingException e) {
            return Mono.error(new RuntimeException(e));
        }
    }

    private Claims validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
            return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        } catch (Exception e) {
            return null;
        }
    }
}