package com.pluralsight.rxjava2.nitrite.utility;

import org.dizitart.no2.objects.Cursor;

import java.util.Iterator;

public class NitriteFlowableCursorState<TEntityType> {

    private Cursor<TEntityType> cursor;
    private Iterator<TEntityType> iterator;

    public NitriteFlowableCursorState(Cursor<TEntityType> cursor) {
        this.cursor = cursor;
        this.iterator = cursor.iterator();
    }

    public Iterator<TEntityType> getIterator() {
        return iterator;
    }
}
