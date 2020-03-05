package com.pluralsight.rxjava2.module3;

import com.pluralsight.rxjava2.utility.datasets.FibonacciSequence;
import com.pluralsight.rxjava2.utility.subscribers.MaybeDemoSubscriber;
import com.pluralsight.rxjava2.utility.subscribers.SingleDemoSubscriber;
import io.reactivex.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PositionalExample1 {

    private final static Logger log = LoggerFactory.getLogger(PositionalExample1.class);

    public static void main(String[] args) {

        // Create a Fibonacci sequence
        Observable<Long> numberSequence = FibonacciSequence.create(10);

        // Demonstrate the "first" operator be emitting on the first
        // number in the sequence which should be zero.
        log.info("first Example");
        numberSequence
                .first(99999L)
                .subscribe(new SingleDemoSubscriber<>());

        log.info("");
        log.info( "----------------------------------------------------------------------------");
        log.info("");

        // Demonstrate the "first" operator on an Observable that has no items.
        // The default value should be emitted.
        log.info("first with default Example");
        Observable.empty()
                .first(99999)
                .subscribe(new SingleDemoSubscriber<>());

        log.info("");
        log.info( "----------------------------------------------------------------------------");
        log.info("");

        // Demonstrate the "firstOrError" operator on an Observable that has no items.
        // An error should be emitted.
        log.info("firstOrError Example");
        Observable.empty()
                .firstOrError()
                .subscribe(new SingleDemoSubscriber<>());

        log.info("");
        log.info( "----------------------------------------------------------------------------");
        log.info("");

        // Demonstrate the "firstElement" operator.
        // Zero should be emitted
        log.info("firstElement Example");
        numberSequence
                .firstElement()
                .subscribe(new MaybeDemoSubscriber<>());

        log.info("");
        log.info( "----------------------------------------------------------------------------");
        log.info("");

        // Demonstrate the "firstElement" operator with an empty stream.
        // Only an onComplete should be emitted.
        log.info("firstElement with empty stream Example");
        Observable.empty()
                .firstElement()
                .subscribe(new MaybeDemoSubscriber<>());

        log.info("");
        log.info( "----------------------------------------------------------------------------");
        log.info("");

    }
}
