package net.dni.spring.camel.converter;

import net.dni.spring.common.api.EnrollSubscriberRequest;
import net.dni.spring.common.entity.Subscriber;
import org.apache.camel.Converter;
import org.apache.camel.TypeConverters;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@Converter
public class SubscriberConverter implements TypeConverters {

    @Converter
    public Subscriber toSubscriber(EnrollSubscriberRequest request) {
        Subscriber subscriber = new Subscriber();
        subscriber.setEmail(StringUtils.lowerCase(request.getEmail()));
        subscriber.setFirstName(StringUtils.upperCase(request.getFirstName()));
        subscriber.setLastName(StringUtils.upperCase(request.getLastName()));
        return subscriber;
    }

}
