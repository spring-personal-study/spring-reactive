package com.company.springreactive.basic;

import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

// jdk 1.8 이전 버전으로 작성된 iterable 과 observable 비교 예시문

// reactive programming.
// Iterable <---> Observable
public class IterableVsObservable {

    // iterable 과 observable(reactive) 은 하는 일이 똑같다. 그러나 수행 순서는 반대로 동작한다.
    public static void main(String[] args) {
        iterable();  // pull 방식. (값은 이미 있고 어딘가에서 이 값을 가져와서 사용하는 방식)
        observable(); // push 방식 (observable 이 값을 만들어내고, 이 값을 observer 에게 push 하는 형태)
    }

    // iterable 특징: pull (콜드 소스를 가져와서(pull) 재사용). 콜드 소스: (이미 정해진, 변경되지 않는 어떤 값들을 통칭)
    // 데이터 값의 끝을 알기 쉬움 (hasNext()가 있으므로 추가 요청했는데 더이상 값을 전달하지 않으면 끝이라는 것을 알 수 있음)
    public static void iterable() {
        Iterable<Integer> iter = () ->
                new Iterator<>() {
                    int i = 0;
                    final static int MAX = 10;

                    @Override
                    public boolean hasNext() {
                        return i < MAX;
                    }

                    @Override
                    public Integer next() {
                        return ++i;
                    }
                };
        System.out.println("=== iterable 방식 ===");

        for (Iterator<Integer> it = iter.iterator(); it.hasNext();) {
            System.out.print(it.next() + " ");
        }

        System.out.println("\n");
    }

    // observable 특징: push (값을 생성하여 이 값을 다른 곳에 밀어넣어 사용하게 하는 방법)
    // 즉 Source(1)에서 -> Event/Data 발생시 -> Observer(N)에게 전달 (Observable(1) : Observer(N))
    // 데이터 값의 끝을 알기 어려움 (이벤트 변경 감지는 했지만 이벤트가 추후에 얼마나 더 변경될지 알 수가 없음)
    private static void observable() {
        //Observer 객체. // Observable 이 만들어낸 값을 받는 객체.
        System.out.println("=== observable 방식 ===");
        Observer ob = (o, arg) -> System.out.print(arg + " ");
        IntObservable intObservable = new IntObservable();
        intObservable.addObserver(ob); // Observable 이 만들어낸 값을 구독할 객체(Observer) 등록
        intObservable.run();
        System.out.println();
    }

    // 값을 만들어내는 Observable. // 1.8 이하 버전에서 사용되던 Observable 구현 객체.
    static class IntObservable extends Observable implements Runnable { // 비동기를 위한 Runnable 도 구현
         @Override
         public void run() {
             for (int i = 1; i <= 10; i++) {
                 setChanged();
                 notifyObservers(i); // int i = it.next()에 대응. push
             }
         }
    }

    // 요약
    /*
        // iterable 방식
        Data method() {
          while (data.hasNext()) {
              return data.next()
          }
        }

        // observable 방식
        void observerMethod(Data data1) {
          while (data1.hasNext()) {
              data1.get(); // observable 이 만들어낸 값을 가져온다.
          }
        }
     */
}
