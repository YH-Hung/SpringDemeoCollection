package hle.jobmetrics.dao;

import hle.jobmetrics.entity.JobRecordRight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRecordRightRepository extends JpaRepository<JobRecordRight, Integer> {
}