package com.company.springreactive.techie;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
public class MonoFluxTest {

    // Mono returns 0 or 1 result.
    @Test
    public void testMono() {
        // monoString is publisher.
        Mono<?> monoString = Mono.just("javatechie")
                .then(Mono.error(new RuntimeException("Exception occured")))
                .log();
        // subscribe(event) - emit event
        monoString.subscribe(System.out::println,
                (e) -> log.error("e.getMessage(): " + e.getMessage()));
    }

    // Flux returns Multiple results.
    @Test
    public void testFlux() {
        Flux<String> fluxString = Flux.just("Spring", "Spring Boot", "Hibernate", "MicroService")
                .concatWithValues("AWS")
                .concatWith(Flux.error(new RuntimeException("Exception occured"))) // it makes flux stream stop
                .concatWithValues("Cloud") // so this method does not execute
                .log();
        fluxString.subscribe(System.out::println,
                (e) -> log.error("e.getMessage(): " + e.getMessage()));
    }
}
