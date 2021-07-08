package com.gak.hospital.config;

import com.gak.hospital.service.impl.CustomUserDetailsService;
import com.gak.hospital.utils.RoleUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final @NonNull CustomUserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/user/**").hasRole(RoleUtils.USER)
                .antMatchers("/admin/**").hasRole(RoleUtils.ADMIN)
                .antMatchers("/doctor/**").hasRole(RoleUtils.DOCTOR)
                .antMatchers("/pharmacist/drugAll").hasRole(RoleUtils.DOCTOR)
                .antMatchers("/technical/**").hasRole(RoleUtils.TECHNICAL)
                .antMatchers("/pharmacist/**").hasRole(RoleUtils.PHARMACIST)
                // permitAll 允许所有人访问
                .antMatchers("/login/auth").permitAll()
                .anyRequest().authenticated()
                // 前后端分离不需要配置 form login 和 basic login
                //不创建不使用session
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 关闭 csrf 防护，放行 post 请求
                .csrf()
                .disable()
                // 这里增加securityConfigurerAdapter
                .apply(securityConfigurerAdapter());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService) // 设置自定义的userDetailsService
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    private AuthTokenConfigurer securityConfigurerAdapter() {
        return new AuthTokenConfigurer(userDetailsService);
    }
}