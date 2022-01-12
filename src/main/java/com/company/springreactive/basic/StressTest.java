package com.company.springreactive.basic;

import io.reactivex.rxjava3.core.Observable;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class StressTest {


    public static void main(String[] args) {

        Runnable r = () -> {
            HttpClient client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();
            try {
                for (int i = 0; i < 10000; i++) {
                    String result =
                            client.sendAsync(HttpRequest.newBuilder(
                                            new URI("http://www.jeom.shop")).GET().build(), HttpResponse.BodyHandlers.ofString())
                                    .thenApply(HttpResponse::body)
                                    .get();
                    if (i % 5 == 0) {
                        System.out.println(result);
                    }
                }

            } catch (URISyntaxException | ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        };

        List<Runnable> list = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            list.add(r);
        }

        list.stream()
                .parallel()
                .forEach(Runnable::run);

    }

}
