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

    @Test
    void rejectsBlank() {
        assertThrows(IllegalArgumentException.class, () -> service.greet(" "));
    }

    @Test
    void rejectsNull() {
        assertThrows(IllegalArgumentException.class, () -> service.greet(null));
    }

    @Test
    void farewellWithoutNameIsGeneric() {
        assertEquals("Goodbye!", service.farewell(" ", true));
    }

    @Test
    void farewellFormal() {
        assertEquals("Farewell, Ana.", service.farewell("Ana", true));
    }

    @Test
    void farewellCasual() {
        assertEquals("Bye, Ana!", service.farewell("Ana", false));
    }
}
