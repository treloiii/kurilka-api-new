package com.trelloiii.kurilka2.services;

import com.trelloiii.kurilka2.exceptions.DialogCreationFailedException;
import com.trelloiii.kurilka2.model.Dialog;
import com.trelloiii.kurilka2.model.Message;
import com.trelloiii.kurilka2.model.User;
import com.trelloiii.kurilka2.repository.DialogRepository;
import com.trelloiii.kurilka2.repository.MessageRepository;
import com.trelloiii.kurilka2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DialogService {
    @Autowired
    private DialogRepository dialogRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Dialog> findAllByOwner(User owner) {
        owner = userRepository.findById(owner.getId()).orElseThrow(() -> new RuntimeException("user not found"));
        List<Dialog> dialogs = new ArrayList<>(owner.getDialogs());
        for (Dialog dialog : dialogs) {
            dialog.setMessages(messageRepository.findTop1ByDialogOrderByIdDesc(dialog));
        }
        dialogs.sort((d1, d2) -> {
            try {
                Message m1 = d1.getMessages().get(0);
                Message m2 = d2.getMessages().get(0);
                return m2.getTime().compareTo(m1.getTime());
            } catch (Exception e) {
                return 1;
            }
        });
        return dialogs;
    }

    public Dialog findOne(User owner, Long id, Pageable pageable) {
        owner = userRepository.findById(owner.getId()).orElseThrow(() -> new RuntimeException("user not found"));
        Dialog dialog = owner.getDialogs().stream()
                .filter(dialog1 -> dialog1.getId().equals(id))
                .findAny()
                .orElseThrow(() -> new RuntimeException("dialog not found exception"));
        List<Message> messages = messageRepository.findByDialog(dialog, pageable);
        messages.sort(Comparator.comparing(Message::getTime));
        dialog.setMessages(messages);
        return dialog;
    }

    public Message saveMessage(User user, String text, Long id) {
        Message message = new Message();
        message.setDialog(
                dialogRepository
                        .findById(id)
                        .orElseThrow(() -> new RuntimeException("Dialog not found"))
        );
        message.setOwner(user);
        message.setText(text);
        message.setTime(LocalDateTime.now());
        return messageRepository.save(message);
    }

    public Long findDialogWith(User user, User caller) {
        caller = userRepository.findById(caller.getId()).orElseThrow(() -> new RuntimeException("user not found"));
        return caller.getDialogs().stream()
                .filter(dialog -> dialog.getUsers().contains(user))
                .sorted(Comparator.comparing(Dialog::getId))
                .mapToLong(Dialog::getId)
                .findFirst()
                .orElse(-1);
    }

    public Message newDialog(User user, String opponentId, String message) {
        User opponent = userRepository.findById(opponentId).orElseThrow(() -> new RuntimeException("Opponent not found"));
        Message messageNew = messageBuilder(message,user);
        Long dialogId = findDialogWith(opponent, user);
        if (dialogId == -1) {
            Set<User> pair = Stream.of(user, opponent)
                    .collect(Collectors.toSet());
            return dialogBuilder(pair,messageNew,null);
        }
        messageNew.setDialog(dialogRepository.findById(dialogId).orElseThrow(() -> new RuntimeException("Dialog not found")));
        return messageRepository.save(messageNew);
    }
    public Message newGroupChat(User owner,Set<String> usersId,String message,String groupName){
        Set<User> users= new HashSet<>(userRepository.findAllById(usersId));
        boolean canCreate=users.stream().noneMatch(user -> findDialogWith(owner,user)==-1);
        if(canCreate){
            Message messageNew=messageBuilder(message,owner);
            return dialogBuilder(users,messageNew,groupName);
        }
        throw new DialogCreationFailedException();
    }

    private Message dialogBuilder(Set<User> users,Message message,String dialogName){
        Dialog dialog = new Dialog();
        dialog.setUsers(users);
        dialog.setName(dialogName);
        Dialog saved = dialogRepository.save(dialog);
        message.setDialog(saved);
        users.forEach(user -> {
            user.getDialogs().add(dialog);
            userRepository.save(user);
        });
        return messageRepository.save(message);
    }

    private Message messageBuilder(String message,User user){
        Message messageNew = new Message();
        messageNew.setTime(LocalDateTime.now());
        messageNew.setOwner(user);
        messageNew.setText(message);
        return messageNew;
    }
}
