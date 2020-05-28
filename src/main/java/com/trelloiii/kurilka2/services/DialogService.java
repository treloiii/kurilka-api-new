package com.trelloiii.kurilka2.services;

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
import java.util.List;

@Service
public class DialogService {
    @Autowired
    private DialogRepository dialogRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Dialog> findAllByOwner(User owner){
        List<Dialog> dialogs=dialogRepository.findByOwner(owner);
        for (Dialog dialog : dialogs) {
            dialog.setMessages(messageRepository.findTop1ByDialogOrderByIdDesc(dialog));
        }
        dialogs.sort((d1,d2)->{
            try {
                Message m1 = d1.getMessages().get(0);
                Message m2 = d2.getMessages().get(0);
                return m2.getTime().compareTo(m1.getTime());
            }
            catch (Exception e){
                return 1;
            }
        });
        return dialogs;
    }

    public Dialog findOne(User owner, Long id, Pageable pageable){
        Dialog dialog= dialogRepository.findConcrete(owner, id);
        List<Message> messages=messageRepository.findByDialog(dialog,pageable);
        messages.sort((m1,m2)->m1.getTime().compareTo(m2.getTime()));
        dialog.setMessages(messages);
        return dialog;
    }

    public Message saveMessage(User user, String text, Long id) {
        Message message=new Message();
        message.setDialog(
                dialogRepository
                        .findById(id)
                        .orElseThrow(()->new RuntimeException("Dialog not found"))
        );
        message.setOwner(user);
        message.setText(text);
        message.setTime(LocalDateTime.now());
        return messageRepository.save(message);
    }
    public Long findDialogWith(User user,User caller){
        return dialogRepository.findByOwnerOrCompanion(user,caller);
    }

    public Message newDialog(User user, String opponentId, String message) {
        User opponent=userRepository.findById(opponentId).orElseThrow(()->new RuntimeException("Opponent not found"));
        Message messageNew=new Message();
        messageNew.setTime(LocalDateTime.now());
        messageNew.setOwner(user);
        messageNew.setText(message);
        Long dialogId=findDialogWith(opponent,user);
        if(dialogId==null){
            Dialog dialog=new Dialog();
            dialog.setOwner(user);
            dialog.setCompanion(opponent);
            Dialog saved=dialogRepository.save(dialog);
            messageNew.setDialog(saved);
            return messageRepository.save(messageNew);
        }
        messageNew.setDialog(dialogRepository.findById(dialogId).orElseThrow(()->new RuntimeException("Dialog not found")));
        return messageRepository.save(messageNew);
    }
}
