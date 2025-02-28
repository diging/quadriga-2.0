package edu.asu.diging.quadriga.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import org.springframework.security.web.util.matcher.RequestMatcher;

import edu.asu.diging.quadriga.config.web.CitesphereTokenFilter;
import edu.asu.diging.simpleusers.core.service.SimpleUsersConstants;
import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class SecurityContext {
    
    @Configuration
    @Order(2)
    public static class WebSecurityConfig {
        
        @Autowired
        private UserDetailsService userManager;
        
        @Bean
        InMemoryUserDetailsManager userDetailsService() {
            UserDetails user = User.withDefaultPasswordEncoder()
              .username("Admin")
              .password("admin")
              .roles("ROLE_ADMIN")
              .roles("ROLE_USER")
              .build();

            return new InMemoryUserDetailsManager(user);
        }
        
        @Bean
        public WebSecurityCustomizer webSecurityCustomizer() throws Exception {           
                    // Spring Security ignores request to static resources such as CSS or JS
                    // files.
            return (web)-> web.ignoring().requestMatchers("/static/**");
        }
        
        @Bean
        SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.cors().and().authorizeHttpRequests()
            .requestMatchers("**");
            HeadersConfigurer<HttpSecurity> config = http.csrf()
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
                    .and().authorizeHttpRequests()
                    // Anyone can access the urls
                    .requestMatchers("/", "/resources/**", "/register", "/login", "/loginFailed", "/register", "/logout",
                            "/reset/**")
                    .permitAll()
                    // The rest of the our application is protected.
                    .requestMatchers("/users/**", "/admin/**").hasRole("ADMIN")
                    .requestMatchers("/auth/**").hasAnyRole("USER", "ADMIN")
                    .requestMatchers("/password/**").hasRole(SimpleUsersConstants.CHANGE_PASSWORD_ROLE);
            return http.build();
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
        SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
            CitesphereTokenFilter citesphereTokenFilter = new CitesphereTokenFilter("/api/v1/**");
            citesphereTokenFilter.setAuthenticationManager(authenticationManager());
            
            httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                    .authorizeHttpRequests().requestMatchers("/api/v1/**").permitAll().and()
                    .addFilterBefore(citesphereTokenFilter, BasicAuthenticationFilter.class)
                    .csrf().disable();
        }
        
        @Bean
        public CitesphereAuthenticationProvider authenticationProvider() {
            return new CitesphereAuthenticationProvider();
        }

    }

}