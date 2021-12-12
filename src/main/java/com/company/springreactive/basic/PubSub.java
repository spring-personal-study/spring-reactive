package com.company.springreactive.basic;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;

/**
 * Subscriber 는 onSubscribe(Subscription) 메서드를 작성하여 특정 Publisher 를 구독할지 설정할 수 있다.
 * (작성만 하는 것이고, 실제 구독은 onSubscribe 메서드가 호출될 때(다시 말해 Publisher.subscribe()가 호출될 때) 시작됨)
 * Publisher 는 Subscription 객체를 만들고 Subscriber 의 onSubscribe 메서드를 호출할 때 Subscription 객체를 전달하고 추후에 Publisher.subscribe()를 호출함으로서
 * Subscriber 가 실제로 자신(Publisher)를 구독할 수 있도록 만든다.
 * 추후에 Publisher 에서 이벤트 발생 또는 값이 생성되면 Publisher 는 Subscriber 에게 바로 이 변경이력을 전달하게 된다. <br><br>
 * Subscription 객체는 Subscriber 가 Publisher 로부터 값을 어느정도만큼씩 전달받을지 제어하도록 고안되었다.
 * 이 제어가 필요한 이유는 백프레셔(역압) 효과 때문인데, 백프레셔란 Publisher 가 생성하는 값이 Subscriber 쪽의 소비보다 더 크게 되는 문제를 말한다.
 * 다시 말해 값을 소비하는 측(Subscriber)의 처리 속도가 Publisher 가 값을 생산해서 보내주는 속도보다 느리게 되는 경우를 말한다.
 * 소비가 상대적으로 느리기 떄문에 생산되는 값이 계속해서 메모리 어딘가에 쌓이거나 소실될 위험이 있으므로 좋지 않다.
 * 반대의 경우(소비하는 측이 생산하는 측보다 속도가 빠른 경우)도 백프레셔 효과라고 말할 수 있으며, 마찬가지로 좋지 않다.
 * 생산(Publisher)과 소비(Subscriber)을 적절히 조율할 필요성이 있으므로 Subscription 객체가 필요하다.
 */
public class PubSub {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService es = Executors.newSingleThreadExecutor();

        Iterable<Integer> iter = List.of(1, 2, 3, 4, 5); // DB 에서 가져온 어떤 컬렉션 데이터
        Flow.Publisher<Integer> pub = (subscriber) -> subscriber.onSubscribe(new Subscription1(es, subscriber, iter.iterator()));
        Subscriber1<Integer> sub = new Subscriber1<>();
        pub.subscribe(sub);
        es.shutdown();
    }

    static class Subscriber1<T> implements Flow.Subscriber<T> {

        Flow.Subscription subscription;
        final int bufferSize = 2;

        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            System.out.println("onSubscribe - " + subscription.getClass().getSimpleName());
            this.subscription = subscription;
            this.subscription.request(1L); // Publisher 가 생산하는 값 중 1개만 일단 먼저 받고 싶다.
        } // 반드시 호출되어야 하는 함수. publisher 쪽에서 subscribe() 를 호출할 때 subscribe 안에서 onSubscribe() 를 호출하도록 설정해야 한다.

        @Override
        public void onNext(Object item) {
            System.out.println("onNext - " + item);
            this.subscription.request(1L);
        } // Publisher 가 데이터를 주면 onNext 가 받는다. (Observer 객체의 update 와 같다. 단, Observer 에는 에러처리 및 데이터 전체 수신 완료를 제어하는 기능이 없다.)

        @Override
        public void onError(Throwable throwable) {
            System.out.println("onError - " + throwable.getMessage());
        } // Subscriber 에만 있음. 데이터 전달과정에서 에러가 발생하면 Publisher 에서 바로 처리하지 말고, onError 라는 메서드를 통해서 Subscriber 에게 전달해달라는 형태로 사용한다.

        @Override
        public void onComplete() {
            System.out.println("onComplete - 완료");
        } // Subscriber 에만 있음. 모든 데이터가 전달되면
    }

    static class Subscription1 implements Flow.Subscription {

        final Flow.Subscriber<? super Integer> subscriber;
        final Iterator<? extends Integer> iterator;
        final ExecutorService es;

        public Subscription1(ExecutorService es, Flow.Subscriber<? super Integer> subscriber, Iterator<? extends Integer> iterator) {
            this.es = es;
            this.subscriber = subscriber;
            this.iterator = iterator;
        }

        @Override
        public void request(long n) {
              while (n-- > 0) {
                try {
                    if (iterator.hasNext()) {
                        subscriber.onNext(iterator.next());
                    } else {
                        subscriber.onComplete();
                        break;
                    }
                } catch (RuntimeException e) {
                    subscriber.onError(e);
                }
            }
        } // publisher 가 push 한 데이터 중 몇 개까지 받을지 제어하는 메서드 (long.maximum 설정하면 전부 받게 된다.)
        // subscriber 가 request 메서드를 실행해도 publisher 쪽에서 추가 이벤트 또는 추가 값 생성이 되지 않았다면 값을 전달받지 못할 수 있다.

        @Override
        public void cancel() {

        }
    }
}


