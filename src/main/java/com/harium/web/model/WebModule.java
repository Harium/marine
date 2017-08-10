package com.harium.web.model;

import spark.Request;

import java.util.Map;

public interface WebModule {
    void init();
    void buildModel(Request request, Map<String, Object> model);
}
