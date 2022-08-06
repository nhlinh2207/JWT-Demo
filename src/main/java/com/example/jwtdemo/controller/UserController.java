package com.example.jwtdemo.controller;

import com.example.jwtdemo.dto.request.RegistryRequest;
import com.example.jwtdemo.dto.response.RegistryResponse;
import com.example.jwtdemo.dto.response.ResponseObject;
import com.example.jwtdemo.dto.response.ResponseStatus;
import com.example.jwtdemo.entity.Permission;
import com.example.jwtdemo.entity.Role;
import com.example.jwtdemo.entity.User;
import com.example.jwtdemo.model.CustomUserPrincipal;
import com.example.jwtdemo.repository.UserRepo;
import com.example.jwtdemo.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(path = "/user")
@Slf4j
@RequiredArgsConstructor
@Api(tags = "API")
public class UserController {

    private final IUserService userService;
    private final UserRepo userRepo;

    @ApiOperation(value = "Tạo tài khoản", response = ResponseEntity.class, authorizations = {@Authorization(value = "JWT")})
    @PostMapping(path = "/register")
    public ResponseEntity<?> register(@RequestBody RegistryRequest req) {
        log.info("Controller: Thêm mới tài khoản");
        ResponseObject<RegistryResponse> res = this.userService.register(req);
        return ResponseEntity.ok(res);
    }

    @ApiOperation(value = "Test role with JWT", response = ResponseObject.class, authorizations = {@Authorization(value = "JWT")})
    @GetMapping(path = "/test-role")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> testRole() {
        log.info("Controller: Test role");
        ResponseObject<String> res = new ResponseObject<>(true, ResponseStatus.DO_SERVICE_SUCCESSFUL);
        UsernamePasswordAuthenticationToken user
                = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepo.findByUsername(((CustomUserPrincipal) user.getPrincipal()).getUsername());
        String roleData = "";
        String permissionData = "";
        for (Role r : currentUser.getRoles()){
            roleData += r.getRoleName()+" ";
            for (Permission p : r.getPermissions()){
                permissionData += p.getActionCode()+" ";
            }
        }
        System.out.println();
        res.setData(currentUser.getUsername()+" has role : "+roleData+" --- hass permission:  "+permissionData);
        return ResponseEntity.ok(res);
    }

    @ApiOperation(value = "Test permission with JWT", response = ResponseObject.class, authorizations = {@Authorization(value = "JWT")})
    @GetMapping(path = "/test-permission")
    @PreAuthorize("hasAuthority('CREATE_USER')")
    public ResponseEntity<?> testPermission() {
        log.info("Controller: Test permission");
        ResponseObject<String> res = new ResponseObject<>(true, ResponseStatus.DO_SERVICE_SUCCESSFUL);
        UsernamePasswordAuthenticationToken user
                = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepo.findByUsername(((CustomUserPrincipal) user.getPrincipal()).getUsername());
        String roleData = "";
        String permissionData = "";
        for (Role r : currentUser.getRoles()){
            roleData += r.getRoleName()+" ";
            for (Permission p : r.getPermissions()){
                permissionData += p.getActionCode()+" ";
            }
        }
        System.out.println();
        res.setData(currentUser.getUsername()+" has permission : "+permissionData);
        return ResponseEntity.ok(res);
    }
}
