package net.dni.spring.camel.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dni.spring.common.api.BadRequestResponse;
import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.apache.camel.Message;
import org.springframework.http.MediaType;

public class ValidationExceptionResponseHandler {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Handler
    public void handleErrorResponse(Exchange exchange) throws JsonProcessingException {
        ValidationException exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, ValidationException.class);

        Message msg = exchange.getMessage();
        msg.setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON);
        msg.setHeader(Exchange.HTTP_RESPONSE_CODE, 400);

        BadRequestResponse response = new BadRequestResponse(exception.getErrors());
        msg.setBody(objectMapper.writeValueAsString(response));
    }

}
