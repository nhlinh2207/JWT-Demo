package com.example.jwtdemo.service.impl;

import com.example.jwtdemo.entity.User;
import com.example.jwtdemo.model.CustomUserPrincipal;
import com.example.jwtdemo.repository.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class JwtUserDetailsService extends User implements UserDetailsService {
    private final UserRepo userRepo;

    public JwtUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }
    @Override
    @Transactional(readOnly = true)
    public CustomUserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepo.findByUsername(username);
        if (user != null) {
            log.info("loadUserByUsername() ", user.getUsername());
            return new CustomUserPrincipal(user, user.getPassword(), user.getIsActive(), true, true, true, this.getAuthority(user));
        } else {
            throw new UsernameNotFoundException("user not found with username = " + user.getUsername());
        }
    }

    private List<GrantedAuthority> getAuthority(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
//       authorities.add(new SimpleGrantedAuthority(Constant.ROLE_PREFIX + user.getRole()));
        log.info("add authorities to UserDetail for username {}", user.getUsername());
//        List<Role> roles =  new ArrayList<>();
//        authorities.add(new SimpleGrantedAuthority(Constant.ROLE_PREFIX + "SUPER_ADMIN"));

        //User có nhiều role
        user.getRoles().forEach(role -> {
            log.info("add role {}", role.getRoleName());
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
            //Role co nhieu permission
            role.getPermissions().forEach(permission -> {
                authorities.add(new SimpleGrantedAuthority(permission.getActionCode()));
            });
        });

        //Remove duplicates in arraylist
        Set<GrantedAuthority> set = new HashSet<>(authorities);
        authorities.clear();
        authorities.addAll(set);
        log.info("Have {} role for user", authorities.size());
        return authorities;
    }
}
