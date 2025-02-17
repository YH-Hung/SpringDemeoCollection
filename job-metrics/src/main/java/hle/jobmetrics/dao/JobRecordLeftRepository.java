package hle.jobmetrics.dao;

import hle.jobmetrics.entity.JobRecordLeft;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRecordLeftRepository extends JpaRepository<JobRecordLeft, Integer> {
}