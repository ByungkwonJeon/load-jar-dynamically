
package com.example.external;

import org.springframework.stereotype.Service;

@Service
public class ExternalService {

    public String getMessage(String name) {
        return "Hello " + name + " from external JAR";
    }
}
