package com.pluralsight.rxjava2.nitrite.entity;

import org.dizitart.no2.IndexType;
import org.dizitart.no2.objects.Id;
import org.dizitart.no2.objects.Index;
import org.dizitart.no2.objects.Indices;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Indices( {
        @Index(value = "customerId", type = IndexType.NonUnique)
})
public class CustomerAddress implements Serializable {

    @Id
    private UUID customerAddressId;

    private UUID customerId;

    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;

    public CustomerAddress() {
    }

    public CustomerAddress(UUID customerAddressId, UUID customerId, String addressLine1, String addressLine2, String city, String state, String postalCode) {
        this.customerAddressId = customerAddressId;
        this.customerId = customerId;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerAddress that = (CustomerAddress) o;
        return getCustomerAddressId() == that.getCustomerAddressId() &&
                Objects.equals(getAddressLine1(), that.getAddressLine1()) &&
                Objects.equals(getAddressLine2(), that.getAddressLine2()) &&
                Objects.equals(getCity(), that.getCity()) &&
                Objects.equals(getState(), that.getState()) &&
                Objects.equals(getPostalCode(), that.getPostalCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCustomerAddressId(), getAddressLine1(), getAddressLine2(), getCity(), getState(), getPostalCode());
    }

    public UUID getCustomerAddressId() {
        return customerAddressId;
    }

    public void setCustomerAddressId(UUID customerAddressId) {
        this.customerAddressId = customerAddressId;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "CustomerAddress{" +
                "customerAddressId=" + customerAddressId +
                ", customerId=" + customerId +
                ", addressLine1='" + addressLine1 + '\'' +
                ", addressLine2='" + addressLine2 + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", postalCode='" + postalCode + '\'' +
                '}';
    }
}
