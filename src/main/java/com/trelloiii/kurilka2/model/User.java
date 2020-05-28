package com.trelloiii.kurilka2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.trelloiii.kurilka2.views.View;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "usr")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class User {
    @Id
    @JsonView(View.MainInfo.class)
    private String id;
    @JsonView(View.MainInfo.class)
    private String name;
    @JsonIgnore
    private String password;
    @JsonView(View.MainInfo.class)
    private String avatar;
    @JsonView(View.FullInfo.class)
    private String email;
    @JsonView(View.FullInfo.class)
    private String locale;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_dialogs",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "dialog_id")}
    )
    @JsonIgnore
    Set<Dialog> dialogs;
}
