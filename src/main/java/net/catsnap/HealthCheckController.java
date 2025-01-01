package net.catsnap;

import java.time.LocalDateTime;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping
    public String healthCheck() {
        return LocalDateTime.now().toString();
    }
}
