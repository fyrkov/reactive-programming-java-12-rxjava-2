package com.pluralsight.rxjava2.module5;

import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BehaviorSubjectExample1 {

    private final static Logger log = LoggerFactory.getLogger(BehaviorSubjectExample1.class);

    public static void main(String[] args) {

        // Create our test BehaviorSubject with an initial state of "omega"
        BehaviorSubject<String> behaviorSubject = BehaviorSubject.createDefault("omega");

        // Subscribe and notice that we will have an initial state - the last
        // event that was emitted into the BehaviorSubject.
        Disposable subscription1 = behaviorSubject.subscribe(
                nextLetter -> log.info("onNext - {}", nextLetter));
        subscription1.dispose();

        // Emit a few more letters
        behaviorSubject.onNext("alpha");
        behaviorSubject.onNext("beta");
        behaviorSubject.onNext("gamma");

        // See what we get for the current event
        Disposable subscription2 = behaviorSubject.subscribe(
                nextLetter -> log.info("onNext - {}", nextLetter));
        subscription2.dispose();

        System.exit(0);
    }
}
