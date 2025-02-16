package hle.etlagent.simulator;

import eu.rekawek.toxiproxy.Proxy;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.TimerTask;

@Slf4j
public class ReEnableProxy extends TimerTask {

    private final Proxy proxy;

    public ReEnableProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    @SneakyThrows
    @Override
    public void run() {
        log.debug("Enable proxy again...");
        proxy.enable();
    }
}
