package com.example.sportsballistics.data.api.network_interceptor;

import java.io.IOException;

public class NoConnectivityException extends IOException {

    @Override
    public String getMessage() {
        return "NetworkError";
        // You can send any message whatever you want from here.
    }
}