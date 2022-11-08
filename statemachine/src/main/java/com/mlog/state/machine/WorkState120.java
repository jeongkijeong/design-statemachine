package com.mlog.state.machine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* execute state */
public class WorkState120 implements State {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private StateAction stateAction = null;

	@Override
	public int doAction(StateMachine stateMachine) {
		if (stateMachine == null) {
			return StateAction.FAILURE;
		}

		String machineId = stateMachine.getMachineId();
		
		String currState = toString();
		String nextState = stateMachine.setState(StateAction.CLOSE).toString();  // EXECUTE -> CLOSE

		int retv = action(stateMachine);
		if (retv == StateAction.SUCCESS) {
			logger.info("success id : [{}] set this state : [{}] -> next state : [{}]", machineId, currState, nextState);
		} else {
			logger.info("failure id : [{}] set this state : [{}] -> next state : [{}]", machineId, currState, nextState);
		}

		return retv;
	}

	private int action(StateMachine stateMachine) {
		int retv = StateAction.FAILURE;

		if (stateMachine != null) {
			stateAction = stateMachine.getContext().getStateAction120();
		}

		if (stateAction != null) {
			retv = stateAction.doAction();
		}

		return retv;
	}

	public int event() {
		return StateAction.EXECT;
	}

	@Override
	public String toString() {
		return "EXECT";
	}
}
