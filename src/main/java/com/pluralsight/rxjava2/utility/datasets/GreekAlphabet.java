package com.pluralsight.rxjava2.utility.datasets;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;

public class GreekAlphabet {

    private static Logger log = LoggerFactory.getLogger(GreekAlphabet.class);

    public static String[] greekLettersInEnglish = {
            "alpha",
            "beta",
            "gamma",
            "delta",
            "epsilon",
            "zeta",
            "eta",
            "theta",
            "iota",
            "kappa",
            "lambda",
            "mu",
            "nu",
            "xi",
            "omicron",
            "pi",
            "rho",
            "sigma",
            "tau",
            "upsilon",
            "phi",
            "chi",
            "psi",
            "omega"
    };

    public static String greekLetters[] = {
            "\u03b1",
            "\u03b2",
            "\u03b3",
            "\u03b4",
            "\u03b5",
            "\u03b6",
            "\u03b7",
            "\u03b8",
            "\u03b9",
            "\u03ba",
            "\u03bb",
            "\u03bc",
            "\u03bd",
            "\u03be",
            "\u03bf",
            "\u03c0",
            "\u03c1",
            "\u03c3",
            "\u03c4",
            "\u03c5",
            "\u03c6",
            "\u03c7",
            "\u03c8",
            "\u03c9"
    };

    public static int findGreekLetterOffset(String greekLetter) {
        // Find the offset into the array of this greek character.
        int offset = -1;
        for( int i = 0 ; i < GreekAlphabet.greekLetters.length ; i++ ) {

            if(greekLetter.equals(GreekAlphabet.greekLetters[i])) {
                offset = i;
                break;
            }
        }

        return offset;
    }

    public static Observable<String> greekAlphabetInEnglishObservable() {

        return Observable.fromArray(greekLettersInEnglish)
            ;//.doOnSubscribe( disposable -> log.info( "doOnSubscribe - greekAlphabetInEnglishObservable"));
    }

    public static Observable<String> greekAlphabetInGreekObservable() {
        return Observable.fromArray(greekLetters)
            ;//.doOnSubscribe( disposable -> log.info( "doOnSubscribe - greekAlphabetInGreekObservable"));
    }

    public static Observable<String> greekAlphabetWithException() {
        return Observable.create(emitter -> {

            // Send out greek letters.
            IntStream.range(0, greekLetters.length).forEach( nextOffset -> {

                if( nextOffset == 5 ) {
                    throw new IllegalStateException("Boom!");
                }

                emitter.onNext(greekLetters[nextOffset]);
            });
        });
    }

    public static Observable<String> greekAlphabetInEnglishHotObservable(final boolean logEachEmission) {

        // Make an endless stream of greek letters by adding the "repeat"
        // operator.  We also want to put this on it's own thread, so we
        // tell it to subscribeOn the newThread scheduler.
        // THIS IS A BAD THING TO DO...THIS THREAD WILL RUN FOREVER AND THERE'S NO
        // WAY TO STOP IT...short of System.exit(0).  Demo purposes only.
        Observable<String> returnObservable = greekAlphabetInEnglishObservable()
                .repeat()
                .doOnNext( nextLetter -> {
                    if( logEachEmission ) {
                        log.info("Emitting - {}", nextLetter);
                    }
                })
                .subscribeOn(Schedulers.newThread());

        // Create a PublishSubject - Subjects are both Observers and Observable.
        PublishSubject<String> publishSubject = PublishSubject.create();

        // Subscribe to the returnObservable the PublishSubject.
        // We don't want this on a separate thread...we want the generating
        // observable and the publish subject to be tied together.
        returnObservable
                .subscribe(publishSubject);

        // Return the PublishSubject, set to observeOn its own thread.
        // Note that we are returning it as an Observable<String>.
        // Subjects are themselves Observable.
        return publishSubject.observeOn(Schedulers.newThread());
    }
}
