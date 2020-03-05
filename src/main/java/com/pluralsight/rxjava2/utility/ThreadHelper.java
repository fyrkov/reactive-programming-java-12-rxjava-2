package com.pluralsight.rxjava2.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class ThreadHelper {

    private final static Logger log = LoggerFactory.getLogger(ThreadHelper.class);

    public static void sleep(long duration , TimeUnit timeUnit ) {
        try {
            log.info( "Sleeping for {} ms", timeUnit.toMillis(duration));

            timeUnit.sleep(duration);
        } catch (InterruptedException e) {

        }
    }
}
