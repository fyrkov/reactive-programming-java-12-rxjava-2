package com.pluralsight.rxjava2.module5;

import com.pluralsight.rxjava2.utility.GateBasedSynchronization;
import com.pluralsight.rxjava2.utility.ThreadHelper;
import com.pluralsight.rxjava2.utility.datasets.FibonacciSequence;
import com.pluralsight.rxjava2.utility.subscribers.DemoSubscriber;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.schedulers.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class ConnectableObservableExample1 {

    private final static Logger log = LoggerFactory.getLogger(ConnectableObservableExample1.class);

    public static void main(String[] args) {

        GateBasedSynchronization gate1 = new GateBasedSynchronization();
        GateBasedSynchronization gate2 = new GateBasedSynchronization();

        // Create the Fibonacci sequence of numbers 20 long...
        ConnectableObservable<Long> fibonacciSequence = FibonacciSequence.create(20)
                // ...scheduled on the computation thread pool
                .subscribeOn(Schedulers.computation())
                // Call publish to turn it into a ConnectableObservable.
                .publish();

        // Create the 2 subscribers
        DemoSubscriber<Long> subscriber1 = new DemoSubscriber<>(gate1);
        DemoSubscriber<Long> subscriber2 = new DemoSubscriber<>(gate2);

        // Create 2 DemoSubscribers that are subscribed to the same sequence.
        fibonacciSequence.subscribe(subscriber1);
        fibonacciSequence.subscribe(subscriber2);

        ThreadHelper.sleep(2, TimeUnit.SECONDS);

        // Call connect to start the flow of events in the Fibonacci sequence.
        fibonacciSequence.connect();

        // Wait for both DemoSubscribers to complete.
        GateBasedSynchronization.waitMultiple( new String[] {"onComplete", "onError"}, gate1, gate2);

        System.exit(0);
    }
}
