package com.mlog.state.machine;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StateMachineManager {
	private Logger logger = LoggerFactory.getLogger(getClass());

	private static StateMachineManager instance = null;

	private Map<String, StateMachineHandler> handlerStore = null;

	// 3단계로 구성된 스테이트 머신을 의미.
	private StateAction stateStep1 = null;
	private StateAction stateStep2 = null;
	private StateAction stateStep3 = null;

	public static StateMachineManager getInstance() {
		if (instance == null) {
			instance = new StateMachineManager();
		}

		return instance;
	}

	private void startMachine(String machineId, Map<String, Object> param, boolean runnable) {
		logger.debug("startMachine id : {}", machineId);

		if (stateStep1 == null) {
			logger.error("state step1 is not prepared.");
			return;
		}

		if (stateStep2 == null) {
			logger.error("state step2 is not prepared.");
			return;
		}

		if (stateStep3 == null) {
			logger.error("state step3 is not prepared.");
			return;
		}

		if (handlerStore == null) {
			handlerStore = new HashMap<String, StateMachineHandler>();
		}

		StateMachineHandler stateMachineHandler = new StateMachineHandler(stateStep1, stateStep2, stateStep3);
		if (runnable == false) {
			stateMachineHandler.handler(param);
		} else {
			Thread thread = new Thread(stateMachineHandler);
			thread.start();
			
			stateMachineHandler.put(param);
		}

		handlerStore.put(machineId, stateMachineHandler);
	}

	private void closeMachine(String machineId, Map<String, Object> param) {
		logger.debug("closeMachien id : {}", machineId);

		StateMachineHandler stateMachineHandler = handlerStore.get(machineId);
		stateMachineHandler.put(param);
	}

	public void address(Map<String, Object> param, int type) {
		if (param == null) {
			param = new HashMap<String, Object>();
		}
		param.put("TYPE", type);

		String machineId = (String) param.get(StateAction.WORK_ID);
		switch (type) {
		case StateAction.START:
			startMachine(machineId, param, false);

			break;
		case StateAction.CLOSE:
			closeMachine(machineId, param);

			break;
		default:
			break;
		}

		logger.info("work type : {} / id : {}", type, machineId);
	}

	public void setStateStep1(StateAction step1) {
		stateStep1 = step1;
	}

	public void setStateStep2(StateAction step2) {
		stateStep2 = step2;
	}

	public void setStateStep3(StateAction step3) {
		stateStep3 = step3;
	}
}
