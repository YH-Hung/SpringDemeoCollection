package hle.jobmetrics.job;

import hle.jobmetrics.constant.CategoryConst;
import hle.jobmetrics.dao.JobDataLeftRepository;
import hle.jobmetrics.dao.JobDataRightRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class UpdateTimeJob {

    private final JobDataLeftRepository leftRepository;
    private final JobDataRightRepository rightRepository;

    public UpdateTimeJob(JobDataLeftRepository leftRepository, JobDataRightRepository rightRepository) {
        this.leftRepository = leftRepository;
        this.rightRepository = rightRepository;
    }


    @Scheduled(fixedDelayString = "${job.delay.update.go-deep.left}", initialDelayString = "${job.init.update.go-deep.left}")
    public void updateLeftGoDeep() {
        updateLeft(CategoryConst.GO_DEEP);
    }

    @Scheduled(fixedDelayString = "${job.delay.update.squeeze-nn.left}", initialDelayString = "${job.init.update.squeeze-nn.left}")
    public void updateLeftSqueezeNn() {
        updateLeft(CategoryConst.SQUEEZE_NN);
    }


    @Scheduled(fixedDelayString = "${job.delay.update.go-deep.right}", initialDelayString = "${job.init.update.go-deep.right}")
    public void updateRightGoDeep() {
        updateRight(CategoryConst.GO_DEEP);
    }

    @Scheduled(fixedDelayString = "${job.delay.update.squeeze-nn.right}", initialDelayString = "${job.init.update.squeeze-nn.right}")
    public void updateRightSqueezeNn() {
        updateRight(CategoryConst.SQUEEZE_NN);
    }

    private void updateLeft(String category) {
        log.info("Update left {}", category);
        var entityOp = leftRepository.findByCategory(category);
        entityOp.ifPresent(entity -> {
            entity.setDataTime(LocalDateTime.now());
            leftRepository.save(entity);
        });
    }

    private void updateRight(String category) {
        log.info("Update right {}", category);
        var entityOp = rightRepository.findByCategory(category);
        entityOp.ifPresent(entity -> {
            entity.setDataTime(LocalDateTime.now());
            rightRepository.save(entity);
        });
    }
}
