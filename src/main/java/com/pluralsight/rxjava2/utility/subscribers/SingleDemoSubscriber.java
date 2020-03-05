package com.pluralsight.rxjava2.utility.subscribers;

import com.pluralsight.rxjava2.utility.GateBasedSynchronization;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SingleDemoSubscriber<TEvent> implements SingleObserver<TEvent> {

    private static final Logger log = LoggerFactory.getLogger(SingleDemoSubscriber.class);

    private final GateBasedSynchronization gate;
    private final String errorGateName;
    private final String successGateName;

    public SingleDemoSubscriber() {
        this.gate = new GateBasedSynchronization();
        this.errorGateName = "onError";
        this.successGateName = "onSuccess";
    }

    public SingleDemoSubscriber(GateBasedSynchronization gate, String errorGateName, String successGateName) {
        this.gate = gate;
        this.errorGateName = errorGateName;
        this.successGateName = successGateName;
    }

    @Override
    public void onSubscribe(Disposable d) {
        log.info( "onSubscribe" );
    }

    @Override
    public void onSuccess(TEvent tEvent) {
        log.info( "onSuccess - {}" , tEvent);
        gate.openGate(successGateName);
    }

    @Override
    public void onError(Throwable e) {
        log.error( "onError - {}" , e.getMessage());
        log.error(e.getMessage(),e);
        gate.openGate(errorGateName);
    }
}
