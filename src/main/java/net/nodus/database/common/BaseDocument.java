package net.nodus.database.common;

import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;

@Getter
@Setter
public abstract class BaseDocument {

    @CreatedDate
    private Instant createdAt = Instant.now();

    @Indexed
    private Instant deletedAt;

    public void softDelete() {
        softDelete(Instant.now());
    }

    public void softDelete(Instant now) {
        this.deletedAt = now;
    }

    public void restore() {
        restore(Instant.now());
    }

    public void restore(Instant now) {
        this.deletedAt = null;
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }
}
