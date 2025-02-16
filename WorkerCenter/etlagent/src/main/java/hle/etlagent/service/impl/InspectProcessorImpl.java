package hle.etlagent.service.impl;

import hle.etlagent.dao.ProductInspRepo;
import hle.etlagent.dao.ProductWorkflowAdoptRepo;
import hle.etlagent.model.TimeWindow;
import hle.etlagent.service.InspectProcessor;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;

@Service
public class InspectProcessorImpl implements InspectProcessor {

    private final ProductInspRepo inspRepo;
    private final ProductWorkflowAdoptRepo workflowAdoptRepo;

    public InspectProcessorImpl(ProductInspRepo inspRepo, ProductWorkflowAdoptRepo workflowAdoptRepo) {
        this.inspRepo = inspRepo;
        this.workflowAdoptRepo = workflowAdoptRepo;
    }

    @Override
    @Retry(name = "retryFetchInsp")
    public void fetchInspRawByWindow(TimeWindow timeWindow) {
        var inspRaws = inspRepo.findBetween(timeWindow.from(), timeWindow.to());
        workflowAdoptRepo.saveRaws(inspRaws);
    }

    @Override
    public void updateAdoptByWindow(TimeWindow timeWindow) {

    }
}
