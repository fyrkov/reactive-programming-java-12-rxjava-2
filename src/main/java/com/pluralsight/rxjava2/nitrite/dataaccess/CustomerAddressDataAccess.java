package com.pluralsight.rxjava2.nitrite.dataaccess;

import com.pluralsight.rxjava2.nitrite.entity.CustomerAddress;
import io.reactivex.Observable;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.filters.ObjectFilters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class CustomerAddressDataAccess {

    private static final Logger log = LoggerFactory.getLogger(CustomerAddressDataAccess.class);

    public static Observable<CustomerAddress> select(Nitrite db, UUID customerId) {

        return Observable.generate( () -> db.getRepository(CustomerAddress.class)
                .find(ObjectFilters.eq("customerId", customerId))
                .iterator(),
                (customerAddressIterator, emitter) -> {
                    try {
                        if (customerAddressIterator.hasNext()) {
                            CustomerAddress nextAddress = customerAddressIterator.next();
                            log.info( "onNext - {}", nextAddress);
                            emitter.onNext(nextAddress);
                        } else {
                            log.info("onComplete");
                            emitter.onComplete();
                        }
                    }
                    catch( Throwable t ) {
                        log.error(t.getMessage(),t);
                        emitter.onError(t);
                    }
                }
                ).doOnSubscribe(disposable -> {
                    log.info("onSubscribe");
                })
                .cast(CustomerAddress.class);
    }
}
