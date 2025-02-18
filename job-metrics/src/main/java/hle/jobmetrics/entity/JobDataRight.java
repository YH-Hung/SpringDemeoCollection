package hle.jobmetrics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "job_data_right")
public class JobDataRight {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "job_data_right_id_gen")
    @SequenceGenerator(name = "job_data_right_id_gen", sequenceName = "job_data_right_seq_id_seq", allocationSize = 1)
    @Column(name = "seq_id", nullable = false)
    private Integer id;

    @Column(name = "category", length = 10)
    private String category;

    @Column(name = "data_time")
    private LocalDateTime dataTime;

}