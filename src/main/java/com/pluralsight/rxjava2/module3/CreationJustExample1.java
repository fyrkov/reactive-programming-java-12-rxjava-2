package com.pluralsight.rxjava2.module3;

import com.pluralsight.rxjava2.utility.subscribers.DemoSubscriber;
import io.reactivex.Observable;

public class CreationJustExample1 {

    public static void main(String[] args) {

        // "just" allows for the creation of Observables from single
        // values.
        Observable justObservable = Observable.just(42);

        // Output the single value.
        justObservable.subscribe(new DemoSubscriber());

        System.exit(0);
    }
}
