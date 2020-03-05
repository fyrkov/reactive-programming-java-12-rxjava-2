package com.pluralsight.rxjava2.module2;

import com.pluralsight.rxjava2.utility.GateBasedSynchronization;
import com.pluralsight.rxjava2.utility.datasets.GreekAlphabet;
import com.pluralsight.rxjava2.utility.datasets.GreekLetterPair;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemoModule1LifecycleExample {

    private static Logger log = LoggerFactory.getLogger(DemoModule1LifecycleExample.class);

    public static void main(String[] args) {

        // Synchronization magic.
        GateBasedSynchronization gate = new GateBasedSynchronization();

        // Create an Observable and store it to a local variable.
        // This will "zip" together two streams of the same length into a single
        // stream of a composite object (GreekLetterPair).
        Observable<GreekLetterPair> zipTogether = Observable.zip(
                GreekAlphabet.greekAlphabetInGreekObservable(),
                GreekAlphabet.greekAlphabetInEnglishObservable(),
                ( greekLetter, englishLetter) -> new GreekLetterPair(greekLetter, englishLetter)
        );

        // Subscribe to the zip observable and have it generate it's output.
        subscribeToZipObservable(gate, zipTogether);

        // Wait for either "onComplete" or "onError" to be called.
        gate.waitForAny("onComplete", "onError");

        // Reset all synchronization gates.
        gate.resetAll();

        log.info("--------------------------------------------------------------------------------");

        // Subscribe to the zip observable and have it generate it's output.
        subscribeToZipObservable(gate, zipTogether);

        // Wait for either "onComplete" or "onError" to be called.
        gate.waitForAny("onComplete", "onError");

        // Reset all synchronization gates.
        gate.resetAll();

        System.exit(0);
    }

    private static void subscribeToZipObservable(GateBasedSynchronization gate, Observable<GreekLetterPair> zipTogether) {

        zipTogether.subscribe(new Observer<GreekLetterPair>() {

            @Override
            public void onSubscribe(Disposable d) {
                log.info("onSubscribe");
            }

            // onNext is passed a GreekLetterPair, which is what our incoming zip observable outputs.
            @Override
            public void onNext(GreekLetterPair greekLetterPair) {
                log.info("onNext - ({}, {})", greekLetterPair.getGreekLetter(), greekLetterPair.getEnglishLetter());
            }

            @Override
            public void onError(Throwable e) {
                log.info("onError - {}", e.getMessage());
                gate.openGate("onError");

            }

            @Override
            public void onComplete() {
                log.info("onComplete");
                gate.openGate("onComplete");
            }
        });
    }
}
