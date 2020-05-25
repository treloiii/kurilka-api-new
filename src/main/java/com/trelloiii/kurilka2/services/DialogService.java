package com.trelloiii.kurilka2.services;

import com.trelloiii.kurilka2.model.Dialog;
import com.trelloiii.kurilka2.model.User;
import com.trelloiii.kurilka2.repository.DialogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DialogService {
    @Autowired
    private DialogRepository dialogRepository;

    public List<Dialog> findAllByOwner(User owner){
        return dialogRepository.findByOwner(owner);
    }
}
