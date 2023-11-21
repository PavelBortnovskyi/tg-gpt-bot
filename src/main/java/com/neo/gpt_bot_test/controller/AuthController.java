package com.neo.gpt_bot_test.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.neo.gpt_bot_test.annotations.Marker;
import com.neo.gpt_bot_test.containers.db.dto.request.AdminUserRequestDTO;
import com.neo.gpt_bot_test.containers.db.facade.AuthFacade;
import com.neo.gpt_bot_test.containers.db.service.AdminUserService;
import com.neo.gpt_bot_test.containers.db.service.JwtTokenService;
import com.neo.gpt_bot_test.exceptions.authError.AuthErrorException;
import com.neo.gpt_bot_test.exceptions.authError.UserAlreadyRegisteredException;
import com.neo.gpt_bot_test.model.AdminUser;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

@Log4j2
@Validated
@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(originPatterns = {"*"})
@RequiredArgsConstructor
public class AuthController {

   private final AuthFacade authFacade;

    /**
     * This endpoint waiting for valid sighUpDTO to register new account and return new token pair(Access and Refresh)
     */
    @ApiOperation("Sign up admin by form")
    @Validated({Marker.New.class})
    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, String>> handleRegistration(@RequestBody @JsonView({Marker.New.class}) @Valid AdminUserRequestDTO signUpDTO) {
       return authFacade.makeSighUp(signUpDTO);
    }

    /**
     * This endpoint waiting for valid loginDTO to check credentials and return new token pair(Access and Refresh)
     */
    @ApiOperation("Login admin")
    @Validated({Marker.Existed.class})
    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, String>> handleLogin(@RequestBody @JsonView({Marker.Existed.class}) @Valid AdminUserRequestDTO loginDTO) {
        return authFacade.makeLogin(loginDTO);
    }

    /**
     * This endpoint waiting for valid token in request to perform refresh token invalidation
     */
    @ApiOperation("Logout user")
    @GetMapping(path = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> handleLogout(HttpServletRequest request) {
        return ResponseEntity.ok(authFacade.makeLogOut((Long) request.getAttribute("userId")));
    }

    /**
     * This endpoint waiting for valid refresh token in request to return new token pair for refresh owner
     */
    @ApiOperation("Get new token pair by refresh token")
    @GetMapping(path = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, String>> handleRefresh(HttpServletRequest request) {
        return authFacade.makeRefresh(request);
    }
}

