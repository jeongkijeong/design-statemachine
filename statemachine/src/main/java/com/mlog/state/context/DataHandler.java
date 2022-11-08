package com.mlog.state.context;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.mlog.state.common.Constant;

public abstract class DataHandler implements Runnable {
	private ArrayBlockingQueue<Map<String, Object>> queue = null;
	private Boolean doRun = null;
	
	public DataHandler() {
		super();
		queue = new ArrayBlockingQueue<Map<String, Object>>(100);
		this.doRun = Constant.RUN;
	}

	@Override
	public void run() {
		while (doRun) {
			try {
				Map<String, Object> object = queue.poll(3, TimeUnit.SECONDS);
				if (object == null) {
					continue;
				}

				handler(object);
			} catch (Exception e) {
			}
		}
	}

	public void put(Map<String, Object> object) {
		try {
			queue.put(object);
		} catch (Exception e) {
		}
	}

	public boolean isRun() {
		return this.doRun;
	}

	public void close() {
		this.doRun = false;
	}

	public int dataSize() {
		int size = 0;

		if (queue != null) {
			size = queue.size();
		}

		return size;
	}

	public abstract void handler(Map<String, Object> object);
}
