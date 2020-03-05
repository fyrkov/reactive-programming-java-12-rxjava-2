package com.pluralsight.rxjava2.utility.datasets;

import io.reactivex.Observable;

import java.util.ArrayList;

public class FibonacciSequence {

    public static Observable<Long> create(final long totalNumbers) {

        return Observable.create(emitter -> {

            long count = 0;
            long previousValue1 = 1;
            long previousValue2 = 1;

            while( count < totalNumbers ) {

                if( emitter.isDisposed() ) {
                    break;
                }

                ++count;

                if( count == 1 ) {
                    emitter.onNext(0L);
                    continue;
                }

                if( count == 2 ) {
                    emitter.onNext(1L);
                    continue;
                }

                long newValue = previousValue1 + previousValue2;
                emitter.onNext(newValue);

                previousValue1 = previousValue2;
                previousValue2 = newValue;
            }

            if( !emitter.isDisposed() ) {
                emitter.onComplete();
            }
        });

    }

    public static Long[] toArray( int totalNumbers ) {

        return create(totalNumbers)
                .collectInto(new ArrayList<Long>(totalNumbers), (collectionTarget, nextValue) -> collectionTarget.add(nextValue))
                .blockingGet()
                .toArray(new Long[totalNumbers]);

    }

    public static ArrayList<Long> toArrayList( int totalNumbers ) {
        return create(totalNumbers)
                .collectInto(new ArrayList<Long>(totalNumbers), (collectionTarget, nextValue) -> collectionTarget.add(nextValue))
                .blockingGet();
    }

}
