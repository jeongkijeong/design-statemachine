package com.mlog.state.machine;

public interface State {
	public int doAction(StateMachine stateMachine);
	
	public int event();
}
