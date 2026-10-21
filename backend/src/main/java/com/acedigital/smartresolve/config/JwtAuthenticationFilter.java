package com.acedigital.smartresolve.config;

import com.acedigital.smartresolve.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String userEmail;

        // 1. If the Authorization header is missing or doesn't start with Bearer, skip this filter
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extract the token from the header block
        jwtToken = authHeader.substring(7);
        userEmail = jwtService.extractEmail(jwtToken);

        // 3. If there is a valid email and the user is not authenticated yet in the security context
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 4. Validate the token signature and status parameters
            if (jwtService.isTokenValid(jwtToken, userEmail)) {

                // 5. Build an internal authentication token matching their role parameters
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userEmail,
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 6. Lock this validated user token directly into Spring's secure request memory box
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 7. Hand control over seamlessly to the next component in the filter chain row
        filterChain.doFilter(request, response);
    }
}
