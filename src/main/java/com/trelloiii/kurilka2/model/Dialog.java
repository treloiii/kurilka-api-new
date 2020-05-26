package com.trelloiii.kurilka2.model;
import com.fasterxml.jackson.annotation.JsonView;
import com.trelloiii.kurilka2.views.View;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Dialog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(View.MainInfo.class)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    @JsonView(View.MainInfo.class)
    private User owner;
    @ManyToOne
    @JoinColumn(name = "companion_id")
    @JsonView(View.MainInfo.class)
    private User companion;
    @OneToMany(mappedBy = "dialog")
    @JsonView(View.FullInfo.class)
    private List<Message> messages;
}
