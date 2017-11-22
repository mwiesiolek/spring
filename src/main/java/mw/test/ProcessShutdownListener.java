package mw.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class ProcessShutdownListener implements ApplicationListener<ContextClosedEvent> {

    private boolean dead;

    public ProcessShutdownListener() {
        log.info("App initialized.");

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(() -> {
            if (!dead) {
                log.info("Processing...");
            }
        }, 0, 2, TimeUnit.SECONDS);
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        dead = true;

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
