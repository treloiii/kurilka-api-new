package com.trelloiii.kurilka2.model;
import com.fasterxml.jackson.annotation.*;
import com.trelloiii.kurilka2.views.View;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.jpa.QueryHints;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Dialog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(View.Id.class)
    private Long id;
//    @ManyToOne
//    @JoinColumn(name = "owner_id")
//    @JsonView(View.MainInfo.class)
//    private User owner;
//    @OneToMany(mappedBy = "dialog")
//    @JsonView(View.MainInfo.class)
//    private List<User> companion;
    @ManyToMany(mappedBy = "dialogs")
    @JsonView(View.MainInfo.class)
    private Set<User> users;


    @OneToMany(mappedBy = "dialog")
    @JsonView(View.MainInfo.class)
    private List<Message> messages;

    @JsonView(View.MainInfo.class)
    private String name;
}
