package com.pluralsight.rxjava2.module2;

import com.pluralsight.rxjava2.utility.GateBasedSynchronization;
import com.pluralsight.rxjava2.utility.ThreadHelper;
import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.schedulers.Schedulers;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class DemoModule1FlowableExample {

    private static final Logger log = LoggerFactory.getLogger(DemoModule1FlowableExample.class);

    public static void main(String[] args) {

        // Synchronization helper
        GateBasedSynchronization gate = new GateBasedSynchronization();

        // Create an ever-repeating number counter that counts from 1 to 1 billion.
        Flowable<Integer> rangeOfNumbers = Flowable.range(1 , 1_000_000_000)
                .repeat()
                .doOnNext( nextInt -> log.info("emitting int {}", nextInt))
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread(), false , 3);

        // Create a FlowableSubscriber with a slight delay of 10ms.
        // This should make the rangeOfNumber's emission far outpace
        // the subscriber.
        FlowableSubscriber<Integer> demoSubscriber = new FlowableSubscriber<Integer>() {

            private AtomicInteger counter = new AtomicInteger(0);
            private Subscription subscription;

            @Override
            public void onSubscribe(Subscription subscription) {
                this.subscription = subscription;

                log.info( "onSubscribe" );
                subscription.request(3);
            }

            @Override
            public void onNext(Integer integer) {
                log.info( "onNext - {}", integer);

                // Slow things down a bit
                ThreadHelper.sleep(10L, TimeUnit.MILLISECONDS);

                // Every three events, request 3 more.
                if(counter.incrementAndGet() % 3 == 0) {
                    subscription.request(3);
                }
            }

            @Override
            public void onError(Throwable t) {
                log.error("onError - {}" , t.getMessage());
                gate.openGate("onError");
            }

            @Override
            public void onComplete() {
                log.info("onComplete");
                gate.openGate("onComplete");
            }
        };

        rangeOfNumbers.subscribe(demoSubscriber);

        // Let it run for 20 seconds or until something completes...or blows up.
        gate.waitForAny(20, TimeUnit.SECONDS, "onComplete", "onError");

        System.exit(0);
    }
}
