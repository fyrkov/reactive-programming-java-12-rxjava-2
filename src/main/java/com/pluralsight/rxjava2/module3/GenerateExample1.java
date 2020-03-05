package com.pluralsight.rxjava2.module3;

import com.pluralsight.rxjava2.utility.subscribers.DemoSubscriber;
import io.reactivex.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenerateExample1 {

    private final static Logger log = LoggerFactory.getLogger(GenerateExample1.class);

    public static void main(String[] args) {

        Observable<Integer> geometricSequence = makeObservable(1,2,8);
        geometricSequence.subscribe(new DemoSubscriber<>());
    }

    public static Observable<Integer> makeObservable(int start , int multiplier, int totalNumbers ) {

        return
                Observable.generate(() -> new GeometricSequenceState(start,multiplier,totalNumbers),
                (state, emitter) -> {

                    // If we have reached the end, then emit and onComplete.
                    if(state.getCount() >= state.getTotalNumbers()) {
                        emitter.onComplete();
                        return;
                    }

                    // Increment the number of values we have emitted
                    state.incrementCount();

                    // Emit the currently calculated
                    // value of the geometric sequence.
                    emitter.onNext(state.getCurrentValue());

                    // Calculate the next value in the sequence.
                    state.generateNextValue();
                });

    }


    public static class GeometricSequenceState {

        private final int multiplier;
        private final int totalNumbers;

        private int count;
        private int currentValue;

        public GeometricSequenceState(int start, int multiplier, int totalNumbers) {
            this.multiplier = multiplier;
            this.totalNumbers = totalNumbers;

            this.count = 0;
            this.currentValue = start;
        }

        public int getTotalNumbers() {
            return totalNumbers;
        }

        public int getCount() {
            return count;
        }

        public int getCurrentValue() {
            return currentValue;
        }

        public void incrementCount() {
            ++this.count;
        }

        public void generateNextValue() {
            this.currentValue = this.currentValue * this.multiplier;
        }
    }
}
