package mw.test;

import javax.annotation.PostConstruct;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class StateMachineInitializator {

    private StateMachine<State, Event> stateMachine;

    public StateMachineInitializator(StateMachine<State, Event> stateMachine) {
        this.stateMachine = stateMachine;
    }

    @PostConstruct
    public void start() {
        stateMachine.start();

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(() -> {
            stateMachine.sendEvent(Event.EVENT1);
            stateMachine.sendEvent(Event.EVENT2);
        }, 5, 5, TimeUnit.SECONDS);
    }
}
