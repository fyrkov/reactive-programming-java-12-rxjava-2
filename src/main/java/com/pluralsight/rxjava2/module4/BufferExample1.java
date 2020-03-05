package com.pluralsight.rxjava2.module4;

import com.pluralsight.rxjava2.utility.datasets.FibonacciSequence;
import com.pluralsight.rxjava2.utility.subscribers.DemoSubscriber;
import io.reactivex.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BufferExample1 {

    private final static Logger log = LoggerFactory.getLogger(BufferExample1.class);

    public static void main(String[] args) {

        // Create a Fibonacci observable to 20 values.
        // Because we are going to buffer the results, we will expect
        // a Observable<List<Long>> instead of Observable<Long>.
        Observable<List<Long>> bufferedFibonacciSequence = FibonacciSequence.create(20)

                // Emit items 3 at a time.
                .buffer(3);

        // Subscribe using the demo subscriber.
        bufferedFibonacciSequence.subscribe(new DemoSubscriber<>());

        System.exit(0);
    }
}
