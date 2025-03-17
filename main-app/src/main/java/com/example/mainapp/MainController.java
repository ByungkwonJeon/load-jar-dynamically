package com.example.mainapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/main")
public class MainController {

    @Autowired
    private ApplicationContext context;

    @GetMapping("/test-external")
    public String callExternalService(@RequestParam String name) {
        try {
            Object service = context.getBean("ExternalService");

            String bean = context.getBean("externalBean", String.class);
            System.out.println("Loaded bean: " + bean);

            // Call the method using reflection with a parameter
            return (String) service.getClass()
                    .getMethod("getMessage", String.class)
                    .invoke(service, name);
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to call external service";
        }
    }
}