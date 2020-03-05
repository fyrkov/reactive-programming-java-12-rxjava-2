package com.pluralsight.rxjava2.module5;

import com.pluralsight.rxjava2.utility.ThreadHelper;
import com.pluralsight.rxjava2.utility.datasets.FibonacciSequence;
import com.pluralsight.rxjava2.utility.subscribers.DemoSubscriber;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class ReplaySubjectExample1 {

    private final static Logger log = LoggerFactory.getLogger(ReplaySubjectExample1.class);

    public static void main(String[] args) {

        // Create our ReplaySubject.  We create it with a limit of 20
        // which makes it a rotating window of 20 items.
        Subject<Long> replaySubject = ReplaySubject
                .createWithSize(20);

        // Create a Fibonacci Sequence that is longer than our
        // ReplaySubject's capacity
        Observable<Long> fibonacciSequence = FibonacciSequence
                .create(30)
                .subscribeOn(Schedulers.computation());

        // Subscribe to the number sequence and emit
        // onNext messages into our replay subject.
        fibonacciSequence.subscribe(
                nextNumber -> replaySubject.onNext(nextNumber)
        );

        // Pause to allow the sequence to run for a moment
        ThreadHelper.sleep(1, TimeUnit.SECONDS);

        // Attach to the ReplaySubject.  We should get at least
        // 20 numbers.
        replaySubject
                .subscribeOn(Schedulers.computation())
                .subscribe(new DemoSubscriber<>());

        // Give it a second...
        ThreadHelper.sleep(1, TimeUnit.SECONDS);

        log.info("----------------------------------------------------");

        // Attach a second observer and see that we get 20
        // numbers.
        replaySubject
                .subscribeOn(Schedulers.computation())
                .subscribe(new DemoSubscriber<>());

        ThreadHelper.sleep(2, TimeUnit.SECONDS);

        System.exit(0);
    }
}
