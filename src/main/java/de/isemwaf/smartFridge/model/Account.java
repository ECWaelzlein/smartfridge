package de.isemwaf.smartFridge.model;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Audited(withModifiedFlag = true)
public class Account extends SuperEntity {
    private long id;
    private String username;
    private String password;

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "SEQUENCE_ACCOUNT_ID"
    )
    @SequenceGenerator(
            name = "SEQUENCE_ACCOUNT_ID",
            sequenceName = "SEQUENCE_ACCOUNT_ID",
            allocationSize = 1
    )

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(length = 50, nullable = false)
    @NotBlank
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(length = 120, nullable = false)
    @NotBlank
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
