package com.pluralsight.rxjava2.module5;

import com.pluralsight.rxjava2.utility.MutableReference;
import com.pluralsight.rxjava2.utility.ThreadHelper;
import com.pluralsight.rxjava2.utility.events.AccountCredentialsUpdatedEvent;
import com.pluralsight.rxjava2.utility.events.EventBase;
import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class UserServiceEventObservable {

    private static final String[] emailList = new String[] {
        "test@test.com",
        "harold@pottery.com",
        "lazerus@moonslider.com",
        "mrwaterwings@deepwater.com"
    };

    public static Observable<EventBase> userServiceEventGenerator() {

        return Observable.generate(
                () -> new MutableReference<Integer>(0),
                (offset, eventBaseEmitter) -> {

                    // Restrict the offset to the size of our email list.
                    if( offset.getValue() >= emailList.length ) {

                        // If we are at the end of the list, then send
                        // the onComplete.
                        eventBaseEmitter.onComplete();
                    }
                    else {
                        // We are still in the list...send an update event with
                        // the correct email address.
                        eventBaseEmitter.onNext(new AccountCredentialsUpdatedEvent(emailList[offset.getValue()]));
                    }

                    // Increment out array offset
                    offset.setValue(offset.getValue() + 1);

                    // Slow things down
                    ThreadHelper.sleep(1, TimeUnit.SECONDS);
                }
        );


    }
}
