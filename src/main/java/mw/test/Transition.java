package mw.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;

@Slf4j
@WithStateMachine
public class Transition {

    public Transition() {
        log.info("Transition class created.");
    }

    @OnTransition(target = "STATE1")
    public void toState1() {
        log.info("Transitioned to STATE1");
    }

    @OnTransition(target = "STATE2")
    public void toState2() {
        log.info("Transitioned to STATE2");
    }
}
