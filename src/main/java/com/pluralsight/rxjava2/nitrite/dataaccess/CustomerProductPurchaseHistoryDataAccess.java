package com.pluralsight.rxjava2.nitrite.dataaccess;

import com.pluralsight.rxjava2.nitrite.entity.CustomerProductPurchaseHistory;
import com.pluralsight.rxjava2.nitrite.entity.Product;
import io.reactivex.Observable;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.filters.ObjectFilters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class CustomerProductPurchaseHistoryDataAccess {

    private final static Logger log = LoggerFactory.getLogger(CustomerProductPurchaseHistoryDataAccess.class);

    public static Observable<CustomerProductPurchaseHistory> selectByCustomer(Nitrite db, UUID customerId) {

        return Observable.generate(() -> db.getRepository(CustomerProductPurchaseHistory.class)
                .find(ObjectFilters.eq("customerId", customerId))
                .iterator(),
                (historyIterator, emitter) -> {


                    try {
                        if (historyIterator.hasNext()) {
                            CustomerProductPurchaseHistory nextHistory = historyIterator.next();
                            log.info("onNext - {}", nextHistory);
                            emitter.onNext(nextHistory);
                        } else {
                            log.info("onComplete");
                            emitter.onComplete();
                        }
                    }
                    catch(Throwable t) {
                        log.error(t.getMessage(),t);
                        emitter.onError(t);
                    }
                }
                )
                .doOnSubscribe(disposable -> log.info("onSubscribe"))
                .cast(CustomerProductPurchaseHistory.class);
    }

    public static Observable<Product> selectOwnedProducts(Nitrite db, UUID customerId) {

        return selectByCustomer(db,customerId)
                // Map the purchase history into a list of products
                .map(customerProductPurchaseHistory -> ProductCache.getProduct(db, customerProductPurchaseHistory.getProductId()));
    }
}
