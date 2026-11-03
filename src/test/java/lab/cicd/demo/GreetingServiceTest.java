package lab.cicd.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class GreetingServiceTest {
    private final GreetingService service = new GreetingService();

    @Test
    void greetsByName() {
        assertEquals("Hello, Ana!", service.greet("Ana"));
    }

    @Test
    void trimsWhitespace() {
        assertEquals("Hello, Ana!", service.greet("  Ana "));
    }
}
