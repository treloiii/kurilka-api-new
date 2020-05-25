package com.trelloiii.kurilka2.jwt;

import java.util.List;

public class JwtResponse {
    private String username;
    private String jwt;
    List<String> roles;

    public JwtResponse(String username, String jwt, List<String> roles) {
        this.username = username;
        this.jwt = jwt;
        this.roles = roles;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public String getJwt() {
        return this.jwt;
    }

    public List<String> getRoles() {
        return roles;
    }
}
