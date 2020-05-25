package com.trelloiii.kurilka2.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.trelloiii.kurilka2.model.User;
import com.trelloiii.kurilka2.repository.UserRepository;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class RegistrationService {
    @Value("${spring.security.oauth2.client.registration.google.clientId}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.google.clientSecret}")
    private String clientSecret;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public User provideGoogle(String code) throws IOException {
        GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                new NetHttpTransport(),
                JacksonFactory.getDefaultInstance(),
                "https://oauth2.googleapis.com/token",
                clientId,
                clientSecret,
                code,
                "postmessage")
                .execute();
        String accessToken = tokenResponse.getAccessToken();
        GoogleIdToken idToken = tokenResponse.parseIdToken();
        GoogleIdToken.Payload payload = idToken.getPayload();
        return extractAndSaveUser(payload);
    }


    public User extractAndSaveUser(GoogleIdToken.Payload payload){
        User user=new User();
        user.setAvatar((String) payload.get("picture"));
        user.setEmail(payload.getEmail());
        user.setName((String) payload.get("name"));
        user.setLocale((String) payload.get("locale"));
        user.setId((String) payload.get("sub"));
        return userRepository.save(user);
    }

    public User setUpPassword(String id, String password) {
        User user=userRepository.findById(id).orElseThrow(()->new RuntimeException("User not found"));
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }
}
