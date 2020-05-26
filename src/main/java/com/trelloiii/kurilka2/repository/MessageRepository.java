package com.trelloiii.kurilka2.repository;

import com.trelloiii.kurilka2.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message,Long> {
}
