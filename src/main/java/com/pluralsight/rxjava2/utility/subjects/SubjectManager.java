 package com.pluralsight.rxjava2.utility.subjects;

import io.reactivex.Observable;
import io.reactivex.Observer;

import java.util.HashMap;

public class SubjectManager {

    private HashMap<String, NamedSubject> subjectMap;

    public SubjectManager() {
        this.subjectMap = new HashMap<>();
    }

    public void registerSubject( NamedSubject namedSubject ) {
        subjectMap.put(namedSubject.getSubjectName(), namedSubject);
    }

    public void deregisterSubject( NamedSubject namedSubject ) {
        subjectMap.remove(namedSubject.getSubjectName());
    }

    public void addEventProducer(String subjectName , Observable observable) {

        if( subjectMap.containsKey(subjectName)) {
            subjectMap.get(subjectName).addEventProducer(observable);
        }
    }

    public void addEventConsumer(String subjectName, Observer observer ) {

        if( subjectMap.containsKey(subjectName)) {
            subjectMap.get(subjectName).addEventConsumer(observer);
        }
    }
}
