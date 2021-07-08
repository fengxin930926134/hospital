package com.gak.hospital.controller;

import com.alibaba.fastjson.JSONObject;
import com.gak.hospital.entity.User;
import com.gak.hospital.service.impl.CustomUserDetailsService;
import com.gak.hospital.utils.CacheUtils;
import com.gak.hospital.utils.TokenUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final @NonNull CustomUserDetailsService userDetailsService;
    private final @NonNull AuthenticationManager authenticationManager;

    @PostMapping(value = "/auth")
    public HttpEntity<?> authorize(@NonNull @RequestBody User user) {
        try {
            // 1 创建UsernamePasswordAuthenticationToken
            UsernamePasswordAuthenticationToken token
                    = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            // 2 认证
            Authentication authentication = this.authenticationManager.authenticate(token);
            // 3 保存认证信息
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // 4 加载UserDetails
            UserDetails details = this.userDetailsService.loadUserByUsername(user.getUsername());
            // 5 生成自定义token
            JSONObject tokenJson = TokenUtils.createToken(details);
            tokenJson.putAll(CacheUtils.single().get(details.getUsername()));
            return ResponseEntity.ok(tokenJson);
        } catch (AuthenticationException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.ok(false);
        }
    }
}
