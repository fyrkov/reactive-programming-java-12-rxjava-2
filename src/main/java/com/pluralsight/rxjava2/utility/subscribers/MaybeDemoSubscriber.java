package com.pluralsight.rxjava2.utility.subscribers;

import com.pluralsight.rxjava2.utility.GateBasedSynchronization;
import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MaybeDemoSubscriber<TEvent> implements MaybeObserver<TEvent> {

    private static final Logger log = LoggerFactory.getLogger(MaybeDemoSubscriber.class);

    private final GateBasedSynchronization gate;
    private final String errorGateName;
    private final String successGateName;
    private final String completeGateName;

    public MaybeDemoSubscriber() {
        this.gate = new GateBasedSynchronization();
        this.errorGateName = "onError";
        this.successGateName = "onSuccess";
        this.completeGateName = "onComplete";
    }

    public MaybeDemoSubscriber(GateBasedSynchronization gate, String errorGateName, String successGateName, String completeGateName) {
        this.gate = gate;
        this.errorGateName = errorGateName;
        this.successGateName = successGateName;
        this.completeGateName = completeGateName;
    }

    @Override
    public void onSubscribe(Disposable d) {
        log.info( "onSubscribe" );
    }

    @Override
    public void onSuccess(TEvent tEvent) {
        log.info( "onSuccess - {}", tEvent );
        gate.openGate(successGateName);
    }

    @Override
    public void onError(Throwable e) {
        log.error( "onError - {}" , e.getMessage());
        log.error(e.getMessage(),e);
        gate.openGate(errorGateName);
    }

    @Override
    public void onComplete() {
        log.info( "onComplete" );
        gate.openGate(completeGateName);
    }
}
