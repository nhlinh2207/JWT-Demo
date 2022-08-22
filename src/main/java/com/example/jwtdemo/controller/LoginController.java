package com.example.jwtdemo.controller;

import com.example.jwtdemo.dto.request.JwtRequest;
import com.example.jwtdemo.dto.response.JwtResponse;
import com.example.jwtdemo.dto.response.ResponseObject;
import com.example.jwtdemo.dto.response.ResponseStatus;
import com.example.jwtdemo.entity.User;
import com.example.jwtdemo.exception.JwtBadCredentialException;
import com.example.jwtdemo.exception.JwtDisabledException;
import com.example.jwtdemo.exception.UnSuccessException;
import com.example.jwtdemo.jwt.jwtConfig.JwtTokenUtils;
import com.example.jwtdemo.model.CustomUserPrincipal;
import com.example.jwtdemo.repository.UserRepo;
import com.example.jwtdemo.service.impl.JwtUserDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@CrossOrigin
@Slf4j
@Api(tags = "API")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtils jwtTokenUtil;
    private final JwtUserDetailsService jwtUserDetailsService;
    private final UserRepo userRepo;

    public LoginController(AuthenticationManager authenticationManager, JwtTokenUtils jwtTokenUtil
            , JwtUserDetailsService jwtUserDetailsService, UserRepo userRepo) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.userRepo = userRepo;
    }

    @ApiOperation(value = "Đăng nhập", response = JwtResponse.class)
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody JwtRequest authenticationRequest) throws DisabledException {
        log.info("Controller: Tạo token xác thực {}", authenticationRequest.toString());
        try {
            final User user = this.userRepo.findByEmail(authenticationRequest.getEmail());
            if (user == null) {
                log.error("user not found");
                throw new UnSuccessException("User not found : "+authenticationRequest.getEmail());
            }
            this.authenticate(user.getUsername(), authenticationRequest.getPassword());
            final CustomUserPrincipal userDetails = jwtUserDetailsService.loadUserByUsername(user.getUsername().toString());
            final String access_token = jwtTokenUtil.generateToken(userDetails);
            ArrayList<GrantedAuthority> role = new ArrayList<>(userDetails.getAuthorities());
           // role.removeIf(s -> !s.getAuthority().contains("ROLE_"));
            ResponseObject<?> res = null;
            res = subscriberNull(user, userDetails, access_token, role);
            return ResponseEntity.ok(res);
        } catch (DisabledException e) {
            throw new JwtDisabledException(e.getLocalizedMessage());
        } catch (BadCredentialsException e) {
            throw new JwtBadCredentialException(e.getLocalizedMessage());
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseObject<>(false, ResponseStatus.UNAUTHORIZE, e.getLocalizedMessage()), HttpStatus.UNAUTHORIZED);
        }
    }

    private void authenticate(String username, String password) {
        log.info("Kiểm tra xác thực {}", username);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    private ResponseObject<?> subscriberNull(User user, CustomUserPrincipal userDetails, String access_token, ArrayList<GrantedAuthority> role) {
        ResponseObject<NullJwt> res = new ResponseObject<>(true, ResponseStatus.DO_SERVICE_SUCCESSFUL);
        log.info("abc login");
        NullJwt nullJwt = NullJwt.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .token(access_token)
                .avatar("avatar-s-11.jpg")
                .role(this.roleToArrayList(role))
                .build();
        log.info(nullJwt.token);
        res.setData(nullJwt);
        return res;
    }

    private ArrayList<String> roleToArrayList(ArrayList<GrantedAuthority> role){
        ArrayList<String> listRole = new ArrayList<>();
        role.forEach(grantedAuthority -> {
            listRole.add(grantedAuthority.getAuthority());
        });
        return listRole;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    static public class NullJwt {
        private String username;
        private String email;
        private String token;
        private String avatar;
        private ArrayList <String> role;
    }
}
