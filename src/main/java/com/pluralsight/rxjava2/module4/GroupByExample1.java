package com.pluralsight.rxjava2.module4;

import com.pluralsight.rxjava2.utility.datasets.FibonacciSequence;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.observables.GroupedObservable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GroupByExample1 {

    private final static Logger log = LoggerFactory.getLogger(GroupByExample1.class);

    private enum GroupTypeEnum {
        EVEN,
        ODD
    }

    public static void main(String[] args) {

        // Make an Observable from the Fibonacci sequence and group by
        // odd vs even numbers.
        Observable<GroupedObservable<GroupTypeEnum, Long>> groupedFibonacci =
                FibonacciSequence.create(20)
                .groupBy(
                        nextNumber -> nextNumber % 2L == 0L ?
                                GroupTypeEnum.EVEN :
                                GroupTypeEnum.ODD
                );

        // The stream comes through as two GroupedObservables of Long (the group key) and
        // Long values.
        groupedFibonacci.subscribe(
                nextGroupedObservable -> {
                    // Get the key for this grouped observable and determine
                    // a header for it.
                    String header = nextGroupedObservable.getKey().name();

                    // Subscribe to this GroupedObservable to process the values.
                    nextGroupedObservable.subscribe(new Observer<Long>() {

                        // StringBuilder used to concatenate the values coming
                        // from the stream.
                        private StringBuilder valueList = new StringBuilder();

                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Long nextValue) {

                            // Add a comma if the buffer isn't empty
                            if (valueList.length() > 0) {
                                valueList.append(", ");
                            }

                            // Add the next value from the Observable
                            valueList.append(nextValue);
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {
                            log.info("{} - {}", header, valueList.toString());
                        }
                    });
                });

        System.exit(0);
    }
}
