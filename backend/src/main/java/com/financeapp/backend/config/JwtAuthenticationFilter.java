package com.financeapp.backend.config;

import com.financeapp.backend.services.CustomUserDetailsService;
import com.financeapp.backend.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            HandlerExceptionResolver handlerExceptionResolver,
            JwtService jwtService,
            CustomUserDetailsService userDetailsService
    ) {
        this.handlerExceptionResolver = handlerExceptionResolver;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }



    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            final String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            final String jwt = authHeader.substring(7);
            final String userEmail = jwtService.extractUsername(jwt);
            Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();

            if (userEmail != null && currentAuth == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
                if (jwtService.isTokenValid(jwt, userDetails)) {

                    List<String> roles = jwtService.extractRoles(jwt);

                    List<SimpleGrantedAuthority> auth= roles.stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList();



                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    auth
                            );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            handlerExceptionResolver.resolveException(request, response, null, ex);
        }
    }
}
