package com.pluralsight.rxjava2.module4;

import com.pluralsight.rxjava2.utility.GateBasedSynchronization;
import com.pluralsight.rxjava2.utility.datasets.GreekAlphabet;
import com.pluralsight.rxjava2.utility.datasets.GreekLetterPair;
import com.pluralsight.rxjava2.utility.subscribers.DemoSubscriber;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class FlatMapExample1 {

    private final static Logger log = LoggerFactory.getLogger(FlatMapExample1.class);

    public static void main(String[] args) {

        GateBasedSynchronization gate = new GateBasedSynchronization();

        // Create the first thread pool
        AtomicInteger threadPool1Counter = new AtomicInteger();
        Executor threadPool1 = Executors.newFixedThreadPool(20, runnable -> {
            Thread returnThread = new Thread(runnable, "Pool 1 Thread " + threadPool1Counter.getAndIncrement());
            return returnThread;
        });
        Scheduler scheduler1 = Schedulers.from(threadPool1);

        // Create a second thread pool
        AtomicInteger threadPool2Counter = new AtomicInteger();
        Executor threadPool2 = Executors.newFixedThreadPool(20, runnable -> {
            Thread returnThread = new Thread(runnable, "Pool 2 Thread " + threadPool2Counter.getAndIncrement());
            return returnThread;
        });
        Scheduler scheduler2 = Schedulers.from(threadPool2);


        // flatMap is used to process a single event into an Observable of zero or
        // many events.  In this case, we will take a single Greek letter,
        // find it's English counterpart and pair them together.  But first we emit
        // strings that represent the Greek and English strings.
        Observable<Object> greekLetterPairs = GreekAlphabet.greekAlphabetInGreekObservable()
                .flatMap((String greekLetter) -> {

                    // Find the offset into the array of this greek character.
                    int offset = GreekAlphabet.findGreekLetterOffset(greekLetter);

                    return Observable.just(
                            greekLetter,
                            GreekAlphabet.greekLettersInEnglish[offset],
                            new GreekLetterPair(greekLetter, GreekAlphabet.greekLettersInEnglish[offset])

                    )
                    .doOnSubscribe( d -> log.info( "observable onSubscribe"))
                    .doOnNext( event -> log.info("observable onNext - {}", event))
                    .doOnComplete( () -> log.info("Observable onComplete"))
                    .subscribeOn(scheduler2);

                }, 3)
                .observeOn(scheduler1)
                ;

        greekLetterPairs
                .doOnSubscribe( d -> log.info("flatMap onSubscribe"))
                .doOnNext(o -> log.info("flatMap onNext - {}" , o))
                .doOnComplete(() -> gate.openGate("onComplete"))
                .doOnError((t) -> {
                    gate.openGate("onError");
                    log.error(t.getMessage(), t);
                })
                .subscribe(new DemoSubscriber<>());

        gate.waitForAny("onComplete", "onError");

        System.exit(0);
    }
}
