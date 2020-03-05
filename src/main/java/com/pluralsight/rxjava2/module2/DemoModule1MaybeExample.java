package com.pluralsight.rxjava2.module2;

import com.pluralsight.rxjava2.utility.GateBasedSynchronization;
import com.pluralsight.rxjava2.utility.datasets.GreekAlphabet;
import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// ---------------------------------------------------------------------------------------------------------------
// This demonstration is intended to illustrate...
// 1.  Show have to makeObservable a Maybe type of Observable.
// 2.  Illustrate the MaybeObserver notification sequence when using a Maybe Observable.
// ---------------------------------------------------------------------------------------------------------------
public class DemoModule1MaybeExample {

    private static Logger log = LoggerFactory.getLogger(DemoModule1MaybeExample.class);

    public static void main(String[] args) {

        // My synchronization magic.  Let's keep this thread from exiting
        // until all of our test code has executed.
        GateBasedSynchronization gate = new GateBasedSynchronization();

        // Create a Maybe<String> that contains 1 greek letters, but only if that letter is alpha.
        Maybe<String> targetObservable = Observable.fromArray(GreekAlphabet.greekLetters)

                // From the stream of greek letters, take only the first one.
                .firstElement()

                // filter out the letter alpha
                .filter(nextLetter -> nextLetter.equals("\u03b1"));

        // Note the slightly different method names on the MaybeObserver interface.
        // Subscribe to the "Maybe" result Observable.
        targetObservable.subscribe(new MaybeObserver<String>() {

            // onSubscribe is called in the usual way...
            @Override
            public void onSubscribe(Disposable d) {
                log.info( "onSubscribe" );
            }

            // Since there is only one event possible, there is no onNext method.
            // Instead, we simply have an onSuccess message and no onComplete.
            @Override
            public void onSuccess(String nextLetter) {
                log.info( "onSuccess - {}" , nextLetter );
                gate.openGate("onSuccess");
            }

            @Override
            public void onError(Throwable e) {

                // Send the error message to the log.
                log.error("onError - {}" , e.getMessage());

                // Open the gate for "onError" so that the main
                // thread will be allowed to continue.
                gate.openGate("onError");
            }

            // If the event stream is empty (no events at all),
            // then onComplete will be called to indicate that the stream
            // is done emitting.  If onSuccess is called, then onComplete
            // will NOT be called.
            @Override
            public void onComplete() {
                log.error("onComplete");

                gate.openGate("onComplete");
            }
        });

        // Wait for either "onSuccess", "onComplete", or "onError" to be called.
        gate.waitForAny("onSuccess", "onComplete", "onError");

        System.exit(0);
    }
}
