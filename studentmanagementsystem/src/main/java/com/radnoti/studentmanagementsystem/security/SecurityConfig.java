package com.radnoti.studentmanagementsystem.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final RequestFilter requestFilter;


    /**
     * Overrides the authenticationManagerBean method to expose the AuthenticationManager as a bean.
     *
     * @return The AuthenticationManager bean.
     * @throws Exception If an error occurs while creating the bean.
     */
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    /**
     * Configures the security settings for the HTTP requests.
     *
     * @param http The HttpSecurity object to configure.
     * @throws Exception If an error occurs while configuring the security settings.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .httpBasic().disable()
                .cors()
                .and()
                .authorizeHttpRequests()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/password-reset/**").permitAll()
                .and()
                .userDetailsService(userDetailsService())
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
                )
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}

