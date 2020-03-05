package com.pluralsight.rxjava2.nitrite.dataaccess;

import com.pluralsight.rxjava2.nitrite.entity.Customer;
import com.pluralsight.rxjava2.nitrite.utility.NitriteFlowableCursorState;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.filters.ObjectFilters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.UUID;

public class CustomerDataAccess {

    private final static Logger log = LoggerFactory.getLogger(CustomerDataAccess.class);

    public static Observable<Customer> select(Nitrite db, UUID customerId) {

        return Observable.generate(() -> db.getRepository(Customer.class)
                .find(ObjectFilters.eq("customerId", customerId)).iterator(),
                (customers, objectEmitter) -> {
                    try {
                        if (customers.hasNext()) {
                            Customer nextCustomer = customers.next();
                            log.info("onNext - {}", nextCustomer);
                            objectEmitter.onNext(nextCustomer);
                        } else {
                            log.info("onComplete");
                            objectEmitter.onComplete();
                        }
                    }
                    catch(Throwable t) {
                        log.error(t.getMessage(),t);
                        objectEmitter.onError(t);
                    }
                }
                )
                .doOnSubscribe(disposable -> log.info("onSubscribe"))
                .cast(Customer.class);
    }

    public static Flowable<Customer> select(Nitrite db){

        return Flowable.generate(
            () -> new NitriteFlowableCursorState(db.getRepository(Customer.class).find()),
                (state, customerEmitter) -> {
                    try {
                        Iterator<Customer> customerIterator = state.getIterator();
                        if (customerIterator.hasNext() == false) {
                            customerEmitter.onComplete();
                        } else {

                            customerEmitter.onNext(customerIterator.next());
                        }
                    }
                    catch(Throwable t) {
                        customerEmitter.onError(t);
                    }
                }
        );
    }
}
