package net.nodus.database.common;

import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@Setter
public abstract class MutableDocument extends BaseDocument {

    @LastModifiedDate
    private Instant updatedAt = Instant.now();

    public void touch() {
        touch(Instant.now());
    }

    public void touch(Instant now) {
        this.updatedAt = now;
    }

    @Override
    public void softDelete(Instant now) {
        setDeletedAt(now);
        this.updatedAt = now;
    }

    @Override
    public void restore(Instant now) {
        setDeletedAt(null);
        this.updatedAt = now;
    }
}
