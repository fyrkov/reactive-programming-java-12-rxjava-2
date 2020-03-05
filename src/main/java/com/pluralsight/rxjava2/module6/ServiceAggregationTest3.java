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

public class ServiceAggregationTest3 {

    private final static Logger log = LoggerFactory.getLogger(ServiceAggregationTest3.class);

    public static void main(String[] args) {

        try {

            NitriteCustomerDatabaseSchema schema = new NitriteCustomerDatabaseSchema();
            try(NitriteTestDatabase testDatabase = new NitriteTestDatabase(Optional.of(schema))) {

                // Create an Observable for getting the customer.
                // Set the scheduler to use the IO thread pool
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
                Observable<Product> ownedProductListObservable =
                        CustomerProductPurchaseHistoryDataAccess.selectOwnedProducts(testDatabase.getNitriteDatabase(), schema.Customer1UUID)
                        .subscribeOn(Schedulers.io());

                // Merge is another operator that performs concurrent execution by subscribing
                // to the incoming Observables.  maxConcurrency for merge is always equal to the
                // number of Observables being passed in.
                Observable<Object> customerAggregateStream = Observable.merge(customerObservable.cast(Object.class),
                        customerAddressObservable.cast(Object.class), ownedProductListObservable.cast(Object.class));

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
