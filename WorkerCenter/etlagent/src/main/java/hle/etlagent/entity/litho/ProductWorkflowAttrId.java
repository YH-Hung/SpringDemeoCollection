package hle.etlagent.entity.litho;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class ProductWorkflowAttrId implements Serializable {
    private static final long serialVersionUID = -1975056573138513463L;
    @Column(name = "wid", nullable = false)
    private Integer wid;

    @Column(name = "workflow_type", nullable = false, length = 20)
    private String workflowType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProductWorkflowAttrId entity = (ProductWorkflowAttrId) o;
        return Objects.equals(this.wid, entity.wid) &&
                Objects.equals(this.workflowType, entity.workflowType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wid, workflowType);
    }

}