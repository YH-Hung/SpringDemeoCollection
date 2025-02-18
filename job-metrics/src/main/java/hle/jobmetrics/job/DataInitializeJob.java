package hle.jobmetrics.job;

import hle.jobmetrics.constant.CategoryConst;
import hle.jobmetrics.dao.JobDataLeftRepository;
import hle.jobmetrics.dao.JobDataRightRepository;
import hle.jobmetrics.entity.JobDataLeft;
import hle.jobmetrics.entity.JobDataRight;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class DataInitializeJob implements CommandLineRunner {

    private final JobDataLeftRepository leftRepository;
    private final JobDataRightRepository rightRepository;

    public DataInitializeJob(JobDataLeftRepository leftRepository, JobDataRightRepository rightRepository) {
        this.leftRepository = leftRepository;
        this.rightRepository = rightRepository;
    }

    @Override
    public void run(String... args) {
        leftRepository.findByCategory(CategoryConst.GO_DEEP)
                .ifPresentOrElse(lgd -> log.info("Left data {} existed", CategoryConst.GO_DEEP), () -> {
                    JobDataLeft left1 = new JobDataLeft();
                    left1.setCategory(CategoryConst.GO_DEEP);
                    left1.setDataTime(LocalDateTime.now());
                    leftRepository.save(left1);
                });


        leftRepository.findByCategory(CategoryConst.SQUEEZE_NN)
                .ifPresentOrElse(lgd -> log.info("Left data {} existed", CategoryConst.SQUEEZE_NN), () -> {
                    JobDataLeft left2 = new JobDataLeft();
                    left2.setCategory(CategoryConst.SQUEEZE_NN);
                    left2.setDataTime(LocalDateTime.now());
                    leftRepository.save(left2);
                });


        rightRepository.findByCategory(CategoryConst.GO_DEEP)
                .ifPresentOrElse(rgd -> log.info("Right data {} existed", CategoryConst.GO_DEEP), () -> {
                    JobDataRight right1 = new JobDataRight();
                    right1.setCategory(CategoryConst.GO_DEEP);
                    right1.setDataTime(LocalDateTime.now());
                    rightRepository.save(right1);
                });

        rightRepository.findByCategory(CategoryConst.SQUEEZE_NN)
                .ifPresentOrElse(rgd -> log.info("Right data {} existed", CategoryConst.SQUEEZE_NN), () -> {
                    JobDataRight right2 = new JobDataRight();
                    right2.setCategory(CategoryConst.SQUEEZE_NN);
                    right2.setDataTime(LocalDateTime.now());
                    rightRepository.save(right2);
                });
    }
}
