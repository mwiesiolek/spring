package mw.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProcessShutdownListener implements ApplicationListener<ContextClosedEvent> {

    public ProcessShutdownListener() {
        log.info("App initialized.");
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        try {
            destroy();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void destroy() throws InterruptedException {
        int limit = 5;
        for (int i = 0; i < limit; i++) {
            log.info("Waiting...");
            Thread.sleep(1000);
        }
    }
}
