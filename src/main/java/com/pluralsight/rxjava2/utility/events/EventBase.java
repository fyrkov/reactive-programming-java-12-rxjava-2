package com.pluralsight.rxjava2.utility.events;

import java.util.UUID;

public class EventBase {

    private UUID eventUUID = UUID.randomUUID();

    public UUID getEventUUID() {
        return eventUUID;
    }

    @Override
    public String toString() {
        return "EventBase{" +
                "eventUUID=" + eventUUID +
                '}';
    }
}
