package com.trelloiii.kurilka2.repository;

import com.trelloiii.kurilka2.model.Dialog;
import com.trelloiii.kurilka2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DialogRepository extends JpaRepository<Dialog, Long> {
    List<Dialog> findByOwner(User owner);
}
