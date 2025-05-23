package hle.jobmetrics.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Entity
@Table(name = "job_record_right")
@NoArgsConstructor
@AllArgsConstructor
public class JobRecordRight {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "job_record_right_id_gen")
    @SequenceGenerator(name = "job_record_right_id_gen", sequenceName = "job_record_right_seq_id_seq", allocationSize = 1)
    @Column(name = "seq_id", nullable = false)
    private Integer id;

    @Column(name = "category", nullable = false, length = 10)
    private String category;

    @Column(name = "data_time", nullable = false)
    private LocalDateTime dataTime;

}