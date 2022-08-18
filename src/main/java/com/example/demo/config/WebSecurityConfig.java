package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


//Sino se habilita esto no funcionara
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user").password("{noop}user").roles("USER")
                .and()
                .withUser("admin").password("{noop}admin").roles("USER", "ADMIN");
    }



    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        //declares which Page(URL) will have What access type
        httpSecurity.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/swagger-ui/index.html").access("hasRole('USER')")
                .antMatchers("/swagger-ui/index.html").hasRole("ADMIN")
                //.antMatchers("/hello1").hasRole("USER")
                .and()
                // some more method calls
                .formLogin()
                .defaultSuccessUrl("/", true)
                .and()
                .exceptionHandling()
                .accessDeniedPage("/login")
        ;
        return httpSecurity.build();
    }
}
