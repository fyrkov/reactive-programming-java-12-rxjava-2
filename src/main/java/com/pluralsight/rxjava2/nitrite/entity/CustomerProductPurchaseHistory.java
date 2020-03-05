package com.pluralsight.rxjava2.nitrite.entity;

import org.dizitart.no2.IndexType;
import org.dizitart.no2.objects.Id;
import org.dizitart.no2.objects.Index;
import org.dizitart.no2.objects.Indices;

import java.util.Objects;
import java.util.UUID;

@Indices({
        @Index(value = "customerId", type = IndexType.NonUnique),
        @Index(value = "productId", type = IndexType.NonUnique)
})
public class CustomerProductPurchaseHistory {

    @Id
    private UUID customerProductPurchaseHistoryId;

    private UUID customerId;
    private UUID productId;

    public CustomerProductPurchaseHistory() {
    }

    public CustomerProductPurchaseHistory(UUID customerProductPurchaseHistoryId, UUID customerId, UUID productId) {
        this.customerProductPurchaseHistoryId = customerProductPurchaseHistoryId;
        this.customerId = customerId;
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerProductPurchaseHistory that = (CustomerProductPurchaseHistory) o;
        return Objects.equals(customerProductPurchaseHistoryId, that.customerProductPurchaseHistoryId) &&
                Objects.equals(customerId, that.customerId) &&
                Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerProductPurchaseHistoryId, customerId, productId);
    }

    public UUID getCustomerProductPurchaseHistoryId() {
        return customerProductPurchaseHistoryId;
    }

    public void setCustomerProductPurchaseHistoryId(UUID customerProductPurchaseHistoryId) {
        this.customerProductPurchaseHistoryId = customerProductPurchaseHistoryId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "CustomerProductPurchaseHistory{" +
                "customerProductPurchaseHistoryId=" + customerProductPurchaseHistoryId +
                ", customerId=" + customerId +
                ", productId=" + productId +
                '}';
    }
}
