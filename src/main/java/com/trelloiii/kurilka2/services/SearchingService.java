package com.trelloiii.kurilka2.services;

import com.trelloiii.kurilka2.model.User;
import com.trelloiii.kurilka2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SearchingService {
    @Autowired
    private UserRepository userRepository;

    public List<User> findByPrefix(String prefix){
        return userRepository.findBySearchQuery(prefix);
    }

    public Set<User> findByDialogsWith(Principal principal) {
        User current=userRepository.findByEmail(principal.getName());
        Set<User> result=new HashSet<>();
        current.getDialogs().forEach(d-> result.addAll(d.getUsers()));
        result.removeIf(user -> user.getId().equals(current.getId()));
        return result;
    }
}
