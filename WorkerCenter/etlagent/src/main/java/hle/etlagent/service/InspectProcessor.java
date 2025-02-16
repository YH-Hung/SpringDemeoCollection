package hle.etlagent.service;

import hle.etlagent.model.TimeWindow;

public interface InspectProcessor {

    void fetchInspRawByWindow(TimeWindow timeWindow);
    void updateAdoptByWindow(TimeWindow timeWindow);
}
