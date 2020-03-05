package com.pluralsight.rxjava2.nitrite.entity;

import org.dizitart.no2.IndexType;
import org.dizitart.no2.objects.Id;
import org.dizitart.no2.objects.Index;
import org.dizitart.no2.objects.Indices;

import java.io.Serializable;
import java.util.UUID;

@Indices({
        @Index( value = "numberValue", type = IndexType.Unique)
})
public class FibonacciNumber implements Serializable {

    @Id
    private UUID fibonacciNumberId;

    private long numberValue;

    public FibonacciNumber(UUID fibonacciNumberId, long numberValue) {
        this.fibonacciNumberId = fibonacciNumberId;
        this.numberValue = numberValue;
    }

    public FibonacciNumber() {
    }

    public UUID getFibonacciNumberId() {
        return fibonacciNumberId;
    }

    public void setFibonacciNumberId(UUID fibonacciNumberId) {
        this.fibonacciNumberId = fibonacciNumberId;
    }

    public long getNumberValue() {
        return numberValue;
    }

    public void setNumberValue(long numberValue) {
        this.numberValue = numberValue;
    }
}
