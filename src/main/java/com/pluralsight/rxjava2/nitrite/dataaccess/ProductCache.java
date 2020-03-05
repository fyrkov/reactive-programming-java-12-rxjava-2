package com.pluralsight.rxjava2.nitrite.dataaccess;

import com.pluralsight.rxjava2.nitrite.entity.Product;
import org.dizitart.no2.Nitrite;

import java.util.HashMap;
import java.util.UUID;

public class ProductCache {

    private static HashMap<UUID, Product> cacheData = new HashMap<>();
    private static Object cacheLoadLock = new Object();

    private static void initialize(Nitrite db) {

        synchronized (cacheLoadLock) {
            if( cacheData.size() == 0 ) {
                ProductDataAccess.select(db)
                        .subscribe(nextProduct -> cacheData.put(nextProduct.getProductId(), nextProduct));
            }
        }
    }

    public static Product getProduct(Nitrite db, UUID productId) {
        initialize(db);

        return cacheData.get(productId);
    }
}
