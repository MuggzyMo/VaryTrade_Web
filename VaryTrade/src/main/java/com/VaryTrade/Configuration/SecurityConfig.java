package com.VaryTrade.Configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	private LoginFormFailureHandler loginFormFailureHandler;
	@Autowired
	private LoginFormSuccessHandler loginFormSuccessHandler;
	@Autowired
	private LoginOAuthSuccessHandler loginOAuthSuccessHandler;
	@Autowired
	private JwtAuthenticationFilter jwtAuthFilter;
	@Autowired
	private DataSource dataSource;
	@Autowired
	private OAuth2AuthenticationService oAuth2AuthenticationService;
	@Autowired
	private CollectorOpenTradeDealAuthorizationManager collectorOpenTradeDealAuthorizationManager;
	@Autowired
	private AcceptableOpenTradeDealAuthorizationManager acceptableOpenTradeDealAuthorizationManager;
	@Autowired
	private PendingTradeDealAuthorizationManager pendingTradeDealAuthorizationManager;
	@Autowired
	private ClosedTradeDealAuthorizationManager closedTradeDealAuthorizationManager;
	@Autowired
	private CollectorOpenResaleDealAuthorizationManager collectorOpenResaleDealAuthorizationManager;
	@Autowired
	private AcceptableOpenResaleDealAuthorizationManager acceptableOpenResaleDealAuthorizationManager;
	@Autowired
	private PendingResaleDealAuthorizationManager pendingResaleDealAuthorizationManager;
	@Autowired
	private ClosedResaleDealAuthorizationManager closedResaleDealAuthorizationManager;
	@Autowired
	private TradeDealTwoAuthorizationManager tradeDealTwoAuthorizationManager;
	@Autowired
	private TradeDealThreeAuthorizationManager tradeDealThreeAuthorizationManager;
	
	@Bean
    @Order(0)
    SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {		
		http.csrf(csrf -> csrf.disable());
		
		http.securityMatcher("/api/**").authorizeHttpRequests(request -> {
			request.requestMatchers("/api/google/register").hasAnyRole("GOOGLE_USER");
			request.requestMatchers("/api/account").hasAnyRole("USER");
			request.requestMatchers("/api/login").permitAll();
			request.requestMatchers("/api/google/login/verify").permitAll();
			request.requestMatchers("/api/register").permitAll();
			request.requestMatchers("/api/company").permitAll();
			request.requestMatchers("api/password/forgot").permitAll();
			request.requestMatchers("/api/states").permitAll();
			request.requestMatchers("/api/credit").hasAnyRole("USER");
			request.requestMatchers("/api/hyperwallet/verify").hasAnyRole("USER");
			request.requestMatchers("/api/password/setup/verify").hasAnyRole("USER");
			request.requestMatchers("/api/hyperwallet/registration").hasAnyRole("USER");
			request.requestMatchers("/api/payout").hasAnyRole("USER");
			request.requestMatchers("/api/collectible/types").hasAnyRole("USER");
			request.requestMatchers("/api/payout/history").hasAnyRole("USER");
			request.requestMatchers("/api/account/edit").hasAnyRole("USER");
			request.requestMatchers("/api/account/google/edit").hasAnyRole("USER");
			request.requestMatchers("/api/account/password/create").hasAnyRole("USER");
			request.requestMatchers("/api/account/password/edit").hasAnyRole("USER");
			request.requestMatchers("/api/trade-deal/open").hasAnyRole("USER");
			request.requestMatchers("/api/resale-deal/open").hasAnyRole("USER");
			request.requestMatchers("/api/your-trade-deals/pending").hasAnyRole("USER");
			request.requestMatchers("/api/your-trade-deals/open").hasAnyRole("USER");
			request.requestMatchers("/api/your-trade-deals/closed").hasAnyRole("USER");
			request.requestMatchers("/api/your-resale-deals/open").hasAnyRole("USER");
			request.requestMatchers("/api/your-resale-deals/pending").hasAnyRole("USER");
			request.requestMatchers("/api/your-resale-deals/closed").hasAnyRole("USER");
			request.requestMatchers("/api/followers").hasAnyRole("USER");
			request.requestMatchers("/api/following").hasAnyRole("USER");
			request.requestMatchers("/api/profile/follow").hasAnyRole("USER");
			request.requestMatchers("/api/profile/unfollow").hasAnyRole("USER");
			request.requestMatchers("/api/profile/follow-status").hasAnyRole("USER");
			request.requestMatchers("/api/profile/search").hasAnyRole("USER");
			request.requestMatchers("/api/trade-deal/profile/open").hasAnyRole("USER");
			request.requestMatchers("/api/resale-deal/profile/open").hasAnyRole("USER");
			request.requestMatchers("/api/trade-deal/create").hasAnyRole("USER");
			request.requestMatchers("/api/trade-deal/collectibles").hasAnyRole("USER");
			request.requestMatchers("/api/collectible/conditions").hasAnyRole("USER");
			request.requestMatchers("/api/attributes-one").hasAnyRole("USER");
			request.requestMatchers("/api/attributes-two").hasAnyRole("USER");
			request.requestMatchers("/api/attributes-three").hasAnyRole("USER");
			request.requestMatchers("/api/attributes/names").hasAnyRole("USER");
			request.requestMatchers("/api/resale-deal/create").hasAnyRole("USER");
			request.requestMatchers("/api/your-trade-deals/delete").hasAnyRole("USER");
			request.requestMatchers("/api/your-resale-deals/delete").hasAnyRole("USER");
			request.requestMatchers("/api/braintree/token").hasAnyRole("USER");
			request.requestMatchers("/api/resale-deal/open/payment/accept").hasAnyRole("USER");
			request.requestMatchers("/api/resale-deal/open/credit/accept").hasAnyRole("USER");
			request.requestMatchers("/api/trade-deal/open/payment/accept").hasAnyRole("USER");
			request.requestMatchers("/api/trade-deal/open/credit/accept").hasAnyRole("USER");
		});
		
		http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    	.addFilterAt(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		
        return http.build();
	}
	
	@Bean
    @Order(1)
    SecurityFilterChain webAppSecurityFilterChain(HttpSecurity http, SecurityContextRepository securityContextRepository) throws Exception {
        http.exceptionHandling(handling -> handling.accessDeniedPage("/403"));
        
        http.securityContext(context -> context.securityContextRepository(securityContextRepository));
        
        http.authorizeHttpRequests(request -> {
        	request.requestMatchers("/account/google/edit").hasAnyRole("USER");
        	request.requestMatchers("/google/register").hasAnyRole("GOOGLE_USER");
        	request.requestMatchers("/register").permitAll();
        	request .requestMatchers("/login**").permitAll();
            request.requestMatchers("/403").permitAll();
            request.requestMatchers("/your-trade-deals/open/details/delete").hasAnyRole("USER");
            request.requestMatchers("/", "/home").hasAnyRole("USER", "ADMIN");
            request.requestMatchers("/account/password/create").hasAnyRole("USER");
            request.requestMatchers("/admin/resale-deal/pass/details").hasAnyRole("ADMIN");
            request.requestMatchers("/admin/resale-deal/fail/details").hasAnyRole("ADMIN");
            request.requestMatchers("/admin/trade-deal/pass/details").hasAnyRole("ADMIN");
            request.requestMatchers("/admin/trade-deal/fail/details").hasAnyRole("ADMIN");
            request.requestMatchers("/contact").hasAnyRole("USER", "ADMIN");
            request.requestMatchers("/account").hasAnyRole("USER", "ADMIN");
            request.requestMatchers("/account/edit").hasAnyRole("USER", "ADMIN");
            request.requestMatchers("/account/password/edit").hasAnyRole("USER", "ADMIN");
            request.requestMatchers("/trade-deal/create/type-1/receive/remove").hasAnyRole("USER");
            request.requestMatchers("/trade-deal/create/type-1/trade/remove").hasAnyRole("USER");
            request.requestMatchers("/trade-deal/create/type-1").hasAnyRole("USER");
            request.requestMatchers("/trade-deal/create/type-2/receive/remove").access(tradeDealTwoAuthorizationManager);
            request.requestMatchers("/trade-deal/create/type-2/trade/remove").access(tradeDealTwoAuthorizationManager);
            request.requestMatchers("/trade-deal/create/type-2").access(tradeDealTwoAuthorizationManager);
            request.requestMatchers("/trade-deal/create/type-3/receive/remove").access(tradeDealThreeAuthorizationManager);
            request.requestMatchers("/trade-deal/create/type-3/trade/remove").access(tradeDealThreeAuthorizationManager);
            request.requestMatchers("/trade-deal/create/type-3").access(tradeDealThreeAuthorizationManager);
            request.requestMatchers("/your-trade-deals").hasAnyRole("USER");
            request.requestMatchers("/your-resale-deals/open/details/delete").hasAnyRole("USER");
            request.requestMatchers("/trade-deal/create/post/confirmation").access(collectorOpenTradeDealAuthorizationManager);
            request.requestMatchers("/your-trade-deals/type-1/open").hasAnyRole("USER");
            request.requestMatchers("/your-trade-deals/open/details").access(collectorOpenTradeDealAuthorizationManager);
            request.requestMatchers("/your-trade-deals/pending/details").access(pendingTradeDealAuthorizationManager);
            request.requestMatchers("/your-trade-deals/type-1/pending").hasAnyRole("USER");
            request.requestMatchers("/your-trade-deals/type-1/closed").hasAnyRole("USER");
            request.requestMatchers("/your-trade-deals/type-2/open").access(tradeDealTwoAuthorizationManager);
            request.requestMatchers("/your-trade-deals/type-2/pending").access(tradeDealTwoAuthorizationManager);
            request.requestMatchers("/your-trade-deals/type-2/closed").access(tradeDealTwoAuthorizationManager);
            request.requestMatchers("/your-trade-deals/type-3/open").access(tradeDealThreeAuthorizationManager);
            request.requestMatchers("/your-trade-deals/type-3/pending").access(tradeDealThreeAuthorizationManager);
            request.requestMatchers("/your-trade-deals/type-3/closed").access(tradeDealThreeAuthorizationManager);
            request.requestMatchers("/your-trade-deals/closed/details").access(closedTradeDealAuthorizationManager);
            request.requestMatchers("/trade-deal/open/type-1").hasAnyRole("USER");
            request.requestMatchers("/trade-deal/open/type-2").access(tradeDealTwoAuthorizationManager);
            request.requestMatchers("/trade-deal/open/type-3").access(tradeDealThreeAuthorizationManager);
            request.requestMatchers("/trade-deal/open/details").access(acceptableOpenTradeDealAuthorizationManager);
            request.requestMatchers("/trade-deal/accept/confirmation").access(pendingTradeDealAuthorizationManager);
            request.requestMatchers("/trade-deal/create/type-1/post").hasAnyRole("USER");
            request.requestMatchers("/trade-deal/create/type-2/post").access(tradeDealTwoAuthorizationManager);
            request.requestMatchers("/trade-deal/create/type-3/post").access(tradeDealThreeAuthorizationManager);
            request.requestMatchers("/admin/register").hasAnyRole("ADMIN");
            request.requestMatchers("/admin/register/confirmation").hasAnyRole("ADMIN");
            request.requestMatchers("/admin/trade-deal/pending/details").hasAnyRole("ADMIN");
            request.requestMatchers("/admin/trade-deal/closed/confirmation").hasAnyRole("ADMIN");
            request.requestMatchers("/trade-deal/open/details/payment/accept").access(acceptableOpenTradeDealAuthorizationManager);
            request.requestMatchers("/trade-deal/open/details/credits/accept").access(acceptableOpenTradeDealAuthorizationManager);
            request.requestMatchers("/followers").hasAnyRole("USER");
            request.requestMatchers("/following").hasAnyRole("USER");
            request.requestMatchers("/account/payout/all").hasAnyRole("USER");
            request.requestMatchers("/account/payout").hasAnyRole("USER");
            request.requestMatchers("/profile/follow").hasAnyRole("USER");
            request.requestMatchers("/profile/unfollow").hasAnyRole("USER");
            request.requestMatchers("/request/denied").authenticated();
            request.requestMatchers("/profile").hasAnyRole("USER");
            request.requestMatchers("/profile/search").hasAnyRole("USER");
            request.requestMatchers("/account/payout/setup").hasAnyRole("USER");
            request.requestMatchers("/account/payout/setup/submit").hasAnyRole("USER");
            request.requestMatchers("/account/payout").hasAnyRole("USER");
            request.requestMatchers("/account/payout/all").hasAnyRole("USER");
            request.requestMatchers("/account/payout/confirmation").hasAnyRole("USER");
            request.requestMatchers("/password/forgot").permitAll();
            request.requestMatchers("/resale-deal/create/type-1").hasAnyRole("USER");
            request.requestMatchers("/resale-deal/create/type-2").access(tradeDealTwoAuthorizationManager);
            request.requestMatchers("/resale-deal/create/type-3").access(tradeDealThreeAuthorizationManager);
            request.requestMatchers("/resale-deal/accept/confirmation").access(pendingResaleDealAuthorizationManager);
            request.requestMatchers("/your-resale-deals/type-1/pending").hasAnyRole("USER");
            request.requestMatchers("/your-resale-deals/pending/details").access(pendingResaleDealAuthorizationManager);
            request.requestMatchers("/your-resale-deals").hasAnyRole("USER");
            request.requestMatchers("/resale-deal/create/post/confirmation").access(collectorOpenResaleDealAuthorizationManager);
            request.requestMatchers("/your-resale-deals/type-1/open").hasAnyRole("USER");
            request.requestMatchers("/your-resale-deals/open/details").access(collectorOpenResaleDealAuthorizationManager);
            request.requestMatchers("/resale-deal/open/type-1").hasAnyRole("USER");
            request.requestMatchers("/resale-deal/open/type-2").access(tradeDealTwoAuthorizationManager);
            request.requestMatchers("/resale-deal/open/type-3").access(tradeDealThreeAuthorizationManager);
            request.requestMatchers("/resale-deal/open/details").access(acceptableOpenResaleDealAuthorizationManager);
            request.requestMatchers("/admin/resale-deal/pending/details").hasAnyRole("ADMIN");
            request.requestMatchers("/admin/resale-deal/closed/confirmation").hasAnyRole("ADMIN");
            request.requestMatchers("/your-resale-deals/closed/details").access(closedResaleDealAuthorizationManager);
            request.requestMatchers("/resale-deal/open/details/credit/accept").access(acceptableOpenResaleDealAuthorizationManager) ;
            request.requestMatchers("your-resale-deals/type-1/closed").hasAnyRole("USER");
            request.requestMatchers("/your-resale-deals/type-2/closed").access(tradeDealTwoAuthorizationManager);
            request.requestMatchers("/your-resale-deals/type-3/closed").access(tradeDealThreeAuthorizationManager);
            request.requestMatchers("/your-resale-deals/type-2/open").access(tradeDealTwoAuthorizationManager);
            request.requestMatchers("/your-resale-deals/type-3/open").access(tradeDealThreeAuthorizationManager);
            request.requestMatchers("/your-resale-deals/type-2/pending").access(tradeDealTwoAuthorizationManager);
            request.requestMatchers("/your-resale-deals/type-3/pending").access(tradeDealThreeAuthorizationManager);
            request.requestMatchers("/resale-deal/open/details/payment/accept").access(acceptableOpenResaleDealAuthorizationManager);
            request.requestMatchers("/logout").hasAnyRole("USER", "ADMIN");
            request.requestMatchers("/error").permitAll();
        });
        
        http.formLogin(login -> login
                .loginPage("/login")
                .failureHandler(loginFormFailureHandler)
                .successHandler(loginFormSuccessHandler))
        .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login"))
        .oauth2Login(login -> login
            	.userInfoEndpoint(userInfo -> userInfo
            			.userService(oAuth2AuthenticationService)
            	)
            	.successHandler(loginOAuthSuccessHandler)
            	.loginPage("/login")
            	.failureUrl("/login?error")
        );
          
        return http.build();
    }
		
	@Autowired	
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
			.usersByUsernameQuery("select email, password, (disable_time is null or disable_time <= now()) as enabled from credential where email = ?")
			.authoritiesByUsernameQuery("select email, authority from user_authority where email = ?");
	}
	
	@Bean
	  SecurityContextRepository securityContextRepository() {
	    return new DelegatingSecurityContextRepository(
	        new RequestAttributeSecurityContextRepository(),
	        new HttpSessionSecurityContextRepository()
	    );
	  }
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
