package net.dni.spring.camel.validator;

import lombok.extern.log4j.Log4j2;
import net.dni.spring.camel.exception.ValidationException;
import net.dni.spring.common.api.BadRequestDetail;
import net.dni.spring.common.api.EnrollSubscriberRequest;
import org.apache.camel.Message;
import org.apache.camel.spi.DataType;
import org.apache.camel.spi.Validator;

import java.util.ArrayList;
import java.util.Collection;

import static net.dni.spring.common.ValidateRule.*;

@Log4j2
public class EnrollSubscriberRequestValidator extends Validator {


    @Override
    public void validate(Message message, DataType type) throws ValidationException {
        log.debug("validate message:[{}], type:[{}]", message.getExchange().getAllProperties(), type);

        Collection<BadRequestDetail> errors = new ArrayList<>();

        EnrollSubscriberRequest request = message.getBody(EnrollSubscriberRequest.class);
        if (!isValidEmail(request.getEmail())) {
            errors.add(new BadRequestDetail("email", "Invalid email."));
        }

        if (!isValidFirstName(request.getFirstName())) {
            errors.add(new BadRequestDetail("firstName", "Invalid firstName."));
        }

        if (!isValidLastName(request.getLastName())) {
            errors.add(new BadRequestDetail("lastName", "Invalid lastName."));
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(null, null, errors);
        }

    }

}
