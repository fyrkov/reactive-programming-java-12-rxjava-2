package com.pluralsight.rxjava2.utility.subscribers;

import com.pluralsight.rxjava2.utility.GateBasedSynchronization;
import com.pluralsight.rxjava2.utility.ThreadHelper;
import io.reactivex.observers.ResourceObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class DemoSubscriber<TEvent> extends ResourceObserver<TEvent> {

    private static final Logger log = LoggerFactory.getLogger(DemoSubscriber.class);

    private final GateBasedSynchronization gate;
    private final String errorGateName;
    private final String completeGateName;

    private final long onNextDelayDuration;
    private final TimeUnit onNextDelayTimeUnit;

    public DemoSubscriber() {
        this.gate = new GateBasedSynchronization();
        this.errorGateName = "onError";
        this.completeGateName = "onComplete";
        this.onNextDelayDuration = 0L;
        this.onNextDelayTimeUnit = TimeUnit.SECONDS;
    }

    public DemoSubscriber(GateBasedSynchronization gate) {
        this.gate = gate;
        this.errorGateName = "onError";
        this.completeGateName = "onComplete";

        this.onNextDelayDuration = 0L;
        this.onNextDelayTimeUnit = TimeUnit.SECONDS;
    }

    public DemoSubscriber(GateBasedSynchronization gate, String errorGateName, String completeGateName) {
        this.gate = gate;
        this.errorGateName = errorGateName;
        this.completeGateName = completeGateName;

        this.onNextDelayDuration = 0L;
        this.onNextDelayTimeUnit = TimeUnit.SECONDS;
    }

    public DemoSubscriber(long onNextDelayDuration , TimeUnit onNextDelayTimeUnit, GateBasedSynchronization gate,
                          String errorGateName, String completeGateName) {
        this.gate = gate;
        this.errorGateName = errorGateName;
        this.completeGateName = completeGateName;

        this.onNextDelayDuration = onNextDelayDuration;
        this.onNextDelayTimeUnit = onNextDelayTimeUnit;
    }

    public GateBasedSynchronization getGates() {
        return this.gate;
    }

    @Override
    public void onNext(TEvent event) {
        log.info( "onNext - {}" , event == null ? "<NULL>" : event.toString());

        // Drag our feet if requested to do so...
        if( onNextDelayDuration > 0 ) {
            ThreadHelper.sleep(onNextDelayDuration, onNextDelayTimeUnit);
        }
    }

    @Override
    public void onError(Throwable e) {
        log.error( "onError - {}" , e.getMessage());
        gate.openGate(errorGateName);
    }

    @Override
    public void onComplete() {
        log.info( "onComplete" );
        gate.openGate(completeGateName);
    }
}
