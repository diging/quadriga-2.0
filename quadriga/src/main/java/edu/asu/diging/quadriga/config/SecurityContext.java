package edu.asu.diging.quadriga.config;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import org.springframework.security.web.util.matcher.RequestMatcher;

import edu.asu.diging.quadriga.config.web.CitesphereTokenFilter;
import edu.asu.diging.simpleusers.core.service.SimpleUsersConstants;

@Configuration
@EnableWebSecurity
public class SecurityContext {
    
    @Configuration
    @Order(2)
    public static class WebSecurityConfig {
        
//        @Autowired
//        private UserDetailsService userManager;
        
//        public void configure(AuthenticationManagerBuilder builder)
//                throws Exception {
//            builder.userDetailsService(userManager);
//        }
        
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.cors().and()
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/**").permitAll() // Allows all requests
            )
            .csrf(csrf -> csrf
                .requireCsrfProtectionMatcher(new RequestMatcher() {
                    @Override
                    public boolean matches(HttpServletRequest request) {
                        // Don't require CSRF for REST API calls
                        if (request.getRequestURI().startsWith("/api/")) {
                            return false;
                        }
                        // CSRF not required for GET requests
                        if ("GET".equals(request.getMethod())) {
                            return false;
                        }
                        return true;
                    }
                })
            )
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin())) // Allow frames from the same origin
            .formLogin(formLogin -> formLogin
                .loginPage("/login")
                .loginProcessingUrl("/login/authenticate")
                .failureUrl("/loginFailed")
            )
            .logout(logout -> logout
                .deleteCookies("JSESSIONID")
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
            )
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .accessDeniedPage("/403")
            )
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/", "/resources/**", "/register", "/login", "/loginFailed", "/reset/**").permitAll()
                .requestMatchers("/users/**", "/admin/**").hasRole("ADMIN")
                .requestMatchers("/auth/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/password/**").hasRole(SimpleUsersConstants.CHANGE_PASSWORD_ROLE)
            );

            return http.build(); // Return the configured SecurityFilterChain
        }
   
        protected void configure(HttpSecurity http) throws Exception {
            HeadersConfigurer<HttpSecurity> config = http.cors().and()
                    .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("**").permitAll()
                    ).csrf()
                    .requireCsrfProtectionMatcher(new RequestMatcher() {
                        @Override
                        public boolean matches(HttpServletRequest arg0) {
                         // don't require CSRF for REST calls
                            if (arg0.getRequestURI().indexOf("/api/") > -1) {
                                return false;
                            }
                            if (arg0.getMethod().equals("GET")) {
                                return false;
                            }
                            return true;
                        }

                    }).and().headers().frameOptions().sameOrigin();
             
            config.and().formLogin().loginPage("/login").loginProcessingUrl("/login/authenticate").failureUrl("/loginFailed").and()
                    .logout()
                    .deleteCookies("JSESSIONID")
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login")
                    .and().exceptionHandling().accessDeniedPage("/403")
                    // Configures url based authorization
                    .and().authorizeRequests()
                    // Anyone can access the urls
                    .requestMatchers("/", "/resources/**", "/register", "/login", "/loginFailed", "/register", "/logout",
                            "/reset/**")
                    .permitAll()
                    // The rest of the our application is protected.
                    .requestMatchers("/users/**", "/admin/**").hasRole("ADMIN")
                    .requestMatchers("/auth/**").hasAnyRole("USER", "ADMIN")
                    .requestMatchers("/password/**").hasRole(SimpleUsersConstants.CHANGE_PASSWORD_ROLE);
        }
    
        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder(4);
        }
        
    }
    
    @Configuration
    @Order(1)
    public class ApiV1WebSecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationManager authenticationManager) throws Exception {
            CitesphereTokenFilter citesphereTokenFilter = new CitesphereTokenFilter("/api/v1/**");
            citesphereTokenFilter.setAuthenticationManager(authenticationManager);
            
            httpSecurity
                .sessionManagement(sessionManagement -> 
                    sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless session policy
                )
                .securityMatcher("/api/v1/**") // Instead of antMatcher in Spring Security 6
                .addFilterBefore(citesphereTokenFilter, UsernamePasswordAuthenticationFilter.class) // Add custom filter before basic auth
                .csrf(csrf -> csrf.disable()); // Disable CSRF protection

            return httpSecurity.build(); // Build and return the SecurityFilterChain
        }
        
        @Bean
        public CitesphereAuthenticationProvider authenticationProvider() {
            return new CitesphereAuthenticationProvider();
        }

    }

}