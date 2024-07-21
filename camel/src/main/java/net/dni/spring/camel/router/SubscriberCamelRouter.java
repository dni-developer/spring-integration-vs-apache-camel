package net.dni.spring.camel.router;

import com.fasterxml.jackson.core.JsonParseException;
import net.dni.spring.camel.exception.ValidationException;
import net.dni.spring.camel.exception.ValidationExceptionResponseHandler;
import net.dni.spring.camel.util.ProcessorUtil;
import net.dni.spring.camel.validator.EnrollSubscriberRequestValidator;
import net.dni.spring.common.api.EnrollSubscriberRequest;
import net.dni.spring.common.api.EnrollSubscriberResponse;
import net.dni.spring.common.entity.Subscriber;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;


@Component
public class SubscriberCamelRouter extends RouteBuilder {

    @Override
    public void configure() {
        registerErrorHandling();
        registerValidator();
        registerRoute();
    }

    public void registerErrorHandling() {
        onException(ValidationException.class)
                .handled(true)
                .bean(ValidationExceptionResponseHandler.class)
                .to("log:badRequest");
    }

    public void registerValidator() {
        validator().type(EnrollSubscriberRequest.class).withJava(EnrollSubscriberRequestValidator.class);
    }


    public void registerRoute() {
        rest("/subscriber")
            .clientRequestValidation(true)
            .consumes(MediaType.APPLICATION_JSON_VALUE)
            .produces(MediaType.APPLICATION_JSON_VALUE)
            .post()
            .type(EnrollSubscriberRequest.class)
            .outType(EnrollSubscriberResponse.class)
            .to("direct:processEnrollSubscriberJson");


        from("direct:processEnrollSubscriberJson")
            .id("processEnrollSubscriberJson")
            .log("${body}")
            .doTry()
                .unmarshal()
                .json(JsonLibrary.Jackson, EnrollSubscriberRequest.class)
                .convertBodyTo(EnrollSubscriberRequest.class)
            .doCatch(JsonParseException.class)
                .process(ProcessorUtil::convertBodyToMalformedRequestBodyResponse)
                .stop()
            .end()
            .to("direct:processEnrollSubscriber");

        from("direct:processEnrollSubscriber")
            .id("processEnrollSubscriber")
            .log("${body}")
            .inputTypeWithValidate(EnrollSubscriberRequest.class)
            .convertBodyTo(Subscriber.class)
            .to("direct:saveSubscriber");


        from("direct:saveSubscriber")
            .id("saveSubscriber")
            .log("${body}")
            .to("jpa:" + Subscriber.class.getName())
            .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(HttpStatus.CREATED.value()))
            .log("${body}")
                .end();
    }


}
