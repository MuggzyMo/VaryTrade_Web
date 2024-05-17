package com.VaryTrade.Configuration;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.VaryTrade.Dao.User.AppUserRepository;
import com.VaryTrade.Model.Email;
import com.VaryTrade.Model.UserCredential;
import com.VaryTrade.Service.Email.AppEmailService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoginFormFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	@Autowired
	private AppUserRepository appUserRepository;
	@Autowired
	private AppEmailService appEmailService;
	
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		String email = request.getParameter("username");
		UserCredential userCredential = appUserRepository.retrieveUserCredential(email);
		
		boolean isUserAccountLocked = appUserRepository.updateUserAccountForFailedLoginAttempt(userCredential);
		
		if(isUserAccountLocked) {
			Email disableAccountEmail = appEmailService.createDisableAccountEmail(userCredential.getEmail());
			appEmailService.sendEmail(disableAccountEmail);
		}
		
		super.setDefaultFailureUrl("/login?error");
        super.onAuthenticationFailure(request, response, exception);
	}
}
