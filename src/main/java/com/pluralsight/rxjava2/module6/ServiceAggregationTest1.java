package com.pluralsight.rxjava2.module6;

import com.pluralsight.rxjava2.nitrite.NitriteTestDatabase;
import com.pluralsight.rxjava2.nitrite.aggregate.CustomerAggregate;
import com.pluralsight.rxjava2.nitrite.aggregate.CustomerAggregateOperations;
import com.pluralsight.rxjava2.nitrite.dataaccess.CustomerAddressDataAccess;
import com.pluralsight.rxjava2.nitrite.dataaccess.CustomerDataAccess;
import com.pluralsight.rxjava2.nitrite.dataaccess.CustomerProductPurchaseHistoryDataAccess;
import com.pluralsight.rxjava2.nitrite.datasets.NitriteCustomerDatabaseSchema;
import com.pluralsight.rxjava2.nitrite.entity.Customer;
import com.pluralsight.rxjava2.nitrite.entity.CustomerAddress;
import com.pluralsight.rxjava2.nitrite.entity.Product;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class ServiceAggregationTest1 {

    private final static Logger log = LoggerFactory.getLogger(ServiceAggregationTest1.class);

    public static void main(String[] args) {

        try {

            NitriteCustomerDatabaseSchema schema = new NitriteCustomerDatabaseSchema();
            try(NitriteTestDatabase testDatabase = new NitriteTestDatabase(Optional.of(schema))) {

                // Create an Observable for getting the customer.
                // Because we are going to make these observables execute concurrently,
                // we set them to subscribeOn the I/O thread scheduler.  All concurrent
                // observe by performing a new subscription on each observable given.
                Observable<Customer> customerObservable =
                        CustomerDataAccess.select(testDatabase.getNitriteDatabase(), schema.Customer1UUID)
                        .subscribeOn(Schedulers.io());

                // Create an Observable to get the Customer's Address information
                // Again, make sure we are using the IO thread pool.
                Observable<CustomerAddress> customerAddressObservable =
                        CustomerAddressDataAccess.select(testDatabase.getNitriteDatabase(), schema.Customer1UUID)
                        .subscribeOn(Schedulers.io());


                // Create an Observable that will give us the Product list that is owned by the customer.
                // ...on the IO thread pool
                Observable<Product> ownedProductList =
                        CustomerProductPurchaseHistoryDataAccess.selectOwnedProducts(testDatabase.getNitriteDatabase(), schema.Customer1UUID)
                        .subscribeOn(Schedulers.io());

                // Nothing has actually happened yet...we just have a bunch of
                // Observables for the 3 sources we want to pull from.
                // Now let's concat together the results into an Observable<Object>.
                // Concat does not execute the observables concurrently!
                Observable<Object> customerAggregateStream =
                        Observable.concat(
                            customerObservable.cast(Object.class),
                            customerAddressObservable.cast(Object.class),
                            ownedProductList.cast(Object.class)
                        );

                // Let's assemble a CustomerAggregate.
                Single<CustomerAggregate> customerAggregate = CustomerAggregateOperations.aggregate(customerAggregateStream);

                // Get the aggregated customer data!
                CustomerAggregate finalCustomer = customerAggregate.blockingGet();

                log.info(finalCustomer.toString());
            }
        }
        catch( Throwable t ) {
            log.error(t.getMessage(),t);
        }

        System.exit(0);
    }
}
