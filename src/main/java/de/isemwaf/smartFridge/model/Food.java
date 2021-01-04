package de.isemwaf.smartFridge.model;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;

@Entity
@Audited(withModifiedFlag = true)
public class Food extends SuperEntity {
    private long id;
    private String barcode;
    private String quantity;
    private String name;

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "SEQUENCE_FOOD_ID"
    )
    @SequenceGenerator(
            name = "SEQUENCE_FOOD_ID",
            sequenceName = "SEQUENCE_FOOD_ID",
            allocationSize = 1
    )
    public long getId() {
        return id;
    }

    public void setId(long id_new) {
        id = id_new;
    }

    @Column
    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    @Column
    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @Column
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
