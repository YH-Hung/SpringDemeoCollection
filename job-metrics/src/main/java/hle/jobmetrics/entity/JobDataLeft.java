package hle.jobmetrics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "job_data_left")
public class JobDataLeft {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "job_data_left_id_gen")
    @SequenceGenerator(name = "job_data_left_id_gen", sequenceName = "job_data_left_seq_id_seq", allocationSize = 1)
    @Column(name = "seq_id", nullable = false)
    private Integer id;

    @Column(name = "category", length = 10)
    private String category;

    @Column(name = "data_time")
    private LocalDateTime dataTime;

}