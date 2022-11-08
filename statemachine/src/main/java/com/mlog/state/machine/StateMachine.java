package com.mlog.state.machine;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StateMachine {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private State state = null;
	private StateContext context = null;

	private String machineId = null;
	private Map<String, Object> machineProcess = null;

	public StateMachine() {
		super();
		context = new StateContext();
		setState(StateAction.WATCH);
	}

	public int doAction() {
		int retv = StateAction.FAILURE;

		try {
			if (state != null) {
				retv = state.doAction(this);
			}

			State nextState = this.state;
			if (nextState != null) {
				if (nextState.event() != StateAction.WATCH) {
					retv = StateAction.NEXT; // 다음상태 수행.
				} else {
					retv = StateAction.STOP; // 머신종료.
				}
			} else {
				retv = StateAction.STOP;
			}

		} catch (Exception e) {
			logger.error("", e);
		}

		return retv;
	}

	public State getState() {
		return this.state;
	}

	public State setState(int type) {
		if (context != null) {
			state = context.getState(type);
		}
		return state;
	}

	public String getMachineId() {
		return machineId;
	}

	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

	public void clear() {
		machineId = null;
	}

	public void close() {
		clear();
	}

	public synchronized StateContext getContext() {
		return this.context;
	}

	public Map<String, Object> getMachineProcess() {
		return machineProcess;
	}

	public void setMachineProcess(Map<String, Object> machineProcess) {
		this.machineProcess = machineProcess;

		if (this.machineProcess != null) {
			this.machineId = (String) this.machineProcess.get(StateAction.WORK_ID);
		}
	}

}
