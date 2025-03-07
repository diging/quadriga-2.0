package edu.asu.diging.quadriga.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
        
        public void configure(AuthenticationManagerBuilder builder)
                throws Exception {
            builder.userDetailsService(userManager);
        }
        
        @Bean
        public WebSecurityCustomizer webSecurityCustomizer() throws Exception {
            
                    // Spring Security ignores request to static resources such as CSS or JS
                    // files.
            return (web)->web.ignoring().requestMatchers("/static/**");
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            HeadersConfigurer<HttpSecurity> config = http.cors().and().authorizeHttpRequests()
                    .requestMatchers("**").permitAll().and().csrf()
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
                            "/reset/**", "/**")
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
        
//        @Bean
//        public CitesphereAuthenticationProvider authenticationProvider() {
//            return new CitesphereAuthenticationProvider();
//        }
        
    }
    
    @Configuration
    @Order(1)
    public class ApiV1WebSecurityConfig {
        
        @Autowired
        private AuthenticationConfiguration authConfig;
        
        @Bean
        public AuthenticationManager authenticationManager() throws Exception {
            return authConfig.getAuthenticationManager();
        }
        
        @Bean
        public SecurityFilterChain apiFilterChain(HttpSecurity httpSecurity) throws Exception {
            CitesphereTokenFilter citesphereTokenFilter = new CitesphereTokenFilter("/api/v1/**");
            citesphereTokenFilter.setAuthenticationManager(authenticationManager());
            
            return httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                    .authorizeHttpRequests()
                    .requestMatchers("/api/v1/**").authenticated().and()
                    .addFilterBefore(citesphereTokenFilter, BasicAuthenticationFilter.class)
                    .csrf().disable().build();
        }
    }

}