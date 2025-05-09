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
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityContext {
    
    @Configuration
    @Order(2)
    public static class WebSecurityConfig {
        
        @Autowired
        private UserDetailsService userManager;
        
        @Autowired
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
            http.cors(withDefaults())
                .authorizeHttpRequests(authorizationManager -> authorizationManager.requestMatchers("**"))
                .csrf(csrf -> csrf.requireCsrfProtectionMatcher(new RequestMatcher() {
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
                }))
                .headers(headers -> headers.frameOptions().sameOrigin())
                .formLogin(login -> login.loginPage("/login").loginProcessingUrl("/login/authenticate").failureUrl("/loginFailed"))
                .logout(logout -> logout.deleteCookies("JSESSIONID").logoutUrl("/logout").logoutSuccessUrl("/login"))
                .exceptionHandling(handling -> handling.accessDeniedPage("/403"))
                .authorizeHttpRequests(requests -> requests
                    .requestMatchers("/", "/resources/**", "/register", "/login", "/loginFailed", "/register", "/logout", "/reset/**", "/citesphere/**").permitAll()
                    .requestMatchers("/users/**", "/admin/**").hasRole("ADMIN")
                    .requestMatchers("/auth/**").hasAnyRole("USER", "ADMIN")
                    .requestMatchers("/password/**").hasRole(SimpleUsersConstants.CHANGE_PASSWORD_ROLE));
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
        
//        @Autowired
//        public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
//            return authConfig.getAuthenticationManager();
//        }
        
        @Bean
        public SecurityFilterChain apiFilterChain(HttpSecurity httpSecurity, AuthenticationConfiguration authenticationConfiguration) throws Exception {
            CitesphereTokenFilter citesphereTokenFilter = new CitesphereTokenFilter("/api/v1/**");
            citesphereTokenFilter.setAuthenticationManager(authenticationConfiguration.getAuthenticationManager());

            httpSecurity.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests(authorizationManager -> authorizationManager
                            .requestMatchers("/api/v1/**").permitAll()).addFilterBefore(citesphereTokenFilter, BasicAuthenticationFilter.class)
                    .csrf().disable();
            return httpSecurity.build();
        }
        
        @Bean
        public CitesphereAuthenticationProvider authenticationProvider() {
            return new CitesphereAuthenticationProvider();
        }
    }

}