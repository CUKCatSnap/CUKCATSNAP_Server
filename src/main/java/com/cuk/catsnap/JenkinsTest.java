package com.cuk.catsnap;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JenkinsTest {

    @GetMapping
    public String test() {
        return "Hello Jenkins!";
    }
}
