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
import web.app.demorestapi.common.TestDestription;

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
    @TestDestription("정상적으로 이벤트를 생성하는 테스트")
    public void createEvent() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("spring")
                .description("Rest API")
                .beginEnrollmentDateTime(LocalDateTime.of(2018,11,23,11,40))
                .closeEnrollmentDateTime(LocalDateTime.of(2018,11,24,11,30))
                .beginEventDateTime(LocalDateTime.of(2018,11,25,11,20))
                .endEventDateTime(LocalDateTime.of(2018,11,26,11,10))
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
    @TestDestription("입력 받아서는 안되는 값을 받았을 때 발생하는 테스트")
    public void createEvent_Bad_Request() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("spring")
                .description("Rest API")
                .beginEnrollmentDateTime(LocalDateTime.of(2018,11,23,11,40))
                .closeEnrollmentDateTime(LocalDateTime.of(2018,11,24,11,30))
                .beginEventDateTime(LocalDateTime.of(2018,11,25,11,20))
                .endEventDateTime(LocalDateTime.of(2018,11,26,11,10))
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

    @Test
    @TestDestription("입력 값이 비어있는 테스트")
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDestription("입력 값이 잘못된 경우 테스트")
    public void createEvent_Bad_Request_Wrong_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("spring")
                .description("Rest API")
                .beginEnrollmentDateTime(LocalDateTime.of(2018,11,26,11,10))
                .closeEnrollmentDateTime(LocalDateTime.of(2018,11,24,11,30))
                .beginEventDateTime(LocalDateTime.of(2018,11,28,11,10))
                .endEventDateTime(LocalDateTime.of(2018,11,26,11,30))
                .basePrice(500)
                .maxPrice(100)
                .limitOfEnrollment(10)
                .location("압구정 4번출구로 다 모여라")
                .build();

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
    }

}
