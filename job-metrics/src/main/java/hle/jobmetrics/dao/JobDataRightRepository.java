package hle.jobmetrics.dao;

import hle.jobmetrics.entity.JobDataRight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobDataRightRepository extends JpaRepository<JobDataRight, Integer> {
    Optional<JobDataRight> findByCategory(String squeezeNn);
}