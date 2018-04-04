package com.stewlutions.azuread_springsecurity5_oidc.kickstart;

import com.stewlutions.azuread_springsecurity5_oidc.kickstart.replacements.KickstartOAuth2UserService;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .authorizeRequests()
        .anyRequest().permitAll()
        .and()
      .oauth2Login()
        .userInfoEndpoint().userService(this.oauth2UserService())
      ;
  }

  private OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
		return new KickstartOAuth2UserService();
	}
}