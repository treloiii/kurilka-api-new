package com.trelloiii.kurilka2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.trelloiii.kurilka2.views.View;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "usr")
@Data
@NoArgsConstructor
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
}
