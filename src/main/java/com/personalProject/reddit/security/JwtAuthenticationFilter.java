package com.personalProject.reddit.security;

import com.personalProject.reddit.exceptionHandler.SpringRedditException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.context.SecurityContextHolder;


import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    UserDetailsService userDetailsService;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();

        if (isExemptedPath(path)) {
            filterChain.doFilter(request, response);
        }
        else {
            String jwt = getJwtFromRequest(request);
            boolean isJwtValidate = jwtProvider.validateToken(jwt);

            if (StringUtils.hasText(jwt) && isJwtValidate) {

                String userNameFromJwt = jwtProvider.getUserNameFromJwt(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(userNameFromJwt);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } else {
                throw new SpringRedditException("Invalid Access Token Provided by User");
            }
            filterChain.doFilter(request, response);
        }
    }

    private String getJwtFromRequest(HttpServletRequest request) {

        String bearerToken = request.getHeader("Authorization");
        
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")){
            return bearerToken.substring(7);
        }
        return bearerToken;
    }

    private boolean isExemptedPath(String requestPath) {
        // Define the exempted paths here
        String exemptedPaths = "/**";

            if (pathMatcher.match(exemptedPaths, requestPath)) {
                return true;
            }
        return false;
    }
}
