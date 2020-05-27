package com.trelloiii.kurilka2.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.trelloiii.kurilka2.dto.EventType;
import com.trelloiii.kurilka2.dto.ObjectType;
import com.trelloiii.kurilka2.dto.Payload;
import com.trelloiii.kurilka2.model.Dialog;
import com.trelloiii.kurilka2.model.Message;
import com.trelloiii.kurilka2.model.User;
import com.trelloiii.kurilka2.repository.UserRepository;
import com.trelloiii.kurilka2.services.CustomUserDetailsService;
import com.trelloiii.kurilka2.services.DialogService;
import com.trelloiii.kurilka2.util.WsSender;
import com.trelloiii.kurilka2.views.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/messages")
@CrossOrigin
public class MessageController {
    private final CustomUserDetailsService userService;
    private final DialogService dialogService;
    private final BiConsumer<EventType, Payload<Message>> sender;

    @Autowired
    public MessageController(CustomUserDetailsService userService, DialogService dialogService, WsSender sender) {
        this.userService = userService;
        this.dialogService = dialogService;
        this.sender=sender.getSender(ObjectType.MESSAGE,View.MainInfo.class);
    }

    @GetMapping
    @JsonView(View.FullInfo.class)
    public List<Dialog> get(Principal principal){
        User user=userService.findByUsername(principal.getName());
        return dialogService.findAllByOwner(user);
    }

    @GetMapping
    @RequestMapping("{id}")
    public Dialog oneDialog(Principal principal,@PathVariable Long id){
        User user=userService.findByUsername(principal.getName());
        return dialogService.findOne(user,id);
    }
    @PostMapping
    public Message saveMessage(Principal principal,
                               @RequestParam String text,
                               @RequestParam Long id){
        User user=userService.findByUsername(principal.getName());
        Message message=dialogService.saveMessage(user,text,id);
        sender.accept(EventType.SEND,new Payload<>(message,message.getDialog().getCompanion().getId()));
        sender.accept(EventType.SEND,new Payload<>(message,message.getDialog().getOwner().getId()));
        return message;
    }
}
