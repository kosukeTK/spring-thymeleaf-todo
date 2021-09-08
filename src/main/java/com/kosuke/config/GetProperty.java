package com.kosuke.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GetProperty {

    @Value("${spring.datasource.url}")
    private String name;
    
    
    public String getMessage() {
//        return String.format("Hello! My name is %s", name);
    	return String.format("Hello! My name is tomato!");
    }

}
