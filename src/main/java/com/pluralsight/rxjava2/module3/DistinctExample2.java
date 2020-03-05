package com.pluralsight.rxjava2.module3;

import com.pluralsight.rxjava2.utility.datasets.GreekAlphabet;
import com.pluralsight.rxjava2.utility.subscribers.DemoSubscriber;
import io.reactivex.Observable;

public class DistinctExample2 {

    public static void main(String[] args) {

        // Get the usual Greek alphabet and repeat it 3 times.
        Observable<String> greekAlphabet = GreekAlphabet.greekAlphabetInEnglishObservable()
                .repeat(3);

        // We want only "distinct" values.
        greekAlphabet
                .distinct(nextLetter -> nextLetter.charAt(0))
                .subscribe(new DemoSubscriber<>());


        System.exit(0);
    }
}
