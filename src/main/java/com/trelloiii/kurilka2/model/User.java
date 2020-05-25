package com.trelloiii.kurilka2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String id;
    private String name;
    @JsonIgnore
    private String password;
    private String avatar;
    private String email;
    private String locale;
}
