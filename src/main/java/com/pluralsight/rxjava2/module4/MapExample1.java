package com.pluralsight.rxjava2.module4;

import com.pluralsight.rxjava2.utility.datasets.GreekAlphabet;
import com.pluralsight.rxjava2.utility.subscribers.DemoSubscriber;
import io.reactivex.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapExample1 {

    private final static Logger log = LoggerFactory.getLogger(MapExample1.class);

    public static void main(String[] args) {

        // Take each english representation and return a string that
        // contains the string length of each one...
        Observable<Integer> lengthStream =
                GreekAlphabet.greekAlphabetInEnglishObservable()
                .map(nextRepresentation -> nextRepresentation.length() );

        lengthStream.subscribe(new DemoSubscriber<>());

        System.exit(0);
    }
}
