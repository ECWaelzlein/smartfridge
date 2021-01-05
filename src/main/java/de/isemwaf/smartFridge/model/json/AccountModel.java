package de.isemwaf.smartFridge.model.json;

import javax.validation.constraints.NotBlank;

public class AccountModel {
    private String username;
    private String passwordHash;

    @NotBlank
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NotBlank
    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
