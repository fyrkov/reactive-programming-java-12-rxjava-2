package com.pluralsight.rxjava2.utility.subjects;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import java.util.HashMap;

public class SelectableSubject<TEventType> {

    private Subject<TEventType> internalSubject;
    private HashMap<Observable<TEventType>, Disposable> producerTrackingMap;
    private HashMap<Observer<TEventType>, Disposable> consumerTrackingMap;

    public SelectableSubject() {
        this.producerTrackingMap = new HashMap<>();
        this.consumerTrackingMap = new HashMap<>();
        this.internalSubject = PublishSubject.create();
    }

    public SelectableSubject(Subject<TEventType> subjectToUse) {
        this.producerTrackingMap = new HashMap<>();
        this.consumerTrackingMap = new HashMap<>();
        this.internalSubject = subjectToUse;
    }

    public synchronized void addEventProducer(Observable<TEventType> newEventSource) {

        // Have the internalSubject subscribe to the incoming event source.
        newEventSource.subscribe(
                internalSubject::onNext,
                internalSubject::onError,
                internalSubject::onComplete,
                disposable -> {
                    // Intercept the onSubscribe and track the associated disposable.
                    this.producerTrackingMap.put(newEventSource, disposable);

                    // Pass the event along to the Subject
                    this.internalSubject.onSubscribe(disposable);
                }
        );
    }

    public synchronized void removeEventProducer(Observable<TEventType> eventSourceToRemove) {

        if( producerTrackingMap.containsKey(eventSourceToRemove)) {

            // Remove the tracking reference and call dispose to stop
            // the flow of messages to the subject.
            producerTrackingMap.remove(eventSourceToRemove).dispose();
        }
    }

    public synchronized void addEventConsumer(Observer<TEventType> newConsumer) {

        internalSubject.subscribe(
            newConsumer::onNext,
            newConsumer::onError,
            newConsumer::onComplete,
            disposable -> {

                // Intercept the disposable for this subscription
                consumerTrackingMap.put(newConsumer, disposable);

                // Pass the onSubscribe along to the Observer.
                newConsumer.onSubscribe(disposable);
            }
        );
    }

    public synchronized void detachEventConsumer(Observer<TEventType> consumerToRemove) {

        if( consumerTrackingMap.containsKey(consumerToRemove)) {

            // Remove the reference from the tracking map and then
            // call dispose to stop the flow of events.
            consumerTrackingMap.remove(consumerToRemove).dispose();
        }
    }
}
