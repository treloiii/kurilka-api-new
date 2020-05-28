package com.trelloiii.kurilka2.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.trelloiii.kurilka2.dto.EventType;
import com.trelloiii.kurilka2.dto.ObjectType;
import com.trelloiii.kurilka2.dto.Payload;
import com.trelloiii.kurilka2.model.Dialog;
import com.trelloiii.kurilka2.model.Message;
import com.trelloiii.kurilka2.model.User;
import com.trelloiii.kurilka2.services.CustomUserDetailsService;
import com.trelloiii.kurilka2.services.DialogService;
import com.trelloiii.kurilka2.util.WsSender;
import com.trelloiii.kurilka2.views.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import java.util.function.BiConsumer;

@RestController
@RequestMapping("/messages")
@CrossOrigin
public class MessageController {
    private final CustomUserDetailsService userService;
    private final DialogService dialogService;
    private final BiConsumer<EventType, Payload<Message>> sender;
    private final int MESSAGE_PER_PAGE=30;

    @Autowired
    public MessageController(CustomUserDetailsService userService, DialogService dialogService, WsSender sender) {
        this.userService = userService;
        this.dialogService = dialogService;
        this.sender=sender.getSender(ObjectType.MESSAGE,View.MainInfo.class);
    }

    @GetMapping
    @JsonView(View.FullInfo.class)
    public List<Dialog> get(Principal principal){
        User user=principal(principal);
        return dialogService.findAllByOwner(user);
    }

    @GetMapping
    @RequestMapping("{id}")
    public Dialog oneDialog(Principal principal,
                            @PathVariable Long id,
                            @PageableDefault(
                                    size = MESSAGE_PER_PAGE, sort = {"id"},
                                    direction = Sort.Direction.DESC
                            ) Pageable pageable){
        User user=principal(principal);
        return dialogService.findOne(user,id,pageable);
    }
    @PostMapping
    public Message saveMessage(Principal principal,
                               @RequestParam String text,
                               @RequestParam Long id){
        User user=principal(principal);
        Message message=dialogService.saveMessage(user,text,id);
        sendWs(EventType.SEND,message);
        return message;
    }
    @PostMapping("/with")
    public Long findWith(@RequestBody User user,Principal principal){
        User caller=principal(principal);
        Long id= dialogService.findDialogWith(user,caller);
        return id==null?-1:id;
    }
    @PostMapping("/dialog")
    public ResponseEntity<?> newDialog(@RequestParam String opponentId,
                                    @RequestParam String message,
                                    Principal principal){
        User user=principal(principal);
        Message messageNew=dialogService.newDialog(user,opponentId,message);
        sendWs(EventType.DIALOG,messageNew);
        return ResponseEntity.ok(messageNew.getDialog().getId());
    }

    private User principal(Principal principal){
        return userService.findByUsername(principal.getName());
    }
    private void sendWs(EventType eventType,Message message){
        Dialog d=message.getDialog();
        if(!d.getCompanion().equals(d.getOwner())){
            sender.accept(eventType,new Payload<>(message,message.getDialog().getCompanion().getId()));
        }
        sender.accept(eventType,new Payload<>(message,message.getDialog().getOwner().getId()));
    }
}
