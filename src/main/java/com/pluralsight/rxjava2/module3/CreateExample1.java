package com.pluralsight.rxjava2.module3;

import com.pluralsight.rxjava2.utility.GateBasedSynchronization;
import com.pluralsight.rxjava2.utility.subscribers.DemoSubscriber;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateExample1 {

    private final static Logger log = LoggerFactory.getLogger(CreateExample1.class);

    public static void main(String[] args) {

        log.info("makeObservable on 'main' thread");

        // Create a geometric sequence that is highly recognizable and emit it without
        // modifying the thread information.  This will cause everything to run on the
        // Java 'main' thread.
        Observable<Integer> geometricSequence1 = createGeometricSequence(1, 2, 8);
        geometricSequence1.subscribe(new DemoSubscriber<>());


        log.info("");
        log.info("-----------------------------------------------------------------------------------");
        log.info("");
        log.info("makeObservable on separate thread");

        // Create a synchronization gate
        GateBasedSynchronization gate = new GateBasedSynchronization();

        // Create a new geometric sequence.
        Observable<Integer> geometricSequence2 = createGeometricSequence(3, 3, 6);

        // This time we set subscribeOn to the RxJava computation scheduler.
        geometricSequence2
                .subscribeOn(Schedulers.computation())
                .subscribe(new DemoSubscriber<>(gate, "onComplete", "onError"));

        // Because we have work being done on a thread other than 'main',
        // we must wait for the subscriber to finish consuming the geometric stream.
        gate.waitForAny("onComplete", "onError");

        System.exit(0);
    }

    private static Observable<Integer> createGeometricSequence(final int start, final int multiplier, final int totalNumbers) {

        // Validate the incoming parameters.  No geometric multiplication based
        // sequences that start at zero.
        if( start == 0 ) {
            throw new IllegalArgumentException("start parameter must be non-zero");
        }

        // Create an observable.
        return Observable.create(emitter -> {

            // emitter provides us with the mechanism to generate the
            // onNext, onError, and onComplete events.

            int count = 0;
            int currentValue = start;

            while(count < totalNumbers) {

                // First, we make sure that the subscriber has not
                // cancelled the subscription.
                if( emitter.isDisposed()) {
                    break;
                }

                ++count;

                // Emit the currently calculated
                // value of the geometric sequence.
                emitter.onNext(currentValue);

                // Calculate the next value in the sequence.
                currentValue = currentValue * multiplier;
            }

            // If the subscription has been cancelled, then we must
            // NOT call onComplete since that would be outside of the
            // normal Observable contract.
            if( !emitter.isDisposed()) {

                // ...but in this case, the subscription is still good
                // and we are at the end of the requested number sequence.
                // Issue an onComplete event.
                emitter.onComplete();
            }
        });

    }
}
