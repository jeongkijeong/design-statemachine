package com.mlog.state.machine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StateContext {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private int cache = 0;

	private WorkState000 stateWatch = null;
	private WorkState100 stateStart = null;
	private WorkState110 stateClose = null;
	private WorkState120 stateExect = null;

	private StateAction stateAction100 = null;
	private StateAction stateAction110 = null;
	private StateAction stateAction120 = null;

	public StateContext() {
		stateWatch = new WorkState000();
		stateStart = new WorkState100();
		stateClose = new WorkState110();
		stateExect = new WorkState120();
	}

	public State getState(int type) {
		State state = null;

		switch (type) {
		case StateAction.WATCH:
			state = stateWatch;
			
			break;
		case StateAction.START:
			state = stateStart;

			break;
		case StateAction.EXECT:
			state = stateExect;

			break;
		case StateAction.CLOSE:
			state = stateClose;

			break;
		default:
			break;
		}
		
		if (state != null) {
			logger.debug("get machine state {}", state.toString());
		}

		return state;
	}

	public int getCache() {
		return cache;
	}

	public void setCache(int cache) {
		this.cache = cache;
	}

	public StateAction getStateAction100() {
		return stateAction100;
	}

	public void setStateAction100(StateAction stateAction100) {
		this.stateAction100 = stateAction100;
	}

	public StateAction getStateAction110() {
		return stateAction110;
	}

	public void setStateAction110(StateAction stateAction110) {
		this.stateAction110 = stateAction110;
	}

	public StateAction getStateAction120() {
		return stateAction120;
	}

	public void setStateAction120(StateAction stateAction120) {
		this.stateAction120 = stateAction120;
	}
	
	
	
}
