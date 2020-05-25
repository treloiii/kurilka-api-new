package com.trelloiii.kurilka2.controller;

import com.trelloiii.kurilka2.model.User;
import com.trelloiii.kurilka2.repository.UserRepository;
import com.trelloiii.kurilka2.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/messages")
@CrossOrigin
public class MessageController {
    @Autowired
    CustomUserDetailsService userService;

    @GetMapping
    public List<String> get(Principal principal){
        User user=userService.findByUsername(principal.getName());
        return Stream.of("one","two","three").collect(Collectors.toList());
    }
}
