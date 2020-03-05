package com.pluralsight.rxjava2.module4;

import com.pluralsight.rxjava2.nitrite.entity.LetterPair;
import com.pluralsight.rxjava2.utility.datasets.GreekAlphabet;
import com.pluralsight.rxjava2.utility.subscribers.DemoSubscriber;
import io.reactivex.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZipWithExample1 {
    private final Logger log = LoggerFactory.getLogger(ZipWithExample1.class);

    public static void main(String[] args) {

        // The zipWith operator is used to chain together zip operations.
        Observable<LetterPair> greekWithEnglishObservable = GreekAlphabet.greekAlphabetInGreekObservable()
                .zipWith(
                        GreekAlphabet.greekAlphabetInEnglishObservable(),
                        // ...for each observable entry, we return a LetterPair "zipping" them
                        // together.
                        (greekLetter, english) -> new LetterPair(greekLetter, english)
                );

        greekWithEnglishObservable.subscribe(new DemoSubscriber<>());

        System.exit(0);
    }
}
