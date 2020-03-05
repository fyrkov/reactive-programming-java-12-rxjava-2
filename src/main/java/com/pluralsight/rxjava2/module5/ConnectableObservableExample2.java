package com.pluralsight.rxjava2.module5;

import com.pluralsight.rxjava2.utility.ThreadHelper;
import com.pluralsight.rxjava2.utility.subscribers.DemoSubscriber;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class ConnectableObservableExample2 {

    private final static Logger log = LoggerFactory.getLogger(ConnectableObservableExample2.class);

    public static void main(String[] args) {

        // Create an Observable that emits every 100 milliseconds.
        Observable<Long> intervalSequence = Observable.interval(100, TimeUnit.MILLISECONDS)

                // ...process it on the computation scheduler
                .subscribeOn(Schedulers.computation())

                // Log each time the interval emits
                .doOnNext( nextLong -> log.info("doOnNext - {}", nextLong))

                // Publish to make it a ConnectableObservable.
                .publish()

                // Call refCount to make it track the number of Subscribers and
                // stop emitting when no one is listening.
                .refCount();

        // Create the two DemoSubscribers we will use.
        DemoSubscriber<Long> demoSubscriber1 = new DemoSubscriber<>();
        DemoSubscriber<Long> demoSubscriber2 = new DemoSubscriber<>();

        // Have both DemoSubscribers subscribe to the interval sequence.
        // This will start the flow of events.
        intervalSequence.subscribe(demoSubscriber1);
        intervalSequence.subscribe(demoSubscriber2);

        // Allow things to happen for 2 seconds.
        ThreadHelper.sleep(2, TimeUnit.SECONDS);

        // Dispose of the first subscriber.  Notice that the
        // events continue to flow.
        demoSubscriber1.dispose();

        // Wait another 2 seconds
        ThreadHelper.sleep(2 , TimeUnit.SECONDS);

        // Dispose of the second subscriber.  Notice that the
        // events stop flowing
        demoSubscriber2.dispose();

        // Wait for another 2 seconds and emit a message
        // so we see that no events are flowing.
        log.info( "Pausing for 2 seconds...");
        ThreadHelper.sleep(2, TimeUnit.SECONDS);
        log.info( "...pause complete");

        ThreadHelper.sleep(2, TimeUnit.SECONDS);

        System.exit(0);
    }
}
