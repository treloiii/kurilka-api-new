package com.trelloiii.kurilka2.model;

import com.fasterxml.jackson.annotation.*;
import com.trelloiii.kurilka2.views.View;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(View.MainInfo.class)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "dialog_id")
    @JsonIgnore
    private Dialog dialog;
    @JsonView(View.MainInfo.class)
    private String text;
    @JsonView(View.MainInfo.class)
    private LocalDateTime time;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    @JsonIdentityReference
    @JsonIdentityInfo(
            property = "id",
            generator = ObjectIdGenerators.PropertyGenerator.class
    )
    @JsonView(View.MainInfo.class)
    private User owner;
}
