package com.pluralsight.rxjava2.module3;

import com.pluralsight.rxjava2.utility.datasets.FibonacciSequence;
import com.pluralsight.rxjava2.utility.subscribers.SingleDemoSubscriber;
import io.reactivex.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PositionalExample2 {

    private final static Logger log = LoggerFactory.getLogger(PositionalExample2.class);

    public static void main(String[] args) {

        // Create a Fibonacci sequence 10 numbers long
        Observable<Long> numberSequence = FibonacciSequence.create(10);

        // Observe the last number in the sequence.
        // Should be 55.
        log.info( "last Example");
        numberSequence
                .last(99999L)
                .subscribe(new SingleDemoSubscriber<>());

        log.info("");
        log.info( "----------------------------------------------------------------------------");
        log.info("");

        // Observe the last number in an empty sequence
        // Should be 9999.
        log.info( "last with empty stream Example");
        Observable.empty()
                .last(99999L)
                .subscribe(new SingleDemoSubscriber<>());

        log.info("");
        log.info( "----------------------------------------------------------------------------");
        log.info("");

        // Observe the last number in the sequence, or emit an error.
        log.info( "lastOrError Example");
        Observable.empty()
                .lastOrError()
                .subscribe(new SingleDemoSubscriber<>());

        log.info("");
        log.info( "----------------------------------------------------------------------------");
        log.info("");

        // Observe the 5th number in the sequence.
        // Should be 8.
        log.info( "elementAt Example");
        numberSequence
                .elementAt(5, 99999L)
                .subscribe(new SingleDemoSubscriber<>());

        log.info("");
        log.info( "----------------------------------------------------------------------------");
        log.info("");

        System.exit(0);
    }
}
