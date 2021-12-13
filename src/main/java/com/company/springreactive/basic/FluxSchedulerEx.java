package com.company.springreactive.basic;

import io.reactivex.rxjava3.core.Flowable;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

/**
 * spring-web-flux example
 * @date 2021-12-12
 * @author Informix
 */
@Slf4j
public class FluxSchedulerEx {

    public static void main(String[] args) {
        Disposable sub1 = Flux.range(1, 10)
                .publishOn(Schedulers.newSingle("publisher"))
                .log()
                .subscribeOn(Schedulers.newSingle("subcriber"))
                .subscribe(e -> log.info(e + " times scheduler run."));

        log.info("Exit");

        sub1.dispose();

        Disposable subscribe = Flux.interval(Duration.ofMillis(500))
                .subscribe(e -> log.info("onNext: {}", e));

        subscribe.dispose();


    }
}
