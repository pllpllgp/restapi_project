package web.app.demorestapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void createEvent() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("spring")
                .description("Rest API")
                .beginEnrollmentDateTime(LocalDateTime.of(2018,11,23,11,40))
                .closeEnrollmentDateTime(LocalDateTime.of(2018,11,23,11,30))
                .beginEventDateTime(LocalDateTime.of(2018,11,23,11,20))
                .endEventDateTime(LocalDateTime.of(2018,11,23,11,10))
                .basePrice(500)
                .maxPrice(1000)
                .limitOfEnrollment(10)
                .location("압구정 4번출구로 다 모여라")
                .build();

        mockMvc.perform(post("/api/events/")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsBytes(eventDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("free").value(Matchers.not(true)))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()));
    }

    @Test
    public void errorEvent() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("spring")
                .description("Rest API")
                .beginEnrollmentDateTime(LocalDateTime.of(2018,11,23,11,40))
                .closeEnrollmentDateTime(LocalDateTime.of(2018,11,23,11,30))
                .beginEventDateTime(LocalDateTime.of(2018,11,23,11,20))
                .endEventDateTime(LocalDateTime.of(2018,11,23,11,10))
                .basePrice(500)
                .maxPrice(1000)
                .limitOfEnrollment(10)
                .location("압구정 4번출구로 다 모여라")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsBytes(event)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}
