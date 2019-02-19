package com.rest.configuration;

import com.rest.repository.UserRepository;
import com.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableMongoRepositories(basePackageClasses = UserRepository.class)
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

 @Autowired
 private UserService userService;

 @Override
 protected void configure(HttpSecurity http) throws Exception {
  http.cors().and().csrf().disable();

  http.authorizeRequests()
      .antMatchers("/login*").permitAll()
      .anyRequest().authenticated()
      .and()
      .formLogin().permitAll();
      //.loginProcessingUrl("/login")
      //.defaultSuccessUrl("/browser",true).successForwardUrl("/browser").failureForwardUrl("/login")
      //.permitAll();

     /*http.formLogin()
             .loginProcessingUrl("/login")
             .defaultSuccessUrl("/browser/index.html",true)
             .failureUrl("/login?error=true")
     .and().authenticationProvider(authenticationProvider()).userDetailsService(userService).httpBasic();*/
 }

 @Override
 protected void configure(AuthenticationManagerBuilder auth) throws Exception {
  auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
  auth.inMemoryAuthentication()
             .withUser("admin").password(passwordEncoder().encode("rdt_user"))
             .authorities("Admin");
 }

/* @Autowired
 protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
  auth.inMemoryAuthentication()
          .withUser("user").password(passwordEncoder().encode("user"))
          .authorities("ROLE_USER");

 }*/

 @Bean
 public PasswordEncoder passwordEncoder() {
  return new PasswordEncoder() {
      @Override
      public String encode(CharSequence rawPassword) {
          return rawPassword.toString();
      }

      @Override
      public boolean matches(CharSequence rawPassword, String encodedPassword) {
          return true;
      }
  };
 }

}
