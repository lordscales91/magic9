package io.github.lordscales91.magic9;

import io.github.lordscales91.magic9.core.CallbackReceiver;

import javax.swing.SwingWorker;

public abstract class MagicWorker extends SwingWorker<String, String> {
	
	protected String tag;
	protected CallbackReceiver receiver;
	private boolean wasStopped;

	public MagicWorker(String tag, CallbackReceiver receiver) {
		this.tag=tag;
		this.receiver=receiver;
	}
	
	/**
	 * Subclasses should implement this method to provide a way
	 * to gracefully stop themselves
	 */
	public abstract void stop();
	
	/**
	 * This should be used by subclasses to inform the worker has been stopped
	 */
	protected void setStopFlag() {
		wasStopped = true;
	}
	
	/**
	 * This returns the progress as a float. Subclasses implementing this method
	 * need to override {@link #hasRealProgressSupport()} as well.
	 */
	public float getRealProgress() {
		return 0.0f;
	}
	
	/**
	 * Informs if this worker has implemented support for floating point progress.
	 * If it doesn't, it should implement at least {@link #setProgress(int)}
	 */
	public boolean hasRealProgressSupport() {
		return false;
	}
	
	/**
	 * This method returns the tag associated to this worker
	 */
	public String getTag() {
		return tag;
	}
	
	
	@Override
	protected void done() {
		try {
			if(!wasStopped) {
				receiver.receiveData(get(), tag);
			}			
		} catch (Exception e) {
			if(!wasStopped) { // We don't care about exceptions when stopping manually
				receiver.receiveData(e, tag);
			}			
		}
	}
}
