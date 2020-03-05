package com.pluralsight.rxjava2.utility.events;

public class AccountCredentialsUpdatedEvent extends EventBase {

    private String accountEmail;

    public AccountCredentialsUpdatedEvent(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    @Override
    public String toString() {
        return "AccountCredentialsUpdatedEvent{" +
                "accountEmail='" + accountEmail + '\'' +
                "} " + super.toString();
    }
}
