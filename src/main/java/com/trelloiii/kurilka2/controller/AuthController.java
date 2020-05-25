package com.trelloiii.kurilka2.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.trelloiii.kurilka2.jwt.JwtResponse;
import com.trelloiii.kurilka2.jwt.JwtUtils;
import com.trelloiii.kurilka2.repository.UserRepository;
import com.trelloiii.kurilka2.services.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private RegistrationService registrationService;
    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    InMemoryTokenRepositoryImpl tokenRepository;
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestParam(name = "username") String username,
                                              @RequestParam(name="password") String password,
                                              HttpServletResponse response) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        User userDetails = (User) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new JwtResponse(
                userDetails.getUsername(),
                jwt,
                roles));
    }
    @PostMapping("/register")
    public com.trelloiii.kurilka2.model.User registerFromGoogle(@RequestBody String code) throws IOException {
        return registrationService.provideGoogle(code);
    }

    @PostMapping("/set_password/{id}")
    public ResponseEntity<?> setPassword(@PathVariable String id,
                                         @RequestBody String password,
                                         HttpServletResponse httpServletResponse){
        com.trelloiii.kurilka2.model.User user=registrationService.setUpPassword(id,password);
        return authenticateUser(user.getEmail(), password,httpServletResponse);
    }
}
