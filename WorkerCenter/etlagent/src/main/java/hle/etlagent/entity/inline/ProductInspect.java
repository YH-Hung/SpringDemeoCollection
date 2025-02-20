package hle.etlagent.entity.inline;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "product_inspect")
public class ProductInspect {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_inspect_id_gen")
    @SequenceGenerator(name = "product_inspect_id_gen", sequenceName = "product_inspect_uid_seq", allocationSize = 1)
    @Column(name = "uid", nullable = false)
    private Integer id;

    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(name = "inspect_date")
    private LocalDateTime inspectDate;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

}