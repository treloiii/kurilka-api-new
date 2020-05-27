package com.trelloiii.kurilka2.repository;

import com.trelloiii.kurilka2.model.Dialog;
import com.trelloiii.kurilka2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.List;

public interface DialogRepository extends JpaRepository<Dialog, Long> {
    @Query("select new com.trelloiii.kurilka2.model.Dialog(d.id,d.owner,d.companion) from Dialog d where (d.companion=?1 or d.owner=?1)")
//    @Query(value="select * from dialog as d join message as m on m.id = (select m1.id from message as m1 where m1.dialog_id=d.id order by m1.id desc limit 1) where (d.owner_id=?1 or companion_id=?1);",nativeQuery=true)
    List<Dialog> findByOwner(User owner);
    @Query("select d from Dialog d where (d.companion=?1 or d.owner=?1) and d.id=?2")
    Dialog findConcrete(User owner,Long id);
}
