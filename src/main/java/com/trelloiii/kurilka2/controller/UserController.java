package com.trelloiii.kurilka2.controller;

import com.trelloiii.kurilka2.model.User;
import com.trelloiii.kurilka2.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {
    @Autowired
    private CustomUserDetailsService userService;
    @GetMapping
    public User info(Principal principal){
        return userService.findByUsername(principal.getName());
    }
}
