package com.mlog.state.machine;

public interface StateAction {

	public final int WATCH = 000;
	public final int START = 100;
	public final int CLOSE = 110;
	public final int EXECT = 120;
	
	public int SUCCESS = +0;
	public int FAILURE = -1;

	public int doAction();
	
	public String WORK_ID = "WORK_ID";

	public final int STOP = -1;
	public final int NEXT = 0;
}
