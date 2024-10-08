package net.dni.spring.camel.router;

import net.dni.spring.camel.exception.ValidationException;
import net.dni.spring.camel.exception.ValidationExceptionResponseHandler;
import net.dni.spring.camel.validator.EnrollSubscriberRequestValidator;
import net.dni.spring.common.ValidateRule;
import net.dni.spring.common.api.EnrollSubscriberRequest;
import net.dni.spring.common.api.EnrollSubscriberResponse;
import net.dni.spring.common.entity.SubscriberEntity;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.platform.http.springboot.PlatformHttpMessage;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;


@Component
public class SubscriberCamelRouter extends RouteBuilder {

    @Value("${subscriber.file.input.directory}")
    public String subscriberFileInputDirectory;

    @Value("${subscriber.file.output.directory}")
    public String subscriberFileOutputDirectory;

    @Value("${subscriber.file.archive.directory}")
    public String subscriberFileArchiveDirectory;

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
        restConfiguration().component("platform-http");

        rest("/subscriber")
            .id("restSubscriber")
            .clientRequestValidation(true)
            .consumes(MediaType.APPLICATION_JSON_VALUE)
            .produces(MediaType.APPLICATION_JSON_VALUE)
            .post()
            .bindingMode(RestBindingMode.json)
            .type(EnrollSubscriberRequest.class)
            .outType(EnrollSubscriberResponse.class)
            .to("direct:processEnrollSubscriberRequest");

        rest("/upload")
            .id("restUpload")
            .clientRequestValidation(true)
            .consumes(MediaType.MULTIPART_FORM_DATA_VALUE)
            .produces(MediaType.APPLICATION_JSON_VALUE)
            .bindingMode(RestBindingMode.off)
            .post()
            .to("direct:storeUpload");

        from("direct:storeUpload")
            .id("storeUpload")
            .process(exchange -> {
                    StandardMultipartHttpServletRequest request = (StandardMultipartHttpServletRequest) exchange.getIn(PlatformHttpMessage.class).getRequest();
                    MultiValueMap<String, MultipartFile> multipartFileMultiValueMap = request.getMultiFileMap();
                    MultipartFile multipartFile = multipartFileMultiValueMap.getFirst("file");
                    Resource resource = multipartFile.getResource();
                    exchange.getMessage().setBody(resource);
                })
            .log("${id} ${body}")
            .to("file:" + subscriberFileInputDirectory);

        from("file:" + subscriberFileInputDirectory + "?move=" + subscriberFileArchiveDirectory)
            .id("processEnrollSubscriberRequestCsv")
            .log("${id} pick: ${file:name}, size: ${file:size}")
            .doTry()
                .unmarshal(new BindyCsvDataFormat(EnrollSubscriberRequest.class))
            .doCatch(IllegalArgumentException.class)
                .log("${exception}")
                .stop()
            .end()
            .split().body()
                .streaming()
                .to("direct:processEnrollSubscriberRequest")
                .marshal(new BindyCsvDataFormat(EnrollSubscriberResponse.class))
                .to("file:" + subscriberFileOutputDirectory + "?fileExist=Append")
            .end()
            .log("${id} finish: ${file:name}, size: ${file:size}")
            .end();

        from("direct:processEnrollSubscriberRequest")
            .id("processEnrollSubscriber")
            .log("${id} ${body}")
            .inputTypeWithValidate(EnrollSubscriberRequest.class)
            .multicast()
                .parallelProcessing()
                .bean(ValidateRule.class, "validateRule1")
                .bean(ValidateRule.class, "validateRule2")
                .bean(ValidateRule.class, "validateRule3")
                .bean(ValidateRule.class, "validateRule4")
            .end()
            .convertBodyTo(SubscriberEntity.class)
            .log("${id} ${body}")
            .to("jpa:SubscriberEntity")
            .convertBodyTo(EnrollSubscriberResponse.class)
            .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(HttpStatus.CREATED.value()))
            .log("${id} ${body}")
                .end();
    }


}
