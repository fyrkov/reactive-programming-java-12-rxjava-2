package com.pluralsight.rxjava2.nitrite.datasets;

import com.pluralsight.rxjava2.nitrite.NitriteSchema;
import com.pluralsight.rxjava2.nitrite.entity.Customer;
import com.pluralsight.rxjava2.nitrite.entity.CustomerAddress;
import com.pluralsight.rxjava2.nitrite.entity.CustomerProductPurchaseHistory;
import com.pluralsight.rxjava2.nitrite.entity.Product;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class NitriteCustomerDatabaseSchema implements NitriteSchema {

    private final static Logger log = LoggerFactory.getLogger(NitriteCustomerDatabaseSchema.class);

    public UUID Product1UUID;
    public UUID Product2UUID;
    public UUID Product3UUID;
    public UUID CustomerAddress1UUID;
    public UUID CustomerAddress2UUID;
    public UUID CustomerAddress3UUID;
    public UUID Customer1UUID;
    public UUID Customer2UUID;

    @Override
    public void applySchema(Nitrite db) {

        // Create the customer data
        createCustomerData(db);

        // Create the Customer Address information
        createCustomerAddressData(db);

        // Create the product data
        createProductData(db);

        // Create purchase history data
        createPurchaseHistoryData(db);
    }

    private void createProductData(Nitrite db) {

        ObjectRepository<Product> productRepo = db.getRepository(Product.class);

        if( productRepo.find().totalCount() == 0 ) {

            // Create 3 products
            Product product1 = new Product(UUID.randomUUID(), "Galoshes of Mud Walking",      "09498772650");
            Product product2 = new Product(UUID.randomUUID(), "Stick of Back Scratching +2", "32982349872");
            Product product3 = new Product(UUID.randomUUID(), "Soap of Cleansing",      "48243939874");

            Product1UUID = product1.getProductId();
            Product2UUID = product2.getProductId();
            Product3UUID = product3.getProductId();

            productRepo.insert(product1,product2,product3);
        }
    }

    private void createCustomerAddressData(Nitrite db) {
        // Make sure the CustomerAddress collection is created.
        ObjectRepository<CustomerAddress> addressRepo = db.getRepository(CustomerAddress.class);

        // If there's no data, then add some.
        if( addressRepo.find().totalCount() == 0 ) {

            // Create 3 addresses for assignment to our 2 customers
            CustomerAddress address1 = new CustomerAddress(
                    UUID.randomUUID(), Customer1UUID,
                    "4448 Wumpus Way","Suite 404", "Bedford", "TN", "37554");
            CustomerAddress address2 = new CustomerAddress(
                    UUID.randomUUID(), Customer1UUID,
                    "19564 Hunter Street","", "Walabash", "NC", "27344");
            CustomerAddress address3 = new CustomerAddress(
                    UUID.randomUUID(), Customer2UUID,
                    "332 Dunder-Muffler Road","Ste 554", "Papertown", "PA", "17845");

            addressRepo.insert(address1, address2, address3);

            CustomerAddress1UUID = address1.getCustomerAddressId();
            CustomerAddress2UUID = address2.getCustomerAddressId();
            CustomerAddress3UUID = address3.getCustomerAddressId();
        }
    }

    private void createCustomerData(Nitrite db) {

        // Make sure the Customer collection is created.
        ObjectRepository<Customer> customerRepo = db.getRepository(Customer.class);

        if( customerRepo.find().totalCount() == 0 ) {

            // Create 2 customers
            Customer customer1 = new Customer(UUID.randomUUID(), "Donald" , "Vanner");
            Customer customer2 = new Customer(UUID.randomUUID(), "Lawrence" , "Spacestrider");

            customerRepo.insert(customer1, customer2);

            Customer1UUID = customer1.getCustomerId();
            Customer2UUID = customer2.getCustomerId();
        }
    }

    private void createPurchaseHistoryData(Nitrite db) {

        ObjectRepository<CustomerProductPurchaseHistory> purchaseRepo = db.getRepository(CustomerProductPurchaseHistory.class);

        if(purchaseRepo.find().totalCount() == 0 ) {

            // Create purchase records for customer 1
            CustomerProductPurchaseHistory purchase1 = new CustomerProductPurchaseHistory(
                    UUID.randomUUID(), Customer1UUID, Product1UUID
            );
            CustomerProductPurchaseHistory purchase2 = new CustomerProductPurchaseHistory(
                    UUID.randomUUID(), Customer2UUID, Product2UUID
            );
            CustomerProductPurchaseHistory purchase3 = new CustomerProductPurchaseHistory(
                    UUID.randomUUID(), Customer2UUID, Product3UUID
            );

            purchaseRepo.insert(purchase1, purchase2, purchase3);
        }
    }
}
