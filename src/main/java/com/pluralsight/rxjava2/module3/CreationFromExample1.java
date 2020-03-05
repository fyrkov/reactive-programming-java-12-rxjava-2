package com.pluralsight.rxjava2.module3;

import com.pluralsight.rxjava2.utility.datasets.FibonacciSequence;
import com.pluralsight.rxjava2.utility.subscribers.DemoSubscriber;
import io.reactivex.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.*;

public class CreationFromExample1 {

    private final static Logger log = LoggerFactory.getLogger(CreationFromExample1.class);

    public static void main(String[] args) {

        Long[] firstFiveFibonacciNumbers = FibonacciSequence.toArray(5);

        // example of "fromArray" using an array of 5 integers.
        log.info("fromArray");

        Observable<Long> targetObservable = Observable.fromArray(firstFiveFibonacciNumbers);
        targetObservable.subscribe(new DemoSubscriber<>());

        log.info("");
        log.info("-----------------------------------------------------------------------------------");
        log.info("");
        log.info("fromIterable");

        // example of "fromIterable" using an array of 7 integers
        ArrayList<Long> fibonacciArray = FibonacciSequence.toArrayList(7);
        targetObservable = Observable.fromIterable(fibonacciArray);
        targetObservable.subscribe(new DemoSubscriber<>());

        log.info("");
        log.info("-----------------------------------------------------------------------------------");
        log.info("");
        log.info("fromCallable");

        // example of "fromCallable" using an array of 9 integers
        Observable<Long[]> workObservable = Observable.fromCallable(() -> FibonacciSequence.toArray(9));

        // Note that the fromCallable method only returns a single value in the return Observable.
        targetObservable = Observable.fromArray(workObservable.blockingSingle());
        targetObservable.subscribe(new DemoSubscriber<>());

        log.info("");
        log.info("-----------------------------------------------------------------------------------");
        log.info("");
        log.info("fromFuture");

        // example of Observable creation via "fromFuture" using an array of 6 integers

        // Create a FutureTask that will return an Integer array of 6 elements
        FutureTask<Long[]> futureTask = new FutureTask<>(() -> FibonacciSequence.toArray(6));

        // Create an ExecutorService that has a single thread.
        ExecutorService executor = Executors.newFixedThreadPool(1);

        // Execute the futureTask to generate the fibonacci sequence...but this will be on a different
        // thread.
        executor.execute(futureTask);

        // Take in the FutureTask (Future as it's base class) and makeObservable an Observable<Integer[]>
        // based on it's result.
        workObservable = Observable.fromFuture(futureTask);

        // Block to pull out the data from the future and pass into an Observable<Integer>
        // using fromArray
        targetObservable = Observable.fromArray(workObservable.blockingSingle());
        targetObservable.subscribe(new DemoSubscriber<>());

        System.exit(0);
    }
}
