package com.VaryTrade.Configuration;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.VaryTrade.Dao.User.AppUserRepository;
import com.VaryTrade.Model.UserCredential;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoginFormSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	@Autowired
	private AppUserRepository appUserRepository;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) 
			throws IOException, ServletException {
		String email = request.getParameter("username");
		UserCredential userCredential = appUserRepository.retrieveUserCredential(email);
		
		appUserRepository.updateUserAccountForSuccessfulLoginAttempt(userCredential);
		
		super.onAuthenticationSuccess(request, response, authentication);
	}
}
