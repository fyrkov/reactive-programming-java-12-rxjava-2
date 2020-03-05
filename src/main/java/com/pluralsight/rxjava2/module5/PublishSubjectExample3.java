package com.pluralsight.rxjava2.module5;

import com.pluralsight.rxjava2.utility.GateBasedSynchronization;
import com.pluralsight.rxjava2.utility.ThreadHelper;
import com.pluralsight.rxjava2.utility.datasets.GreekAlphabet;
import com.pluralsight.rxjava2.utility.subscribers.DemoSubscriber;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class PublishSubjectExample3 {

    private final static Logger log = LoggerFactory.getLogger(PublishSubjectExample3.class);

    public static void main(String[] args) {

        GateBasedSynchronization gate = new GateBasedSynchronization();

        // Create a Subject of type String.  Note that Subjects must be
        // created using their "create" method and not a constructor.
        Subject<String> publishSubscribeSubject = PublishSubject.create();

        // Create an Observable that emits the English form of the Greek alphabet.
        GreekAlphabet.greekAlphabetInEnglishObservable()

                // Process on the computation thread pool.
                .subscribeOn(Schedulers.computation())

                // Subscribe to the publishSubjectObservable.
                .subscribe(

                        // onNext handler...
                        nextLetter -> {

                            // For each letter emitted, pass it along to the PublishSubject
                            publishSubscribeSubject.onNext(nextLetter);

                            // Slow things down be 250 milliseconds
                            ThreadHelper.sleep(250, TimeUnit.MILLISECONDS);
                        },

                        // onError handler...
                        throwable -> {log.error(throwable.getMessage(), throwable); gate.openGate("onError");},

                        // onComplete Handler...
                        () -> gate.openGate("onComplete")
                );

        // Create a simple subscriber that waits until it sees "eta".
        // Once it sees "eta" it will open a gate called "Proceed".
        publishSubscribeSubject

                // Process on the computation scheduler.
                .subscribeOn(Schedulers.computation())

                // Subscriber using our simple onNext handler that looks for
                // "eta".
                .subscribe(nextEvent -> {
                    log.info("onNext - {}", nextEvent);

                    // Once we see "eta"...
                    if( nextEvent.equals("eta")) {

                        // ...open the synchronization gate named "Proceed"
                        gate.openGate("Proceed");
                    }
        });

        // Cause the main thread to pause until it sees "Proceed"
        gate.waitForAny("Proceed");

        // Subscribe a new DemoSubscriber to the PublishSubject.
        publishSubscribeSubject

                // Subscribe on the computation thread scheduler.
                .subscribeOn(Schedulers.computation())

                // Subscribe a new DemoSubscriber.
                .subscribe(new DemoSubscriber<>(gate));

        // Wait for the stream to complete (all Greek letters emitted)
        gate.waitForAny("onComplete", "onError");

        System.exit(0);
    }
}
