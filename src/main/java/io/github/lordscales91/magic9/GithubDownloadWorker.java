package io.github.lordscales91.magic9;

import io.github.lordscales91.magic9.core.CallbackReceiver;
import io.github.lordscales91.magic9.core.MagicUtils;
import io.github.lordscales91.magic9.http.ProgressListener;
import io.github.lordscales91.magic9.http.ProgressResponseBody;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class GithubDownloadWorker extends MagicWorker implements ProgressListener {
	
	public static final String GITHUB_API_BASE_URL = "https://api.github.com";
	public static final String GITHUB_API_VERSION_HEADER = "application/vnd.github.v3+json";
	public static final String REAL_PROGRESS = "real_progress";

	private String releaseUrl;
	private String assetExtension;
	private float progress;
	private File out;
	private Call assetCall;
	private Call dlproc;
	
	

	public GithubDownloadWorker(String releaseUrl, String assetExtension,
			File out, String tag, CallbackReceiver receiver) {
		super(tag, receiver);
		this.releaseUrl = releaseUrl;
		this.assetExtension = assetExtension;
		this.out = out;
	}

	@Override
	protected String doInBackground() throws Exception {
		OkHttpClient client = new OkHttpClient.Builder().build();
		Request req = new Request.Builder()
			.header("Accept", GITHUB_API_VERSION_HEADER)
			.url(getApiUrl()).build();
		Response resp = null;
		URL assetUrl = null;
		assetCall = client.newCall(req);
		try {
			resp = assetCall.execute();
			if(!resp.isSuccessful()) {
				throw new IOException("Error Happened: "+resp.message());
			}
			assetUrl = getAssetUrl(resp);
		} finally {
			IOUtils.closeQuietly(resp); // Close the API response
		}
		
		if(assetUrl == null) { // This shouldn't happen
			throw new IOException("Unkown Error");
		}
		// And now start a new request to actually download the asset
		req = new Request.Builder().url(assetUrl).build();
		// build s new client to track the download progress
		final ProgressListener progressListener = this;
		client = new OkHttpClient.Builder().addNetworkInterceptor(new Interceptor() {
			
			@Override
			public Response intercept(Chain chain) throws IOException {
				Response originalResponse = chain.proceed(chain.request());
	            return originalResponse.newBuilder()
	                .body(new ProgressResponseBody(originalResponse.body(), progressListener))
	                .build();
			}
		}).build();
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

	private URL getAssetUrl(Response resp) throws IOException {
		URL result = null;
		// Parse the JSON response
		InputStream in = resp.body().byteStream();
		ObjectMapper mp = new ObjectMapper();
		JsonNode obj = mp.readTree(in);
		ArrayNode assets = (ArrayNode)obj.get("assets");
		JsonNode targetAsset = null;
		for(int i=0;i<assets.size();i++) {
			JsonNode asset = assets.get(i);
			if(asset.get("name").asText().endsWith(assetExtension)) {
				targetAsset = asset;
				i = assets.size(); // break loop
			}
		}
		if(targetAsset == null) {
			throw new IOException("Couldn't find a valid asset in the release");
		}
		// We have the right asset at this point, let's return the URL
		result = new URL(targetAsset.get("browser_download_url").asText());
		return result;
	}

	private URL getApiUrl() throws IOException {
		URL result = null;
		try {
			URL url = new URL(releaseUrl);
			String[] paths = url.getPath().split("/");
			String user = paths[1]; // Paths starts with "/"
			String repo = paths[2];
			result = new URL(GITHUB_API_BASE_URL+"/repos/"+user+"/"+repo+"/releases/latest");
		} catch (MalformedURLException e) {
			throw new IOException("Release URL couldn't be resolved", e);
		}
		return result;
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
		if(assetCall != null) {
			assetCall.cancel();
		}
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
