package web.app.demorestapi.events;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EventsTest {

    @Test
    public  void builder() {

        Event event = Event.builder()
                .name("Cho")
                .description("Rest Api")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean() {

        Event event = new Event();
        String name = "hi";
        String description = "yo";

        event.setName(name);
        event.setDescription(description);

        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);

    }

}