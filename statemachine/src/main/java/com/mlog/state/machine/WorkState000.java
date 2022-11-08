package com.mlog.state.machine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkState000 implements State {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public int doAction(StateMachine stateMachine) {
		String machineId = stateMachine.getMachineId();
		
		String currState = toString();
		String nextState = null;

		int retv = action(stateMachine);
		if (retv == StateAction.SUCCESS) {
			nextState = stateMachine.setState(StateAction.START).toString();

			logger.info("success id : [{}] set this state : [{}] -> next state : [{}]", machineId, currState, nextState);
		} else {
			nextState = stateMachine.setState(StateAction.CLOSE).toString();

			logger.info("failure id : [{}] set this state : [{}] -> next state : [{}]", machineId, currState, nextState);
		}

		return retv;
	}

	private int action(StateMachine stateMachine) {
		int retv = StateAction.FAILURE;

		try {
			if (stateMachine != null) {
				retv = StateAction.SUCCESS;
			}
		} catch (Exception e) {
			logger.error("", e);
		}

		return retv;
	}

	public int event() {
		return StateAction.WATCH;
	}

	@Override
	public String toString() {
		return "WATCH";
	}
}
