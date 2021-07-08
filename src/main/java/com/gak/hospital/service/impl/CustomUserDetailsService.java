package com.gak.hospital.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.gak.hospital.entity.User;
import com.gak.hospital.repository.UserRepository;
import com.gak.hospital.utils.CacheUtils;
import com.gak.hospital.utils.RoleUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Collection;

@Component("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final @NonNull UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 查询用户
        User userByUsername = userRepository.findFirstUserByUsernameOrPhone(username, username);
        if (userByUsername == null) {
            throw new UsernameNotFoundException("login User:" + username + " was not found in db");
            //这里找不到必须抛异常
        }
        // 2. 设置角色(权限)
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for(String role : RoleUtils.getRoles(userByUsername.getRole())) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role));
        }
        // 缓存
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", userByUsername.getName());
        CacheUtils.single().set(username, jsonObject);
        // 3. 封装用户信息返回 参数分别是：用户名，密码，用户权限 new BCryptPasswordEncoder().encode()
        return new org.springframework.security.core.userdetails.User(username,
                userByUsername.getPassword(), grantedAuthorities);
    }
}