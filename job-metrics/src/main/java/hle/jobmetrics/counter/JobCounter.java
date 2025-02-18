package hle.jobmetrics.counter;

import hle.jobmetrics.constant.CategoryConst;
import io.micrometer.core.annotation.Counted;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JobCounter {
    @Counted(value = "job-long-running-count", description = "counter of job running", extraTags = {"job-name", CategoryConst.GO_DEEP})
    public void countSyncGoDeep() {
        log.info("Count sync go deep");
    }

    @Counted(value = "job-long-running-count", description = "counter of job running", extraTags = {"job-name", CategoryConst.SQUEEZE_NN})
    public void countSyncSqueezeNn() {
        log.info("Count sync squeeze nn");
    }
}
