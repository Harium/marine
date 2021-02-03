package com.harium.marine.model;

import spark.Request;

import java.util.Map;

public interface WebModule {
    void init();
    default void buildModel(Request request, Map<String, Object> model) {

    }
}
