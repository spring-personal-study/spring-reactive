package com.company.springreactive.basic;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class Gugudan {

    public static void main(String[] args) {
        preheat();
        int i = 0;
        spendTime(Gugudan::immediateFlux, ++i);
        spendTime(Gugudan::singleFlux, ++i);
    }

    private static void immediateFlux() {
        Flux.range(2, 8)
                .subscribeOn(Schedulers.immediate())
                .subscribe(i -> {
                    Flux.range(2, 8)
                            .subscribeOn(Schedulers.immediate())
                            .subscribe(j -> System.out.print(i + " x " + j + " = " + i * j + "\t"))
                                    .dispose();
                    System.out.println();
                }).dispose();
    }

    private static void singleFlux() {
        Flux.range(2, 8)
                .subscribeOn(Schedulers.single())
                .subscribe(i -> {
                    Flux.range(2, 8)
                            .subscribeOn(Schedulers.immediate())
                            .subscribe(j -> System.out.print(i + " x " + j + " = " + i * j + "\t"))
                                .dispose();
                    System.out.println();
                }).dispose();
    }

    private static void spendTime(Runnable runnable, int i) {
        long start = System.nanoTime();
        runnable.run();
        System.out.print(i + " 의 전체 수행시간은 ");
        System.out.println((System.nanoTime() - start) / 1_000_000_000.0 + " 초입니다.");
    }

    private static void preheat() {
        // TODO: 아직 예열 방법을 제대로 구현하지 못했음
        Runnable runnable = () -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        runnable.run();
    }
}