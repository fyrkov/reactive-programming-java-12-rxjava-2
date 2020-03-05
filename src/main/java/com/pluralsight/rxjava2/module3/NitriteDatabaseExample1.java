package com.pluralsight.rxjava2.module3;

import com.pluralsight.rxjava2.nitrite.NitriteTestDatabase;
import com.pluralsight.rxjava2.nitrite.datasets.NitriteGreekAlphabetSchema;
import com.pluralsight.rxjava2.nitrite.entity.LetterPair;
import com.pluralsight.rxjava2.utility.GateBasedSynchronization;
import com.pluralsight.rxjava2.utility.subscribers.DemoSubscriber;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.Cursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.Optional;

public class NitriteDatabaseExample1 {

    private final static Logger log = LoggerFactory.getLogger(NitriteDatabaseExample1.class);

    public static void main(String[] args) {

        try {

            GateBasedSynchronization gate = new GateBasedSynchronization();

            // Create a test database that has a "LetterPair" collection of
            // Greek and their corresponding English pronunciations.
            try(NitriteTestDatabase database = new NitriteTestDatabase(Optional.of(new NitriteGreekAlphabetSchema()))) {

                Observable<LetterPair> letterPairs = createLetterPairObservable(database);
                letterPairs
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.computation())
                    .subscribe(new DemoSubscriber<>(gate, "onComplete", "onError"));

                // Wait here...because on the other side of the try with resources the database
                // will be closed.
                gate.waitForAny("onComplete", "onError");
            }
        }
        catch( IOException e ) {
            log.error(e.getMessage(),e);
        }

        System.exit(0);
    }

    private static Observable<LetterPair> createLetterPairObservable(NitriteTestDatabase database) {

        Observable<LetterPair> workObservable = Observable.generate(
                // We supply a callable that will generate a starting state for this
                // operation each time 'subscribe' is called on the Observable we are returning.
                () -> new NitriteCursorState<>(
                    database.getNitriteDatabase(),

                    // Setup the Nitrite "Cursor" class with a simple "find" which gets
                    // all documents from the collection "LetterPair".
                    database.getNitriteDatabase()
                            .getRepository(LetterPair.class)
                            .find()

        ), (state, emitter) -> {

            try {
                // if the iterator has more LetterPairs...
                if (state.getIterator().hasNext()) {

                    LetterPair nextLetterPair = state.getIterator().next();

                    log.info( "generator called - onNext {}", nextLetterPair );

                    // ...then emit an onNext with the given LetterPair
                    emitter.onNext(nextLetterPair);

                } else {
                    log.info( "generator called - onComplete" );

                    // No more LetterPairs...send an onComplete.
                    emitter.onComplete();
                }
            }
            catch( Throwable t ) {

                // If there is an error of any kind, make sure to tell
                // the subscriber.
                emitter.onError(t);
            }
        });

        //workObservable = workObservable.doOnNext(letterPair -> log.info("doOnNext - {}", letterPair));

        return workObservable;
    }

    public static class NitriteCursorState<TCursorType> {
        private Nitrite database;
        private Cursor<TCursorType> cursor;
        private Iterator<TCursorType> iterator;

        public NitriteCursorState(Nitrite database, Cursor<TCursorType> cursor) {
            this.database = database;
            this.cursor = cursor;

            this.iterator = cursor.iterator();
        }

        public Nitrite getDatabase() {
            return database;
        }

        public Cursor<TCursorType> getCursor() {
            return cursor;
        }

        public Iterator<TCursorType> getIterator() {
            return iterator;
        }
    }
}
