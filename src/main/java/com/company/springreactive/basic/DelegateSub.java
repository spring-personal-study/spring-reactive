package com.company.springreactive.basic;

import java.util.concurrent.Flow;

public abstract class DelegateSub<T, R> implements Flow.Subscriber<T> {

    private final Flow.Subscriber<? super T> sub;

    public DelegateSub(Flow.Subscriber<? super T> sub) {
        this.sub = sub;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        sub.onSubscribe(subscription);
    }

    @Override
    public void onNext(T t) {
        sub.onNext(t);
    }

    @Override
    public void onError(Throwable throwable) {
        sub.onError(throwable);
    }

    @Override
    public void onComplete() {
        sub.onComplete();
    }
}
