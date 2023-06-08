package edu.asu.diging.quadriga.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import edu.asu.diging.quadriga.config.web.CitesphereTokenFilter;
import edu.asu.diging.simpleusers.core.service.SimpleUsersConstants;

@Configuration
@EnableWebSecurity
public class SecurityContext extends WebSecurityConfigurerAdapter {
    
    @Configuration
    @Order(2)
    public static class WebSecurityConfig extends WebSecurityConfigurerAdapter {
        
        @Autowired
        private UserDetailsService userManager;
        
        public void configure(AuthenticationManagerBuilder builder)
                throws Exception {
            builder.userDetailsService(userManager);
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            web
                    // Spring Security ignores request to static resources such as CSS or JS
                    // files.
                    .ignoring().antMatchers("/static/**");
        }
    
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            HeadersConfigurer<HttpSecurity> config = http.cors().and().antMatcher("**").csrf()
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
                    .antMatchers("/", "/resources/**", "/register", "/login", "/loginFailed", "/register", "/logout",
                            "/reset/**")
                    .permitAll()
                    // The rest of the our application is protected.
                    .antMatchers("/users/**", "/admin/**").hasRole("ADMIN")
                    .antMatchers("/auth/**").hasAnyRole("USER", "ADMIN")
                    .antMatchers("/password/**").hasRole(SimpleUsersConstants.CHANGE_PASSWORD_ROLE);
        }
    
        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder(4);
        }
        
    }
    
    @Configuration
    @Order(1)
    public class ApiV1WebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity httpSecurity) throws Exception {
            CitesphereTokenFilter citesphereTokenFilter = new CitesphereTokenFilter("/api/v1/**");
            citesphereTokenFilter.setAuthenticationManager(authenticationManager());
            
            httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                    .antMatcher("/api/v1/**").addFilterBefore(citesphereTokenFilter, BasicAuthenticationFilter.class)
                    .csrf().disable();
        }
        
        @Bean
        public CitesphereAuthenticationProvider authenticationProvider() {
            return new CitesphereAuthenticationProvider();
        }

    }

}