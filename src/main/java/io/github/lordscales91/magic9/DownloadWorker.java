package io.github.lordscales91.magic9;

import io.github.lordscales91.magic9.core.CallbackReceiver;
import io.github.lordscales91.magic9.http.ProgressListener;
import io.github.lordscales91.magic9.http.ProgressResponseBody;

import java.io.File;
import java.io.IOException;

import javax.swing.SwingWorker;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadWorker extends SwingWorker<String, String> implements ProgressListener {
	
	public static final String REAL_PROGRESS = "real_progress";
	
	private float progress;
	private String url;
	private File out;
	private String tag;	
	private CallbackReceiver receiver;
	
	
	
	public DownloadWorker(String url, File out, String tag, CallbackReceiver receiver) {
		this.url = url;
		this.out = out;
		this.tag = tag;
		this.receiver = receiver;
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
			resp = client.newCall(req).execute();
			if(!resp.isSuccessful()) {
				throw new IOException("Error Happened: "+resp.message());
			}
			FileUtils.copyInputStreamToFile(resp.body().byteStream(), out);
		} finally {
			IOUtils.closeQuietly(resp);
		}		
		return "success";
	}

	@Override
	public void update(long bytesRead, long contentLength) {
		float oldProgress = progress;
		progress = (bytesRead * 100.0f) / contentLength;
		firePropertyChange(REAL_PROGRESS, oldProgress, progress);
	}
	
	@Override
	protected void done() {
		try {
			receiver.receiveData(get(), tag);
		} catch (Exception e) {
			receiver.receiveData(e, tag);
		}
	}

}
