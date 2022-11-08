package com.mlog.state.machine;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mlog.state.context.DataHandler;

public class StateMachineHandler extends DataHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());

	private StateMachine stateMachine = null;

	public StateMachineHandler() {
		super();

		this.stateMachine = new StateMachine();
	}

	public StateMachineHandler(StateAction step1, StateAction step2, StateAction step3) {
		super();

		this.stateMachine = new StateMachine();

		this.setStateStep1(step1);
		this.setStateStep2(step2);
		this.setStateStep3(step3);
	}

	public void handler(Map<String, Object> object) {
		try {
			if (object == null) {
				return;
			}
			
			Map<String, Object> process = object;

			int type = (Integer) process.get("TYPE");
			if (type == StateAction.START) {
				stateMachine.setMachineProcess(process);
			}

			while (true) {
				int retv = stateMachine.doAction();
				if (retv == StateAction.STOP) {
					break;
				}
			}

			// close handler thread.
			close();
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			close();
		}
	}

	public State getState() {
		State state = null;

		if (stateMachine != null) {
			state = stateMachine.getState();
		}

		return state;
	}

	public void setState(int type) {
		if (stateMachine != null) {
			stateMachine.setState(type);
		}
	}

	public void setStateStep1(StateAction state) {
		stateMachine.getContext().setStateAction100(state);
	}

	public void setStateStep2(StateAction state) {
		stateMachine.getContext().setStateAction110(state);
	}

	public void setStateStep3(StateAction state) {
		stateMachine.getContext().setStateAction120(state);
	}

}
