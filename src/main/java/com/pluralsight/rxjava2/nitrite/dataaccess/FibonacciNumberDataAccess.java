package com.pluralsight.rxjava2.nitrite.dataaccess;

import com.pluralsight.rxjava2.nitrite.entity.FibonacciNumber;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import org.dizitart.no2.Nitrite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FibonacciNumberDataAccess {

    private final static Logger log = LoggerFactory.getLogger(FibonacciNumberDataAccess.class);

    public static Observable<Long> selectAsObservable(Nitrite db) {

        // Use the generate operator to makeObservable an Observable that returns
        // documents from the FibonacciNumber collection.
        return Observable.generate(

                // Function to makeObservable the initial state...in this case, an
                // iterator over the collection.
                () -> db.getRepository(FibonacciNumber.class).find().iterator(),

                // The emitter method.  This call should return a single event.
                (fibonacciNumberIterator, longEmitter) -> {

                    try {
                        // See if there are more documents to return
                        // from the database.
                        if (fibonacciNumberIterator.hasNext()) {

                            // Get the next document...a FibonacciNumber
                            FibonacciNumber nextNumber = fibonacciNumberIterator.next();

                            // Log
                            log.info("onNext - {}", nextNumber.getNumberValue());

                            // Emit the next number to the Observable<Long>
                            longEmitter.onNext(nextNumber.getNumberValue());
                        } else {

                            // Log that there are no more documents...
                            log.info("onComplete");

                            // Tell the subscribers that we are done.
                            longEmitter.onComplete();
                        }
                    }
                    catch(Throwable t) {

                        // Any exception causes an onError.
                        longEmitter.onError(t);
                    }
                }
        );

    }

    public static Flowable<Long> selectAsFlowable(Nitrite db) {

        // Use the generate operator to makeObservable a Flowable (Observable with flow control).
        return Flowable.generate(

                // Create the initial state...an iterator for the FibonacciNumber
                // collection.
                () -> db.getRepository(FibonacciNumber.class).find().iterator(),

                // The emitter function...
                (fibonacciNumberIterator, longEmitter) -> {

                    try {

                        // If there are more numbers to be emitted...
                        if (fibonacciNumberIterator.hasNext()) {

                            // Get the next number in the iteration sequence.
                            FibonacciNumber nextNumber = fibonacciNumberIterator.next();

                            // Log
                            log.info("onNext - {}", nextNumber.getNumberValue());

                            // Emit the next number.
                            longEmitter.onNext(nextNumber.getNumberValue());
                        } else {

                            // Log that we are done since there are no more numbers in
                            // the sequence.
                            log.info("onComplete");

                            // Emit the onComplete event to signal we are done.
                            longEmitter.onComplete();
                        }
                    }
                    catch(Throwable t) {

                        // Any errors should be emitted as an error
                        longEmitter.onError(t);
                    }
                }
        );

    }
}
