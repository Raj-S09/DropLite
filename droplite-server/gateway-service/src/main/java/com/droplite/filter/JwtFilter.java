package com.droplite.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class JwtFilter implements WebFilter {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("#{'${security.paths-to-skip}'.split(',')}")
    private List<String> pathsToSkip;

    public static final String HEADER_AUTHORIZATION_PREFIX = "Bearer ";
    public static final String KEY_AUTHORITY = "role";

    @NonNull
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        // Skip authentication for whitelisted paths
        if (pathsToSkip.contains(path)) {
            return successHandler(exchange, chain, "guest","guest");
        }
        if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            return chain.filter(exchange);
        }
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(HEADER_AUTHORIZATION_PREFIX)) {
            return chain.filter(exchange);
        }
        String token = authHeader.substring(HEADER_AUTHORIZATION_PREFIX.length());
        Claims claims = validateToken(token);
        if (claims == null) {
            return chain.filter(exchange);
        }
        String username = claims.getSubject();
        String role = claims.get(KEY_AUTHORITY, String.class);
        return successHandler(exchange, chain, username, role);
    }

    private static Mono<Void> successHandler(ServerWebExchange exchange, WebFilterChain chain, String username, String role) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                username, null, List.of(new SimpleGrantedAuthority(role)));
        SecurityContext securityContext = new SecurityContextImpl(authentication);
        return chain.filter(exchange)
                .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
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