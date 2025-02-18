package hle.jobmetrics.dao;

import hle.jobmetrics.entity.JobDataLeft;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobDataLeftRepository extends JpaRepository<JobDataLeft, Integer> {

  Optional<JobDataLeft> findByCategory(String category);
}