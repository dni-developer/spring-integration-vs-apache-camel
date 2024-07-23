package net.dni.spring.camel.exception;

import net.dni.spring.common.api.BadRequestResponse;
import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.apache.camel.Message;
import org.springframework.http.MediaType;

public class ValidationExceptionResponseHandler {

    @Handler
    public void handleErrorResponse(Exchange exchange) {
        ValidationException exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, ValidationException.class);

        Message msg = exchange.getMessage();
        msg.setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON);
        msg.setHeader(Exchange.HTTP_RESPONSE_CODE, 400);

        BadRequestResponse response = new BadRequestResponse(exception.getErrors());
        msg.setBody(response);
    }

}
