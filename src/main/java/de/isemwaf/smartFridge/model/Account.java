package de.isemwaf.smartFridge.model;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Audited(withModifiedFlag = true)
public class Account extends SuperEntity {
    private long id;
    private String firstname;
    private String lastname;

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
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    @Column(length = 50, nullable = false)
    @NotBlank
    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
