package com.trelloiii.kurilka2.services;

import com.trelloiii.kurilka2.model.User;
import com.trelloiii.kurilka2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchingService {
    @Autowired
    private UserRepository userRepository;

    public List<User> findByPrefix(String prefix){
        return userRepository.findBySearchQuery(prefix);
    }
}
