package com.example.translationapplication.http;

public class ProviderImplement implements ProviderInterface {
    @Override
    public ServiceInterface newService() {
        return new HttpService();
    }
}
