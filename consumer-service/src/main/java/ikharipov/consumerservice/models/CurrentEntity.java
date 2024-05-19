package ikharipov.consumerservice.models;

import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

/**
 * Базовый класс для всех сущностей приложения.
 */
@MappedSuperclass
public class CurrentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created")
    private Date created;

    public CurrentEntity() {
    }

    @PrePersist
    protected void onCreate() {
        created = new Date();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
