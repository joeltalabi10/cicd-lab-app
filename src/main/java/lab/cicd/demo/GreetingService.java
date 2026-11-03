package lab.cicd.demo;

import org.springframework.stereotype.Service;

@Service
public class GreetingService {
    public String greet(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        return "Hello, " + name.trim() + "!";
    }

    public String farewell(String name, boolean formal) {
        if (name == null || name.isBlank()) {
            return "Goodbye!";
        }
        if (formal) {
            return "Farewell, " + name.trim() + ".";
        }
        return "Bye, " + name.trim() + "!";
    }
}
