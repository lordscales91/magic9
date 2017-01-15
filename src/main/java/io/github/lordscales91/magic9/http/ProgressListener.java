package io.github.lordscales91.magic9.http;

public interface ProgressListener {
    void update(long bytesRead, long contentLength);
  }