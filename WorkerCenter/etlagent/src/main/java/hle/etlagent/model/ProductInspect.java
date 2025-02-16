package hle.etlagent.model;

import java.time.LocalDateTime;

public record ProductInspect(Integer uid,
                             String productId,
                             LocalDateTime inspectDate,
                             LocalDateTime createDate,
                             LocalDateTime lastModifiedDate) {
}
