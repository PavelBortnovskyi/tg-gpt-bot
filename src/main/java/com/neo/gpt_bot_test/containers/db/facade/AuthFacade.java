package com.neo.gpt_bot_test.containers.db.facade;

import com.neo.gpt_bot_test.containers.db.dto.request.AdminUserRequestDTO;
import com.neo.gpt_bot_test.containers.db.dto.response.AdminUserResponseDTO;
import com.neo.gpt_bot_test.containers.db.service.AdminUserService;
import com.neo.gpt_bot_test.containers.db.service.JwtTokenService;
import com.neo.gpt_bot_test.containers.db.service.ServiceInterface;
import com.neo.gpt_bot_test.enums.TokenType;
import com.neo.gpt_bot_test.exceptions.authError.AuthErrorException;
import com.neo.gpt_bot_test.exceptions.authError.JwtAuthenticationException;
import com.neo.gpt_bot_test.exceptions.authError.UserAlreadyRegisteredException;
import com.neo.gpt_bot_test.model.AdminUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

@Log4j2
@Component
public class AuthFacade extends GeneralFacade<AdminUser, AdminUserRequestDTO, AdminUserResponseDTO>{

  private final JwtTokenService jwtTokenService;

  private final AuthenticationManager authenticationManager;

  private final AdminUserService adminUserService;

  private final PasswordEncoder encoder;

  public AuthFacade(ModelMapper mm, ServiceInterface<AdminUser> service, JwtTokenService jwtTokenService,
                    AuthenticationManager authenticationManager, AdminUserService adminUserService,
                    PasswordEncoder passwordEncoder) {
    super(mm, service);
    this.jwtTokenService = jwtTokenService;
    this.authenticationManager = authenticationManager;
    this.adminUserService = adminUserService;
    this.encoder = passwordEncoder;
  }

  /**
   * Method performs user login operation based on provided in DTO credentials and returns new token pair
   */
  public ResponseEntity<HashMap<String, String>> makeLogin(AdminUserRequestDTO loginDTO) {
    //Auth procedure handling
    Authentication authentication = authenticationManager
      .authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    Object principal = authentication.getPrincipal();

    Optional<User> maybeAuthUser = (principal instanceof User) ? Optional.of((User) principal) : Optional.empty();
    User authUser = maybeAuthUser.orElseThrow(() -> new AuthErrorException("Something went wrong during authentication"));

    //User extraction from DB by security credentials from Authenticated User (email aka username)
    Optional<AdminUser> maybeAdmin = adminUserService.getAdmin(authUser.getUsername());
    AdminUser currentAdmin = maybeAdmin.orElseThrow(() -> new AuthErrorException("Authenticated user not found in DB! MAGIC!"));

    return ResponseEntity.ok(jwtTokenService.generateTokenPair(currentAdmin));
  }

  /**
   * Method performs user sighUp operation based on provided in DTO credentials and returns new token pair
   */
  public ResponseEntity<HashMap<String, String>> makeSighUp(AdminUserRequestDTO signUpDTO) {
    //Email duplicate checking
    if (adminUserService.isEmailPresentInDB(signUpDTO.getEmail()))
      throw new UserAlreadyRegisteredException("email: " + signUpDTO.getEmail());

    //Saving new Admin to DB and getting user_id
    signUpDTO.setPassword(encoder.encode(signUpDTO.getPassword()));
    AdminUser freshAdmin = this.convertToEntity(signUpDTO);
    freshAdmin.setCreatedAt(LocalDateTime.now());
    freshAdmin.setCreatedBy(signUpDTO.getEmail());
    freshAdmin = adminUserService.save(freshAdmin);

    return ResponseEntity.ok(jwtTokenService.generateTokenPair(freshAdmin));
  }

  /**
   * Method performs user logout by refresh token invalidation
   */
  public String makeLogOut(Long userId) {
    jwtTokenService.changeRefreshTokenStatus(userId, true);
    log.info("Admin id: " + userId + " logged out");
    return "Admin with Id: " + userId + " logged out";
  }


  public ResponseEntity<HashMap<String, String>> makeRefresh(HttpServletRequest request) {
    String token = jwtTokenService.extractTokenFromRequest(request).orElseThrow(() -> new JwtAuthenticationException("Token not found!"));
    if (jwtTokenService.validateToken(token, TokenType.REFRESH) && !jwtTokenService.checkRefreshTokenStatus(token)) {
      AdminUser currAdmin = adminUserService.getAdminByRefreshToken(token).get();
      return ResponseEntity.ok(jwtTokenService.generateTokenPair(currAdmin));
    } else return ResponseEntity.status(400).body(new HashMap<>());
  }
}

