package com.trelloiii.kurilka2.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.trelloiii.kurilka2.model.User;
import com.trelloiii.kurilka2.services.SearchingService;
import com.trelloiii.kurilka2.views.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping("/search")
public class SearchingController {

    @Autowired
    private SearchingService searchingService;


    @GetMapping
    @JsonView(View.MainInfo.class)
    public List<User> byPrefix(@RequestParam(name = "query") String query){
        return searchingService.findByPrefix(query);
    }
    @GetMapping("/with")
    @JsonView(View.MainInfo.class)
    public Set<User> byDialogsWith(Principal principal){
        return searchingService.findByDialogsWith(principal);
    }
}
