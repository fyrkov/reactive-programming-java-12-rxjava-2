package com.pluralsight.rxjava2.nitrite.datasets;

import com.pluralsight.rxjava2.nitrite.NitriteSchema;
import com.pluralsight.rxjava2.nitrite.entity.LetterPair;
import com.pluralsight.rxjava2.utility.datasets.GreekAlphabet;
import io.reactivex.Observable;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;

import java.util.List;

public class NitriteGreekAlphabetSchema implements NitriteSchema {

    @Override
    public void applySchema(Nitrite db) {

        // Make a collection to hold the greek alphabet
        ObjectRepository<LetterPair> letterRepo = db.getRepository(LetterPair.class);

        // See if it's already populated
        if( letterRepo.find().totalCount() == 0 ) {

            // Make a LetterPair for each letter in the greek alphabet
            List<LetterPair> letterList = Observable.zip(
                    GreekAlphabet.greekAlphabetInGreekObservable(),
                    GreekAlphabet.greekAlphabetInEnglishObservable(),
                    (greek, english) -> new LetterPair(greek, english))
                    .toList()
                    .blockingGet();

            letterRepo.insert(letterList.toArray(new LetterPair[letterList.size()]));
        }
    }
}
