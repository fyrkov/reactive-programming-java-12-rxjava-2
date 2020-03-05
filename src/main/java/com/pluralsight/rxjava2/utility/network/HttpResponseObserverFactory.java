package com.pluralsight.rxjava2.utility.network;

import io.reactivex.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class HttpResponseObserverFactory {

    private final static Logger log = LoggerFactory.getLogger(HttpResponseObserverFactory.class);

    public static Observable<Integer> additionRequestResponseObservable(URI httpUri) {

        log.info("Creating observable for: {}", httpUri.toString());

        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(20))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(httpUri)
                .GET()
                .build();

        CompletableFuture<HttpResponse<String>> futureResponse =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString(Charset.forName("UTF-8")));

        Observable<Integer> returnObservable = Observable.fromFuture(futureResponse)
                .map( stringHttpResponse -> stringHttpResponse.body() )
                .map( bodyString -> Integer.parseInt( bodyString ))
                .doOnNext( responseInteger -> log.info("Response: {}", responseInteger));

        return returnObservable;
    }
}
