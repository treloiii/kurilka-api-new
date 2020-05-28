package com.trelloiii.kurilka2.repository;

import com.trelloiii.kurilka2.model.Dialog;
import com.trelloiii.kurilka2.model.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Long> {
    List<Message> findTop1ByDialogOrderByIdDesc(Dialog dialog);
    List<Message> findByDialog(Dialog dialog,Pageable pageable);
}
