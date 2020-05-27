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
