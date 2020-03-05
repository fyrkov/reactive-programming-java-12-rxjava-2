package com.pluralsight.rxjava2.module5;

import com.pluralsight.rxjava2.utility.ThreadHelper;
import com.pluralsight.rxjava2.utility.datasets.FibonacciSequence;
import com.pluralsight.rxjava2.utility.datasets.GreekAlphabet;
import com.pluralsight.rxjava2.utility.subjects.SelectableSubject;
import com.pluralsight.rxjava2.utility.subscribers.DemoSubscriber;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class PublishSubjectExample1 {

    private final static Logger log = LoggerFactory.getLogger(PublishSubjectExample1.class);

    public static void main(String[] args) {

        // Create a SelectableSubject<String> using a PublishSubject
        SelectableSubject subject = new SelectableSubject(PublishSubject.create());

        // At least one consumer needs to be present, else the producers
        // will detect that no one is listening and dispose themselves.
        subject.addEventConsumer(
                new DemoSubscriber()
        );
        subject.addEventConsumer(
                new DemoSubscriber()
        );

        // Create an Observable that emits the English form of the Greek alphabet.
        subject.addEventProducer(
                // The base observable will be the English version of the Greek
                // alphabet.
                GreekAlphabet.greekAlphabetInEnglishObservable()
                .subscribeOn(Schedulers.computation())
        );

        subject.addEventProducer(
                // The base observable will be the Fibonacci Numbers in String form.
                FibonacciSequence.create(20)
                        .repeat()
                        .map( nextNumber -> Long.toString(nextNumber))
                        .subscribeOn(Schedulers.computation())
        );

        ThreadHelper.sleep(10, TimeUnit.SECONDS);

        System.exit(0);
    }
}
