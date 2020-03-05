package com.pluralsight.rxjava2.module6;

import com.pluralsight.rxjava2.nitrite.NitriteSchema;
import com.pluralsight.rxjava2.nitrite.NitriteTestDatabase;
import com.pluralsight.rxjava2.nitrite.dataaccess.FibonacciNumberDataAccess;
import com.pluralsight.rxjava2.nitrite.datasets.NitriteFibonacciSequenceSchema;
import com.pluralsight.rxjava2.utility.GateBasedSynchronization;
import com.pluralsight.rxjava2.utility.ThreadHelper;
import io.reactivex.FlowableSubscriber;
import io.reactivex.parallel.ParallelFlowable;
import io.reactivex.schedulers.Schedulers;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class DatabaseFlowableExample3 {

    private final static Logger log = LoggerFactory.getLogger(DatabaseFlowableExample3.class);
    private final static int RAIL_COUNT = 3;

    public static void main(String[] args) {

        // Since we will have 3 threads in parallel ("rails"), makeObservable 3 synchonization gate
        // collections.
        GateBasedSynchronization[] gates = new GateBasedSynchronization[RAIL_COUNT];
        IntStream.range(0,RAIL_COUNT).forEach(value -> gates[value] = new GateBasedSynchronization());

        try {

            // Create a schema object that we will apply to our test
            // database.  In this case, it will makeObservable a collection that
            // contains the Fibonacci sequence.
            NitriteSchema schema = new NitriteFibonacciSequenceSchema();

            // Using a NitriteTestDatabase, initialized with our Fibonacci sequence
            // of numbers.
            try (NitriteTestDatabase testDatabase = new NitriteTestDatabase(Optional.of(schema))) {

                // We are going to makeObservable a ParallelFlowable in order to use our flow
                // controlled observable, but process the messages in parallel.  We
                // start with our Flowable as in the last example.
                ParallelFlowable<Long> fibonacciParallelFlowable =

                        // This time we want a ParallelFlowable
                        ParallelFlowable.from(

                                // .. using the same flowable Fibonacci sequence.
                                FibonacciNumberDataAccess.selectAsFlowable(testDatabase.getNitriteDatabase())

                                        // We want to observe on the IO scheduler...this also cuts the buffer
                                        // size down to 3 x RAIL_COUNT
                                        .observeOn(Schedulers.io(), false, 3)
                                , RAIL_COUNT
                        )
                        // ParallelFlowable.runOn method to set where the parallel tasks
                        // will take place.
                        .runOn(Schedulers.io(), 1);

                // To subscribe to a parallel flowable, you must provide a FlowableSubscriber
                // per rail.
                FlowableSubscriber<Long>[] subscriberRails = new FlowableSubscriber[RAIL_COUNT];
                for( int i = 0 ; i < subscriberRails.length ; i++ ) {

                    // Make a final version of our index so we can access it
                    // from inside our anonymous FlowableSubscriber.
                    final int iInner = i;

                    // Our event counter so we know when to ask for more events.
                    AtomicInteger counter = new AtomicInteger();

                    subscriberRails[i] = new FlowableSubscriber<>() {

                        // A place to store our subscription so we can request more
                        // message.
                        private Subscription subscription;

                        // The id for this rail so we can log it.
                        private int id = iInner;

                        // Select the correct gate array for this rail.
                        private GateBasedSynchronization gate = gates[iInner];

                        @Override
                        public void onSubscribe(Subscription s) {

                            // Store the subscription reference.
                            this.subscription = s;

                            // Request the first batch of events.  Let's ask
                            // for the same number of events as we have parallel
                            // rails.
                            this.subscription.request(RAIL_COUNT);
                        }

                        @Override
                        public void onNext(Long nextNumber) {

                            // Slow things down on purpose.
                            ThreadHelper.sleep(50, TimeUnit.MILLISECONDS);

                            // Log the next number
                            log.info("Next Fibonacci Number: {} - {}", nextNumber, id);

                            // See if we need to request more events.
                            if(counter.incrementAndGet() % RAIL_COUNT == 0 ) {

                                // Request more events - one for each parallel rail.
                                this.subscription.request(RAIL_COUNT);
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            log.error(t.getMessage(),t);
                            gate.openGate("onError");
                        }

                        @Override
                        public void onComplete() {
                            log.info( "onComplete - {}", id);
                            gate.openGate("onComplete");
                        }
                    };
                }

                // Finally...we subscribe and watch the magic.  We pass in
                // our FlowableSubscribers array, one for each parallel rail.
                fibonacciParallelFlowable.subscribe(subscriberRails);

                // Wait for things to finish.
                GateBasedSynchronization.waitMultiple( new String[] { "onError", "onComplete"}, gates);
            }
        }
        catch( Throwable t ) {
            log.error(t.getMessage(),t);
        }

        System.exit(0);
    }
}
