package com.pluralsight.rxjava2.module6;

import com.pluralsight.rxjava2.nitrite.NitriteSchema;
import com.pluralsight.rxjava2.nitrite.NitriteTestDatabase;
import com.pluralsight.rxjava2.nitrite.dataaccess.FibonacciNumberDataAccess;
import com.pluralsight.rxjava2.nitrite.datasets.NitriteFibonacciSequenceSchema;
import com.pluralsight.rxjava2.utility.GateBasedSynchronization;
import com.pluralsight.rxjava2.utility.ThreadHelper;
import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.schedulers.Schedulers;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class DatabaseFlowableExample2 {

    private final static Logger log = LoggerFactory.getLogger(DatabaseFlowableExample2.class);

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

                // We makeObservable an Flowable<Long> that will emit our Fibonacci
                // numbers using the Nitrite database.  A flowable will allow
                // the subscribers to control the speed at which the Fibonacci
                // sequence is emitted.
                Flowable<Long> fibonacciObservable = FibonacciNumberDataAccess
                        .selectAsFlowable(testDatabase.getNitriteDatabase())

                        // Observe on the computation thread, don't delay errors, and use
                        // a buffer size of 3 items with the flowable.
                        .observeOn(Schedulers.computation(), false, 3)

                        // Perform the subscription (the work against the database), on the
                        // io thread pool.
                        .subscribeOn(Schedulers.io());

                // Since we don't have a DemoSubscriber that works with Flowable, we are
                // going to do it inline.  We makeObservable a FlowableSubscriber...
                fibonacciObservable.subscribe(new FlowableSubscriber<>() {

                    // We makeObservable a counter that we can use to count messages.
                    private AtomicInteger counter = new AtomicInteger();

                    // Keep track of our subscription so we can dispose if we
                    // need to and so we can ask for events in our FlowableSubscriber.
                    private Subscription subscription;

                    // onSubscribe, capture the subscription and...
                    @Override
                    public void onSubscribe(Subscription s) {
                        this.subscription = s;

                        // Request 3 messages for starters.  Without this,
                        // the Flowable will hang waiting for the subscriber to tell
                        // it how many messages it's ready to process.
                        this.subscription.request(3);
                    }

                    // Handle an onNext message...
                    @Override
                    public void onNext(Long nextNumber) {

                        // Sleep for 10 milliseconds to slow things down like
                        // our previous example.
                        ThreadHelper.sleep(10, TimeUnit.MILLISECONDS);

                        // Increment our counter.  Every 3rd event,
                        // ask for 3 more.
                        if(counter.incrementAndGet() % 3 == 0 ) {

                            // Request 3 more events through the subscription
                            this.subscription.request(3);
                        }

                        log.info("Next Fibonacci Number: {}", nextNumber);
                    }

                    @Override
                    public void onError(Throwable t) {
                        log.error(t.getMessage(),t);
                        gate.openGate("onError");
                    }

                    @Override
                    public void onComplete() {
                        log.info( "onComplete" );
                        gate.openGate("onComplete");
                    }
                });

                gate.waitForAny("onError", "onComplete");
            }
        }
        catch( Throwable t ) {
            log.error(t.getMessage(),t);
        }
    }
}
