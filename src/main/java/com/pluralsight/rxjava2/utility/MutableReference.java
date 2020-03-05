package com.pluralsight.rxjava2.utility;

import java.util.Optional;

public class MutableReference<TContainedType> {

    private Optional<TContainedType> value;

    public MutableReference() {
        value = Optional.empty();
    }

    public MutableReference(TContainedType containedValue) {
        this.value = Optional.ofNullable(containedValue);
    }

    public boolean hasValue() {
        return value.isPresent();
    }

    public TContainedType getValue() {
        return value.get();
    }

    public TContainedType getValue( TContainedType defaultValue ) {
        return value.orElse(defaultValue);
    }

    public void setValue(TContainedType newValue) {
        value = Optional.ofNullable(newValue);
    }
}
