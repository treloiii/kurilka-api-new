package com.trelloiii.kurilka2.services;

import com.trelloiii.kurilka2.model.Dialog;
import com.trelloiii.kurilka2.model.Message;
import com.trelloiii.kurilka2.model.User;
import com.trelloiii.kurilka2.repository.DialogRepository;
import com.trelloiii.kurilka2.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DialogService {
    @Autowired
    private DialogRepository dialogRepository;
    @Autowired
    private MessageRepository messageRepository;

    public List<Dialog> findAllByOwner(User owner){
        return dialogRepository.findByOwner(owner);
    }

    public Dialog findOne(User owner,Long id){
        return dialogRepository.findConcrete(owner, id);
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
}
