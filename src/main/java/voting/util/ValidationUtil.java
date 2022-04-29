package voting.util;

import lombok.experimental.UtilityClass;
import voting.error.IllegalRequestDataException;
import voting.model.BaseEntity;

import java.time.LocalDateTime;
import java.time.LocalTime;

@UtilityClass
public class ValidationUtil {

    public static void checkNew(BaseEntity entity) {
        if (!entity.isNew()) {
            throw new IllegalRequestDataException(entity.getClass().getSimpleName() + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(BaseEntity entity, int id) {
        if (entity.isNew()) {
            entity.setId(id);
        } else if (entity.id() != id) {
            throw new IllegalRequestDataException(entity.getClass().getSimpleName() + " must have id=" + id);
        }
    }

    public static void checkTime(LocalDateTime time) {
        if (time.toLocalTime().isAfter(LocalTime.of(11, 0))) {
            throw new IllegalRequestDataException("You can't change your vote after 11:00");
        }
    }
}
