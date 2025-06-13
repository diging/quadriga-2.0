package edu.asu.diging.quadriga.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
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
	
	@Autowired
	private UserDetailsService userManager;
	
	@Autowired
    private CitesphereAuthenticationProvider citesphereAuthProvider;
	
	@Autowired
	public void configure(AuthenticationManagerBuilder builder)
	        throws Exception {
	    builder.authenticationProvider(citesphereAuthProvider).userDetailsService(userManager);
	}
	
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() throws Exception {

		// Spring Security ignores request to static resources such as CSS or JS
		// files.
		return (web) -> web.ignoring().requestMatchers("/static/**");
	}
	
	@Bean
	@Order(1)
	SecurityFilterChain apiSecurityFilterChain(HttpSecurity http, AuthenticationConfiguration authenticationConfiguration) throws Exception {
		CitesphereTokenFilter citesphereTokenFilter = new CitesphereTokenFilter("/api/v1/**");
		citesphereTokenFilter.setAuthenticationManager(authenticationConfiguration.getAuthenticationManager());

	    return http
	            .securityMatcher("/api/**").addFilterBefore(citesphereTokenFilter, BasicAuthenticationFilter.class)
	            .authorizeHttpRequests(auth -> {
	                auth.anyRequest().authenticated();
	            })
	            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	            .httpBasic(withDefaults())
	            .build();
	}
	
	@Bean
	@Order(2)
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
				})).headers(headers -> headers.frameOptions().sameOrigin())
				.formLogin(login -> login.loginPage("/login").loginProcessingUrl("/login/authenticate")
						.failureUrl("/loginFailed"))
				.logout(logout -> logout.deleteCookies("JSESSIONID").logoutUrl("/logout").logoutSuccessUrl("/login"))
				.exceptionHandling(handling -> handling.accessDeniedPage("/403"))
				.authorizeHttpRequests(requests -> requests
						.requestMatchers("/", "/resources/**", "/register", "/login", "/loginFailed", "/register",
								"/logout", "/reset/**", "/citesphere/**")
						.permitAll().requestMatchers("/users/**", "/admin/**").hasRole("ADMIN")
						.requestMatchers("/auth/**").hasAnyRole("USER", "ADMIN").requestMatchers("/password/**")
						.hasRole(SimpleUsersConstants.CHANGE_PASSWORD_ROLE));
		return http.build();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(4);
	}

}