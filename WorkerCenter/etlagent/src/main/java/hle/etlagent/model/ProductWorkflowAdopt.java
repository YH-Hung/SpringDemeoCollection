package hle.etlagent.model;

import java.time.LocalDateTime;

public record ProductWorkflowAdopt(Integer productId,
                                   LocalDateTime inspectDate,
                                   String adopt) {
}
