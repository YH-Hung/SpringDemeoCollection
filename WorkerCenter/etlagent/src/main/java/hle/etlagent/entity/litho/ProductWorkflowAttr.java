package hle.etlagent.entity.litho;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "product_workflow_attr")
public class ProductWorkflowAttr {
    @EmbeddedId
    private ProductWorkflowAttrId id;

    @MapsId("wid")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "wid", nullable = false)
    private hle.etlagent.entity.litho.ProductWorkflowMain wid;

    @Column(name = "val", length = 17)
    private String val;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

}