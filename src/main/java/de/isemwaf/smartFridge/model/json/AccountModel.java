package de.isemwaf.smartFridge.model.json;

import javax.validation.constraints.NotBlank;

public class AccountModel {
    private String username;
    private String password;

    @NotBlank
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NotBlank
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
