package com.trelloiii.kurilka2.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.trelloiii.kurilka2.model.Dialog;
import com.trelloiii.kurilka2.model.User;
import com.trelloiii.kurilka2.repository.UserRepository;
import com.trelloiii.kurilka2.services.CustomUserDetailsService;
import com.trelloiii.kurilka2.services.DialogService;
import com.trelloiii.kurilka2.views.View;
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
    private CustomUserDetailsService userService;
    @Autowired
    private DialogService dialogService;
    @GetMapping
    @JsonView(View.MainInfo.class)
    public List<Dialog> get(Principal principal){
        User user=userService.findByUsername(principal.getName());
        List<Dialog> dialogs= dialogService.findAllByOwner(user);
        return dialogs;
    }
}
