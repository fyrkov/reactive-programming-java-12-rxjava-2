package com.pluralsight.rxjava2.module4;

import com.pluralsight.rxjava2.utility.GateBasedSynchronization;
import com.pluralsight.rxjava2.utility.datasets.FibonacciSequence;
import com.pluralsight.rxjava2.utility.subscribers.DemoSubscriber;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubscribeOnObserveOnExample1 {

    private final static Logger log = LoggerFactory.getLogger(SubscribeOnObserveOnExample1.class);

    public static void main(String[] args) {

        GateBasedSynchronization gate = new GateBasedSynchronization();

        // Our base observable for this example will be a FibonacciSequence with 10 numbers.
        Observable<Long> fibonacciObservable = FibonacciSequence.create(10)
                .doOnSubscribe( disposable -> {
                    log.info("fibonacciObservable::onSubscribe");
                });

        // -----------------------------------------------------------------------------------------

        // First, let's look at subscription with no threading modification.
        fibonacciObservable.subscribe(new DemoSubscriber<>(gate));

        // No threading, but do our synchronization pattern anyway.
        gate.waitForAny("onError", "onComplete");
        log.info("--------------------------------------------------------");

        // -----------------------------------------------------------------------------------------
        // Scan the numbers on the computation thread pool
        // -----------------------------------------------------------------------------------------

        gate.resetAll();

        // SubscribeOn example illustrating how first SubscribeOn wins.
        fibonacciObservable
                .subscribeOn(Schedulers.computation())
                .subscribeOn(Schedulers.io()) // This will be ignored.  subscribeOn is always first come, first served.
                .subscribe(new DemoSubscriber<>(gate));

        // No threading, but do our synchronization pattern anyway.
        gate.waitForAny("onError", "onComplete");
        log.info("--------------------------------------------------------");

        // -----------------------------------------------------------------------------------------
        // Illustrate how observeOn's position effects which scheduler is used.
        // -----------------------------------------------------------------------------------------

        gate.resetAll();

        // Illustrate how observeOn's position alters the scheduler that is
        // used for the observation portion of the code.
        fibonacciObservable
                // First observeOn...will be altered by the
                // observeOn further downstream.
                .observeOn(Schedulers.computation())

                // The location of subscribeOn doesn't matter.
                // First subscribeOn always wins.
                .subscribeOn(Schedulers.newThread())

                // the last observeOn takes precedence.
                .observeOn(Schedulers.io())

                .subscribe(new DemoSubscriber<>(gate));

        // No threading, but do our synchronization pattern anyway.
        gate.waitForAny("onError", "onComplete");
        log.info("--------------------------------------------------------");

        System.exit(0);
    }
}
