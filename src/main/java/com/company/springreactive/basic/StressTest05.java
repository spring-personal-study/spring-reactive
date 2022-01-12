package com.company.springreactive.basic;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class StressTest05 implements Runnable {
    private int rowNum;

    public StressTest05(int rowNum) {
        this.rowNum = rowNum;
    }

    @Override
    public void run() {

        for (int i = 0; i < 1_000_000; i++) {

            try {
                HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();

                String result = client.sendAsync(
                                HttpRequest.newBuilder(
                                        new URI("https://server.wearedj.club")).GET().build(),  //GET방식 요청
                                HttpResponse.BodyHandlers.ofString()  //응답은 문자형태
                        ).thenApply(HttpResponse::body)  //thenApply메소드로 응답body값만 받기
                        .get();  //get메소드로 body값의 문자를 확인
                System.out.println(this.rowNum + " " + result);

            } catch (URISyntaxException | ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    public static void main(String[] args) {

        List<Thread> list = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            list.add(new Thread(new StressTest05(i)));
        }

        list.stream()
                .parallel()
                .forEach(Thread::start);

    }
}
