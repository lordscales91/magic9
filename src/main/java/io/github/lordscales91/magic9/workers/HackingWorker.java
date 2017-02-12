package io.github.lordscales91.magic9.workers;

import io.github.lordscales91.magic9.core.CallbackReceiver;
import io.github.lordscales91.magic9.core.HackingProcess;

public class HackingWorker extends MagicWorker {

	private HackingProcess proc;

	public HackingWorker(HackingProcess proc, String tag, CallbackReceiver receiver) {
		super(tag, receiver);
		this.proc = proc;
	}

	@Override
	public void stop() {
		// Do nothing
	}

	@Override
	protected String doInBackground() throws Exception {
		proc.process();
		return "success";
	}

}
