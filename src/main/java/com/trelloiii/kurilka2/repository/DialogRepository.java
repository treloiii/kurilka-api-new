package com.trelloiii.kurilka2.repository;

import com.trelloiii.kurilka2.model.Dialog;
import com.trelloiii.kurilka2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DialogRepository extends JpaRepository<Dialog, Long> {
    @Query("select d from Dialog d where (d.companion=?1 or d.owner=?1)")
    List<Dialog> findByOwner(User owner);
    @Query("select d from Dialog d where (d.companion=?1 or d.owner=?1) and d.id=?2")
    Dialog findConcrete(User owner,Long id);
}
