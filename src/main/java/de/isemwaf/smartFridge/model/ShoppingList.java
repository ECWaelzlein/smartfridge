package de.isemwaf.smartFridge.model;

import javax.persistence.*;

@Entity
public class ShoppingList extends SuperEntity {

    private long id;

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "SEQUENCE_SHOPPINGLIST_ID"
    )
    @SequenceGenerator(
            name = "SEQUENCE_SHOPPINGLIST_ID",
            sequenceName = "SEQUENCE_SHOPPINGLIST_ID",
            allocationSize = 1
    )

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
