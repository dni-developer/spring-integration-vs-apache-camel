package net.dni.spring.camel.util;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class ProcessorUtil {

    private ProcessorUtil() {
        //hide util constructor
    }

    public static void convertBodyToMalformedRequestBodyJsonResponse(Exchange exchange) {
        Throwable cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);
        Message msg = exchange.getMessage();
        msg.setHeader(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON);
        msg.setHeader(Exchange.HTTP_RESPONSE_CODE, HttpStatus.BAD_REQUEST.value());
        msg.setBody("{\"errors\":[\"Malformed request body.\"]}");
    }

}
