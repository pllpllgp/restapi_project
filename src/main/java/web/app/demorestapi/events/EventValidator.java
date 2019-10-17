package web.app.demorestapi.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class EventValidator {

    public void validate(EventDto eventDto, Errors errors) {

        if(eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() > 0) {

            errors.reject("WrongPrices", "Value of Price are Wrong");
        }

        if(eventDto.getEndEventDateTime().isBefore(eventDto.getBeginEventDateTime()) ||
        eventDto.getEndEventDateTime().isBefore(eventDto.getCloseEnrollmentDateTime()) ||
        eventDto.getEndEventDateTime().isBefore(eventDto.getBeginEnrollmentDateTime())) {

            errors.rejectValue("endEventDateTime", "wrongValue", "endEventDateTime is Wrong");
        }
    }
}
