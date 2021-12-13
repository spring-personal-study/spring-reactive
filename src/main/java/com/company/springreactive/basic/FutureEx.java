package com.company.springreactive.basic;

import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.SuccessCallback;

import java.util.Objects;
import java.util.concurrent.*;

public class FutureEx {

    interface SuccessCallback<T> {
        void onSuccess(T result);
    }

    interface ExceptionCallback {
        void onError(Throwable throwable);
    }

    public static class CallbackFutureTask extends FutureTask<String> {

        final SuccessCallback<String> sc;
        final ExceptionCallback ec;

        public CallbackFutureTask(Callable<String> callable, SuccessCallback<String> sc, ExceptionCallback ec) {
            super(callable);
            this.sc = Objects.requireNonNull(sc);
            this.ec = Objects.requireNonNull(ec);
        }

        @Override
        protected void done() { // 비동기 작업이 성공적으로 완료되면 실행
            try {
                sc.onSuccess(get()); // Hello 출력 (System.out::println)
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                ec.onError(e.getCause()); // 에러 출력: (Error: e.getMessage())
            }
        }
    }

    public static void main(String... args) {
        ExecutorService es = Executors.newCachedThreadPool();

        CallbackFutureTask f = new CallbackFutureTask(() -> {
            Thread.sleep(2000);
            System.out.println("Async");
            // 주석 해제시 ec.onError(e.getCause()) 수행됨
            //if (1 == 1) throw new RuntimeException("async 작업 중 에러가 발생했습니다.");
            return "Hello";
        }, System.out::println,
           e -> System.out.println("Error: " + e.getMessage()));

        es.execute(f);
        System.out.println("Exit");
        es.shutdown();
    }
    /*
     * if (1 == 1) 주석 해제시 출력
     * Exit
     * Async
     * Error: async 작업 중 에러가 발생했습니다.
     *
     * 주석처리시 출력
     * Exit
     * Async
     * Hello
     */
}
