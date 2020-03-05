package com.pluralsight.rxjava2.module2;

import com.pluralsight.rxjava2.utility.GateBasedSynchronization;
import com.pluralsight.rxjava2.utility.datasets.GreekAlphabet;
import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// ---------------------------------------------------------------------------------------------------------------
// This demonstration is intended to illustrate...
// 1.  Show have to makeObservable a Single type of Observable.
// 2.  Illustrate the SingleObserver notification sequence when using a Single Observable.
// ---------------------------------------------------------------------------------------------------------------
public class DemoModule1SingleExample {

    private static Logger log = LoggerFactory.getLogger(DemoModule1SingleExample.class);

    public static void main(String[] args) {

        // My synchronization magic.  Let's keep this thread from exiting
        // until all of our test code has executed.
        GateBasedSynchronization gate = new GateBasedSynchronization();

        // Create an Observable<String> that contains the 24 greek letters.
        Single<String> targetObservable = Observable.fromArray(GreekAlphabet.greekLetters)

                // From the stream of greek letters, take only the first one.
                .first("A");


        // Subscribe to the "Single" result Observable.
        // Note the slightly different method names on the MaybeObserver interface.
        targetObservable.subscribe(new SingleObserver<String>() {

            // onSubscribe is called in the usual way...
            @Override
            public void onSubscribe(Disposable d) {
                log.info( "onSubscribe" );
            }

            // Since there is only a _Single_ event, there is no onNext method.
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
        });

        // Wait for either "onSuccess" or "onError" to be called.
        gate.waitForAny("onSuccess", "onError");

        System.exit(0);
    }
}
