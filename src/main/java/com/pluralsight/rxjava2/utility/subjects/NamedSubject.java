package com.pluralsight.rxjava2.utility.subjects;

import io.reactivex.subjects.Subject;

public class NamedSubject<TEventType> extends SelectableSubject<TEventType> {

    private String subjectName;

    public NamedSubject(String subjectName) {
        this.subjectName = subjectName;
    }

    public NamedSubject(String subjectName, Subject<TEventType> subjectToUse) {
        super(subjectToUse);
        this.subjectName = subjectName;
    }

    public String getSubjectName() {
        return subjectName;
    }
}
