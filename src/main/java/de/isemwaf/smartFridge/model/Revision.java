package de.isemwaf.smartFridge.model;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Entity
@RevisionEntity
public class Revision implements Serializable {

	@Serial
	private static final long serialVersionUID = 3749373712434996947L;

	@RevisionNumber
	private long id;
	@RevisionTimestamp
	private Date created;

	@Id
	@GeneratedValue(
	        strategy = GenerationType.SEQUENCE,
	        generator = "revision_seq_generator")
	@SequenceGenerator(
	        name = "revision_seq_generator",
	        sequenceName = "revision_seq",
	        allocationSize = 1)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(nullable = false)
	public Date getCreated() {
		return created == null ? null : new Date(created.getTime());
	}

	public void setCreated(Date created) {
		this.created = created == null ? null : new Date(created.getTime());
	}
}