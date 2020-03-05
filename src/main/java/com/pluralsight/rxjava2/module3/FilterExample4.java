package com.pluralsight.rxjava2.module3;

import com.pluralsight.rxjava2.utility.datasets.GreekAlphabet;
import io.reactivex.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilterExample4 {
    private static final Logger log = LoggerFactory.getLogger(FilterExample4.class);

    public static void main(String[] args) {

        // Create an Observable to filter.
        // Filter out "delta"
        Observable<String> greekAlphabet = GreekAlphabet.greekAlphabetInEnglishObservable()
                .filter( nextLetter -> !nextLetter.toLowerCase().equals("delta"));

        greekAlphabet.subscribe(nextLetter -> {
            log.info( "onNext - {}" , nextLetter);

            if (nextLetter.equals("iota")) {
                throw new IllegalStateException("BOOM!");

            }
        });

        System.exit(0);
    }
}
