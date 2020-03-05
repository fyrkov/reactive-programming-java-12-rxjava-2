package com.pluralsight.rxjava2.module2.slideExamples;

import io.reactivex.Observable;

public class HighLevelLifecycle {

    public static void main(String[] args) {

        Observable<Integer> exampleObservable = Observable.just(1,2,3);
        exampleObservable.subscribe(System.out::println);
        System.out.println("---------");
        exampleObservable.subscribe(System.out::println);
        System.out.println("---------");

    }
}
