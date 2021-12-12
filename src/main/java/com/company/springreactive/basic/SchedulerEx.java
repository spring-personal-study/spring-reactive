package com.company.springreactive.basic;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.Collections;
import java.util.List;

// Reactive Streams - Scheduler

/**
 * Schedulers.computation() 을 이용해서 계산 작업을 처리하는 예제
 */
public class SchedulerEx {
    public static void main(String[] args) {
        Observable<Integer> observable1 = Observable.fromIterable(List.of(30, 40, 50, 60, 70,
                                                                    80, 90, 100, 110, 120, 130,
                                                                    140, 150, 160, 170, 180,
                                                                    190, 200, 210, 220, 230, 240, 250, 260,
                                                                    270, 280, 290, 300));
        Observable<Integer> observable2 = Observable.range(1, 24);

        Observable<Object> source = Observable.zip(observable1, observable2, (data1, hour) -> hour + "시: " + Collections.max(List.of(data1)));

        source.subscribeOn(Schedulers.computation())
                .subscribe(System.out::println);

        source.subscribe();
        //TimeUtil.sleep(500L);
    }
}