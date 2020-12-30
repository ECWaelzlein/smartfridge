package de.isemwaf.smartFridge.model;

import de.isemwaf.smartFridge.enums.Category;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Food extends SuperEntity {
    private long id;
    private String barcode;
    private Category category;
    private String quantity;
    private String name;

    @Id
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
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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
