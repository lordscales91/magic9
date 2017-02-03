package io.github.lordscales91.magic9.workers;

import io.github.lordscales91.magic9.core.CallbackReceiver;
import io.github.lordscales91.magic9.core.MagicUtils;
import io.github.lordscales91.magic9.http.ProgressListener;
import io.github.lordscales91.magic9.http.ProgressResponseBody;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.apache.commons.io.IOUtils;

public class DownloadWorker extends MagicWorker implements ProgressListener {
	
	public static final String REAL_PROGRESS = "real_progress";
	
	private float progress;
	private String url;
	private File out;
	private Call dlproc;

	
	public DownloadWorker(String url, File out, String tag, CallbackReceiver receiver) {
		super(tag, receiver);
		this.url = url;
		this.out = out;
	}

	@Override
	protected String doInBackground() throws Exception {
		final ProgressListener progressListener = this;
		OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
			
			@Override
			public Response intercept(Chain chain) throws IOException {
				Response originalResponse = chain.proceed(chain.request());
	            return originalResponse.newBuilder()
	                .body(new ProgressResponseBody(originalResponse.body(), progressListener))
	                .build();
			}
		}).build();
		Request req = new Request.Builder().url(url).build();
		Response resp = null;
		try {
			dlproc = client.newCall(req);
			resp = dlproc.execute();
			if(!resp.isSuccessful()) {
				throw new IOException("Error Happened: "+resp.message());
			}
			// FileUtils.copyInputStreamToFile(resp.body().byteStream(), out);
			MagicUtils.saveStreamToFile(resp.body().byteStream(), out);
		} finally {
			IOUtils.closeQuietly(resp);
		}		
		return "success";
	}

	@Override
	public void update(long bytesRead, long contentLength) {
		float oldProgress = progress;
		progress = (bytesRead * 100.0f) / contentLength;
		int iProgress = (int) progress;
		setProgress((iProgress > 100)?100:iProgress); // deal with precision loss
		firePropertyChange(REAL_PROGRESS, oldProgress, progress);
	}

	@Override
	public void stop() {		
		setStopFlag();
		if(dlproc != null) {
			dlproc.cancel();
		}				
	}
	
	@Override
	public float getRealProgress() {
		return progress;
	}
	
	@Override
	public boolean hasRealProgressSupport() {
		return true;
	}
}
