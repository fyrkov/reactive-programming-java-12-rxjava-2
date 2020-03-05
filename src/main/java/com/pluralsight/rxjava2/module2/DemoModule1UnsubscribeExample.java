package com.pluralsight.rxjava2.module2;

import com.pluralsight.rxjava2.utility.GateBasedSynchronization;
import com.pluralsight.rxjava2.utility.datasets.GreekAlphabet;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

// ---------------------------------------------------------------------------------------------------------------
// This demonstration is intended to illustrate...
// 1.  Simple Observable creation using the "fromArray" creation method.
// 2.  Show the event sequence that occurs when an Observable is unsubscribed from before it has called onComplete.
// ---------------------------------------------------------------------------------------------------------------
public class DemoModule1UnsubscribeExample {

    private static Logger log = LoggerFactory.getLogger(DemoModule1UnsubscribeExample.class);

    public static void main(String[] args) {

        // Simple counter so we can cut things off in the middle...
        AtomicInteger counter = new AtomicInteger(0);

        // Synchronization magic.
        GateBasedSynchronization gate = new GateBasedSynchronization();

        // Create an Observable<String> that contains the 24 greek letters.
        Observable.fromArray(GreekAlphabet.greekLetters)

                // Using dot-chaining syntax, subscribe to the Observable
                // we just created.  In this case, I am creating an
                // anonymous class based on Observer<String> so we can see
                // all of the methods that are available.
                .subscribe(new Observer<String>() {

                    // Field used to hold the Disposable instance so we can unsubscribe
                    // from the stream of greek letters.
                    private Disposable disposable;

                    // This time we want to capture the "Disposable" so we can
                    // illustrate it's use.
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        this.disposable = disposable;

                        log.info( "onSubscribe" );
                    }

                    // onNext is called for each event that is emitted by an
                    // observable.  Once onError or onComplete has been called,
                    // onNext is guaranteed to never be called again on this
                    // Observable.
                    @Override
                    public void onNext(String nextLetter) {
                        log.info( "onNext - {}" , nextLetter );

                        // Once we have reached 5 events, we want to unsubscribe
                        if( counter.incrementAndGet() >= 5) {

                            // We hit how many messages we want, so we call
                            // dispose on our subscription.  This will make the Observable
                            // stop sending us onNext events.
                            disposable.dispose();

                            // We open the "eventMaxReached" gate so that it will allow
                            // the main thread to terminate.
                            gate.openGate("eventMaxReached");
                        }
                    }

                    // onError is called when any exception is thrown either
                    // in the Observable code, or from the Observer code.
                    @Override
                    public void onError(Throwable e) {

                        // Send the error message to the log.
                        log.error("onError - {}" , e.getMessage());

                        // Open the gate for "onError" so that the main
                        // thread will be allowed to continue.
                        gate.openGate("onError");
                    }

                    // onComplete is called when the Observable finishes emitting
                    // all events.  If onError is called, you will not see an
                    // onComplete call.  Likewise, once onComplete is called, onError
                    // is guaranteed not to be called.
                    @Override
                    public void onComplete() {
                        log.info( "onComplete" );
                        gate.openGate("onComplete");
                    }
                });

        // Wait for one of these gates to open: "eventMaxReached", "onComplete" or "onError"
        gate.waitForAny("eventMaxReached", "onComplete", "onError");

        // Is the "eventMaxReached" gate open?
        log.info("eventMaxReached gate status: {}" , gate.isGateOpen("eventMaxReached"));

        // Did "onComplete" get called?
        log.info("onComplete      gate status: {}" , gate.isGateOpen("onComplete"));

        // Did "onError" get called?
        log.info("onError         gate status: {}" , gate.isGateOpen("onError"));

        System.exit(0);
    }
}
