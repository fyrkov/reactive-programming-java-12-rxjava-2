package com.pluralsight.rxjava2.module3;

import com.pluralsight.rxjava2.utility.ThreadHelper;
import com.pluralsight.rxjava2.utility.datasets.GreekAlphabet;
import com.pluralsight.rxjava2.utility.subscribers.DemoSubscriber;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class SampleExample1 {

    private final static Logger log = LoggerFactory.getLogger(SampleExample1.class);

    public static void main(String[] args) {

        // Create a repeating greek alphabet
        Observable<Long> incrementingObservable = Observable.interval(0L, 50L, TimeUnit.MILLISECONDS)

                // Like timeout, sample must use a different thread pool
                // so that it can send a message event though events
                // may be being generated on the main thread.
                .subscribeOn(Schedulers.computation())

                // Sample the stream every 2 seconds.
                .sample(100, TimeUnit.MILLISECONDS)
                ;

        // Subscribe and watch the emit happen every 2 seconds.
        incrementingObservable.subscribe(new DemoSubscriber<>());

        // Wait for 10 seconds
        ThreadHelper.sleep(10, TimeUnit.SECONDS);

        System.exit(0);
    }
}
