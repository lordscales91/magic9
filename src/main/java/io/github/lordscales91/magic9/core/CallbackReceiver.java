package io.github.lordscales91.magic9.core;

/**
 * Simple interface to return information back to caller via the {@link CallbackReceiver#receiveData(Object, String)}
 */
public interface CallbackReceiver {
	/**
	 * Is called by other class to return information back.
	 * @param data The information received
	 * @param tag Intended to distinguish between different calls.
	 */
	public void receiveData(Object data, String tag);
}
