package io.github.lordscales91.magic9;

public class MagicWorkerHandler {
	public static final long DEFAULT_WAIT_TIME = 30000; 
	public static final int DEFAULT_MAX_RETRIES = 5;

	private MagicWorker worker;
	private long waitTime;
	private long startTime;
	private int maxRetries;
	private int retries;
	
	public MagicWorkerHandler(MagicWorker worker) {
		this(worker, DEFAULT_WAIT_TIME, DEFAULT_MAX_RETRIES);
	}

	public MagicWorkerHandler(MagicWorker worker, long waitTime, int maxRetries) {
		this.worker = worker;
		this.waitTime = waitTime;
		this.maxRetries = maxRetries;
	}
	
	public void startWorker() {
		this.startTime = System.currentTimeMillis();
		this.worker.execute();
	}
	
	public boolean isStuck() {
		return worker.getProgress() == 0 && (System.currentTimeMillis() - startTime) > waitTime;
	}
	
	public boolean restart(MagicWorker newWorker) {
		return restart(newWorker, false);
	}
	
	public boolean restart(MagicWorker newWorker, boolean isAuto) {
		boolean restarted = true;
		if(isAuto && ++retries > maxRetries) {
			restarted = false;
		} else {
			this.worker.stop(); // Stop the worker
			this.worker = newWorker; // Get the new worker
			startWorker(); // Start it
		}
		return restarted;
	}
	
	public MagicWorker getWorker() {
		return worker;
	}

	public String getTag() {
		return this.worker.getTag();
	}
}
