package com.example.jwtdemo.service.impl;

import com.example.jwtdemo.dto.request.RegistryRequest;
import com.example.jwtdemo.dto.response.RegistryResponse;
import com.example.jwtdemo.dto.response.ResponseObject;
import com.example.jwtdemo.dto.response.ResponseStatus;
import com.example.jwtdemo.entity.Role;
import com.example.jwtdemo.entity.User;
import com.example.jwtdemo.exception.UnSuccessException;
import com.example.jwtdemo.repository.RoleRepo;
import com.example.jwtdemo.repository.UserRepo;
import com.example.jwtdemo.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    public ResponseObject<RegistryResponse> register(RegistryRequest req) {

        ResponseObject<RegistryResponse> res = new ResponseObject<>(true, ResponseStatus.DO_SERVICE_SUCCESSFUL);

        try {
            Set<Role> roles = new HashSet<>();
            Role role = roleRepo.findByRoleName(req.getRole());
            roles.add(role);

            User newUser = User.builder().username(req.getUsername())
                    .password(bCryptPasswordEncoder.encode(req.getPassword()))
                    .email(req.getEmail())
                    .isActive(false)
                    .roles(roles)
                    .createdAt(new Date())
                    .updatedAt(new Date())
                    .build();
            newUser = userRepo.save(newUser);

            RegistryResponse resp = RegistryResponse.builder()
                    .userId(newUser.getUserId())
                    .username(newUser.getUsername())
                    .email(newUser.getEmail())
                    .isActive(newUser.getIsActive())
                    .createdAt(newUser.getCreatedAt())
                    .updatedAt(newUser.getUpdatedAt())
                    .build();

            res.setData(resp);
            return res;
        }catch (Exception e){
            throw new UnSuccessException(e.getLocalizedMessage());
        }
    }
}
