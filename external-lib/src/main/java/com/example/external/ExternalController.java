
package com.example.external;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/external")
public class ExternalController {

    @GetMapping("/test")
    public String test() {
        return "Hello from external JAR!";
    }
}
