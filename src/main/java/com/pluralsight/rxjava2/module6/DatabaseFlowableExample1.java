package com.pluralsight.rxjava2.module6;

import com.pluralsight.rxjava2.nitrite.NitriteSchema;
import com.pluralsight.rxjava2.nitrite.NitriteTestDatabase;
import com.pluralsight.rxjava2.nitrite.dataaccess.FibonacciNumberDataAccess;
import com.pluralsight.rxjava2.nitrite.datasets.NitriteFibonacciSequenceSchema;
import com.pluralsight.rxjava2.utility.GateBasedSynchronization;
import com.pluralsight.rxjava2.utility.subscribers.DemoSubscriber;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class DatabaseFlowableExample1 {

    private final static Logger log = LoggerFactory.getLogger(DatabaseFlowableExample1.class);

    public static void main(String[] args) {

        GateBasedSynchronization gate = new GateBasedSynchronization();

        try {
            // Create a schema object that we will apply to our test
            // database.  In this case, it will makeObservable a collection that
            // contains the Fibonacci sequence.
            NitriteSchema schema = new NitriteFibonacciSequenceSchema();

            // Using a NitriteTestDatabase, initialized with our Fibonacci sequence
            // of numbers.
            try (NitriteTestDatabase testDatabase = new NitriteTestDatabase(Optional.of(schema))) {

                // We makeObservable an Observable<Long> that will emit our Fibonacci
                // numbers using the Nitrite database.
                Observable<Long> fibonacciObservable = FibonacciNumberDataAccess
                        .selectAsObservable(testDatabase.getNitriteDatabase())

                        // Observe events on the computation scheduler.
                        .observeOn(Schedulers.computation())

                        // ..but perform the act of interacting with the database
                        .subscribeOn(Schedulers.io());

                // Set up our DemoSubscriber with a 10 millisecond delay when
                // processing each event.
                fibonacciObservable.subscribe(new DemoSubscriber<>(
                        10, TimeUnit.MILLISECONDS, gate,
                        "onError", "onComplete"));

                gate.waitForAny("onError", "onComplete");
            }
        }
        catch( Throwable t ) {
            log.error(t.getMessage(),t);
        }
    }
}
