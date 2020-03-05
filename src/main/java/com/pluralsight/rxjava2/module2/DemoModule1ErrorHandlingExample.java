package com.pluralsight.rxjava2.module2;

import com.pluralsight.rxjava2.utility.GateBasedSynchronization;
import com.pluralsight.rxjava2.utility.datasets.GreekAlphabet;
import com.pluralsight.rxjava2.utility.datasets.GreekLetterPair;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class DemoModule1ErrorHandlingExample {

    private static Logger log = LoggerFactory.getLogger(DemoModule1ErrorHandlingExample.class);

    public static void main(String[] args) {

        // My synchronization magic.  Let's keep this thread from exiting
        // until all of our test code has executed.
        GateBasedSynchronization gate = new GateBasedSynchronization();

        AtomicInteger counter =  new AtomicInteger();

        // Create an Observable and store it to a local variable.
        // This will "zip" together two streams of the same length into a single
        // stream of a composite object (GreekLetterPair).
        Observable<GreekLetterPair> zipTogether = Observable.zip(
                GreekAlphabet.greekAlphabetInGreekObservable(),
                GreekAlphabet.greekAlphabetInEnglishObservable(),
                ( greekLetter, englishLetter) -> {

                    // Cause an exception on the 5th event.
                    if( counter.incrementAndGet() >= 5 ) {
                        throw new IllegalStateException("BOOM!");
                    }

                    return new GreekLetterPair(greekLetter, englishLetter);
                }
        );

        zipTogether
                // onErrorResumeNext
                // The placement of onError operators matter.  It must be downstream of the exceptions
                // they are meant to guard against.
                .onErrorResumeNext(Observable.just(new GreekLetterPair("κεραία", "BOOM")))

                .subscribe(new Observer<GreekLetterPair>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        log.info("onSubscribe");
                    }

                    @Override
                    public void onNext(GreekLetterPair nextPair) {
                        log.info("onNext - ({},{})" , nextPair.getGreekLetter(), nextPair.getEnglishLetter());
                    }

                    @Override
                    public void onError(Throwable e) {
                        log.info("onError - {}", e.getMessage());
                        log.error( e.getMessage(), e);
                        gate.openGate("onError");
                    }

                    @Override
                    public void onComplete() {
                        log.info("onComplete");
                        gate.openGate("onComplete");
                    }
                });

        // Wait for either "onComplete" or "onError" to be called.
        gate.waitForAny("onComplete", "onError");

        System.exit(0);
    }
}
