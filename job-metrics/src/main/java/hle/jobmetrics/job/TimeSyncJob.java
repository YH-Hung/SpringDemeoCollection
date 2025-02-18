package hle.jobmetrics.job;

import hle.jobmetrics.constant.CategoryConst;
import hle.jobmetrics.counter.JobCounter;
import hle.jobmetrics.dao.JobDataLeftRepository;
import hle.jobmetrics.dao.JobDataRightRepository;
import hle.jobmetrics.dao.JobRecordLeftRepository;
import hle.jobmetrics.dao.JobRecordRightRepository;
import hle.jobmetrics.entity.JobRecordLeft;
import hle.jobmetrics.entity.JobRecordRight;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class TimeSyncJob {

    private final JobRecordLeftRepository recordLeftRepository;
    private final JobDataLeftRepository dataLeftRepository;
    private final JobRecordRightRepository recordRightRepository;
    private final JobDataRightRepository dataRightRepository;

    private final AtomicInteger goDeepIsRunning = new AtomicInteger(0);
    private final AtomicInteger squeezeNnIsRunning = new AtomicInteger(0);
    private final JobCounter jobCounter;

    public TimeSyncJob(MeterRegistry registry, JobRecordLeftRepository recordLeftRepository, JobDataLeftRepository dataLeftRepository,
                       JobRecordRightRepository recordRightRepository, JobDataRightRepository dataRightRepository,
                       JobCounter jobCounter) {
        this.recordLeftRepository = recordLeftRepository;
        this.dataLeftRepository = dataLeftRepository;
        this.recordRightRepository = recordRightRepository;
        this.dataRightRepository = dataRightRepository;
        this.jobCounter = jobCounter;

        Gauge.builder("job-running-status", goDeepIsRunning, AtomicInteger::get)
                .description("running: 1 / not running: 0")
                .tag("job-name", CategoryConst.GO_DEEP)
                .register(registry);

        Gauge.builder("job-running-status", squeezeNnIsRunning, AtomicInteger::get)
                .description("running: 1 / not running: 0")
                .tag("job-name", CategoryConst.SQUEEZE_NN)
                .register(registry);
    }

    @Timed(value = "job-running-time", description = "time distribution of running", extraTags = {"job-name", CategoryConst.GO_DEEP}, histogram = true)
    @Scheduled(fixedDelay = 30_000)
    public void syncGoDeep() {
        goDeepIsRunning.set(1);
        sync(CategoryConst.GO_DEEP);
        jobCounter.countSyncGoDeep();
        goDeepIsRunning.set(0);
    }

    @Timed(value = "job-running-time", description = "time distribution of running", extraTags = {"job-name", CategoryConst.SQUEEZE_NN}, histogram = true)
    @Scheduled(fixedDelay = 30_000)
    public void syncSqueezeNn() {
        squeezeNnIsRunning.set(1);
        sync(CategoryConst.SQUEEZE_NN);
        jobCounter.countSyncSqueezeNn();
        squeezeNnIsRunning.set(0);
    }

    private void sync(String category) {
        log.info("Start do sync {}...", category);

        var leftData = dataLeftRepository.findByCategory(category);
        leftData.ifPresent(data -> {
            var record = JobRecordLeft.builder()
                    .category(category)
                    .dataTime(data.getDataTime())
                    .build();

            recordLeftRepository.save(record);
        });

        var rightData = dataRightRepository.findByCategory(category);
        rightData.ifPresent(data -> {
            var record = JobRecordRight.builder()
                    .category(category)
                    .dataTime(data.getDataTime())
                    .build();

            recordRightRepository.save(record);
        });

        log.info("End do sync {}...", category);
    }
}
