package de.isemwaf.smartFridge.model;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
public abstract class SuperEntity {
    Date created;
    Date lastModified;
    int version;

    @Column
    public Date getCreated() {
        return new Date(created.getTime());
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Column()
    public Date getLastModified() {
        return new Date(lastModified.getTime());
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    @PrePersist
    protected void onCreate() {
        Date date = new Date();

        created = date;
        lastModified = date;
    }

    @PreUpdate
    protected void onUpdate() {
        lastModified = new Date();
    }

    @Version
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
