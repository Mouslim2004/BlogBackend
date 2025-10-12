package com.comorosrising.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    // Once per request filter ensure that the filter run once per request : spring security base class for filters
    // 1. We gonna initialize jwtUtil (for token operation) then user details service for loading user from database
    private final JWTUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthFilter(JWTUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 2. We gonna get header from the request
        String header = request.getHeader("Authorization");

        // 3. Then we gonna check is header is not null in order to retrieve tke from that
        if(header != null && header.startsWith("Bearer ")){
            String token = header.substring(7); // get token

            // 4. Then we gonna check if token is valid or not
            if(jwtUtil.isValid(token)){
                String email = jwtUtil.extractEmail(token);

                if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){ // re authentication if user is already authenticated
                    // 5. Load user details : fetch user from database using email
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                    // 6. Create authentication object: create spring security authentication token
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // 7. Set security context : stores the authentication in spring security's context
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }
        // 8. Continue filter chain : passes request to the next filter in chain
        filterChain.doFilter(request, response);
    }
}
