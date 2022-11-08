package com.mlog.state.machine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* start state */
public class WorkState100 implements State {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private StateAction stateAction = null;

	@Override
	public int doAction(StateMachine stateMachine) {
		if (stateMachine == null) {
			return StateAction.FAILURE;
		}
		
		String machineId = stateMachine.getMachineId();
		
		String currState = toString();
		String nextState = null;

		int retv = action(stateMachine);
		if (retv == StateAction.SUCCESS) {
			nextState = stateMachine.setState(StateAction.EXECT).toString();

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
				stateAction = stateMachine.getContext().getStateAction100();
			}
			
			if (stateAction != null) {
				retv = stateAction.doAction();
			}

		} catch (Exception e) {
			logger.error("", e);
		}

		return retv;
	}

	public int event() {
		return StateAction.START;
	}

	@Override
	public String toString() {
		return "START";
	}
}
