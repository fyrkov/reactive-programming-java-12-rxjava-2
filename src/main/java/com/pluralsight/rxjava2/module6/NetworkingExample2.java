package com.pluralsight.rxjava2.module6;

import com.pluralsight.rxjava2.utility.GateBasedSynchronization;
import com.pluralsight.rxjava2.utility.network.HttpResponseObserverFactory;
import com.pluralsight.rxjava2.utility.subscribers.DemoSubscriber;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.concurrent.TimeUnit;

public class NetworkingExample2 {

    private static final Logger log = LoggerFactory.getLogger(NetworkingExample2.class);

    public static void main(String[] args) {

        try {
            // Synchronization gates
            GateBasedSynchronization gate = new GateBasedSynchronization();

            // Create two requests to our addition service.  The first will have a long delay
            URI request1 = new URI("http://localhost:22221/addition?a=5&b=9&delay=6000");

            // The second request will no have a delay.
            URI request2 = new URI("http://localhost:22221/addition?a=21&b=21&delay=0");

            // Use our HttpResponseObserverFactory to makeObservable an Observable that returns
            // the result of the call to the addition service for the first request.
            // Note that we are placing this request on the IO thread pool since it
            // will be waiting on IO predominantly.
            Observable<Integer> networkRequest1 =
                    HttpResponseObserverFactory.additionRequestResponseObservable(request1)
                    .subscribeOn(Schedulers.io());

            // ...and another for the second request.
            Observable<Integer> networkRequest2 =
                    HttpResponseObserverFactory.additionRequestResponseObservable(request2)
                    .subscribeOn(Schedulers.io());

            // We use the merge operator with maxConcurrency of 2 in order
            // to cause both networkRequest1 and networkRequest2 to be executed
            // simultaneously.  We want all of this on the IO threads.
            // We also set a timeout of 5 seconds for the requests.
            Observable<Integer> responseStream = Observable.mergeArray(
                    2, 1, networkRequest1, networkRequest2)
                    .subscribeOn(Schedulers.io())
                    .timeout(5L, TimeUnit.SECONDS , Observable.just(-1));

            // No that we have our Observable chain, we can use our standard
            // DemoSubscriber to cause it to execute.
            responseStream.subscribe(new DemoSubscriber<>(gate));

            // We wait for success or failure.
            gate.waitForAny("onError", "onComplete");

        } catch (Throwable e) {
            log.error(e.getMessage(),e);
        }

    }
}
