package com.pluralsight.rxjava2.nitrite.entity;

import org.dizitart.no2.objects.Id;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Product implements Serializable {

    @Id
    private UUID productId;

    private String productName;
    private String productSKU;

    public Product() {
    }

    public Product(UUID productId, String productName, String productSKU) {
        this.productId = productId;
        this.productName = productName;
        this.productSKU = productSKU;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return getProductId() == product.getProductId() &&
                Objects.equals(getProductName(), product.getProductName()) &&
                Objects.equals(getProductSKU(), product.getProductSKU());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProductId(), getProductName(), getProductSKU());
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSKU() {
        return productSKU;
    }

    public void setProductSKU(String productSKU) {
        this.productSKU = productSKU;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productSKU='" + productSKU + '\'' +
                '}';
    }
}
