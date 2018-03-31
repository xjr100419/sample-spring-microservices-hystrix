package pl.piomin.microservices.edge;

import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;

public class ZuulRetryListener implements RetryListener{
    @Override
    public <T, E extends Throwable> boolean open(RetryContext retryContext, RetryCallback<T, E> retryCallback) {
        return true;
    }

    @Override
    public <T, E extends Throwable> void close(RetryContext retryContext, RetryCallback<T, E> retryCallback, Throwable throwable) {
        System.out.println("%%%%%%  ZuulRetryListener close%%%%%");
    }

    @Override
    public <T, E extends Throwable> void onError(RetryContext retryContext, RetryCallback<T, E> retryCallback, Throwable throwable) {
        System.out.println("%%%%%%  ZuulRetryListener onError%%%%%");
    }
}
