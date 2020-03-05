package com.pluralsight.rxjava2.module3;

import com.pluralsight.rxjava2.utility.datasets.GreekAlphabet;
import com.pluralsight.rxjava2.utility.subscribers.DemoSubscriber;
import io.reactivex.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilterExample1 {

    private static final Logger log = LoggerFactory.getLogger(FilterExample1.class);

    public static void main(String[] args) {

        // Create an Observable to filter.
        Observable<String> greekAlphabet = GreekAlphabet.greekAlphabetInEnglishObservable()

                // Filter out "delta"
                .filter( nextLetter -> !nextLetter.equals("delta"));

        greekAlphabet.subscribe(new DemoSubscriber<>());

        System.exit(0);
    }
}
