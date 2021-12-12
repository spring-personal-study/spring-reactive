package com.company.springreactive.basic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;

// Reactive Streams - Scheduler
@Slf4j
public class SchedulerEx {
    public static void main(String[] args) {

        Flow.Publisher<Integer> pub = (subscriber) -> subscriber.onSubscribe(new Flow.Subscription() {
            @Override
            public void request(long n) {
                subscriber.onNext(1);
                subscriber.onNext(2);
                subscriber.onNext(3);
                subscriber.onNext(4);
                subscriber.onNext(5);
                subscriber.onComplete();
            }

            @Override
            public void cancel() {
                log.info("cancel");
            }
        });

        Flow.Publisher<Integer> subOnPub = sub -> {

            ExecutorService es = Executors.newSingleThreadExecutor(new CustomizableThreadFactory() {
                @Override
                protected String getDefaultThreadNamePrefix() {
                    return "subOn-";
                }
            });

            es.execute(() -> pub.subscribe(sub));
            es.shutdown();
        };

        Flow.Publisher<Integer> pubOnPub = sub -> {

            ExecutorService es = Executors.newSingleThreadExecutor(new CustomizableThreadFactory() {
                @Override
                protected String getDefaultThreadNamePrefix() {
                    return "pubOn -";
                }
            });

            es.execute(() -> subOnPub.subscribe(new Flow.Subscriber<>() {
                @Override
                public void onSubscribe(Flow.Subscription subscription) {
                    sub.onSubscribe(subscription);
                }

                @Override
                public void onNext(Integer item) {
                    sub.onNext(item);
                }

                @Override
                public void onError(Throwable throwable) {
                    sub.onError(throwable);
                    es.shutdown();
                }

                @Override
                public void onComplete() {
                    sub.onComplete();
                    es.shutdown();
                }
            }));
        };

        Flow.Subscriber<Integer> subscriber = new Flow.Subscriber<>() {
            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                log.info("onSubscribe");
                subscription.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer item) {
                log.info("item : " + item);
            }

            @Override
            public void onError(Throwable throwable) {
                log.info("onError");
            }

            @Override
            public void onComplete() {
                log.info("onComplete");
            }
        };

        //pub.subscribe(subscriber);
        pubOnPub.subscribe(subscriber);

    }
}
