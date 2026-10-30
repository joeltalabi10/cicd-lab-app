package lab.cicd.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class GreetingControllerTest {
    @Test
    void delegatesToService() {
        var controller = new GreetingController(new GreetingService());
        assertEquals("Hello, Bo!", controller.greet("Bo"));
    }
}
