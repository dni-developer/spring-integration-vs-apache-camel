package net.dni.spring.camel.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dni.spring.common.api.EnrollSubscriberRequest;
import net.dni.spring.common.api.EnrollSubscriberResponse;
import net.dni.spring.common.entity.RequestEntity;
import net.dni.spring.common.entity.SubscriberEntity;
import org.apache.camel.Converter;
import org.apache.camel.TypeConverters;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@Converter
public class SubscriberConverter implements TypeConverters {

    @Converter
    public SubscriberEntity toSubscriber(EnrollSubscriberRequest enrollSubscriberRequest) throws JsonProcessingException {
        RequestEntity requestEntity = new RequestEntity();
        requestEntity.setContent(new ObjectMapper().writeValueAsString(enrollSubscriberRequest));

        SubscriberEntity subscriberEntity = new SubscriberEntity();
        subscriberEntity.setRequestEntity(requestEntity);
        subscriberEntity.setEmail(StringUtils.lowerCase(enrollSubscriberRequest.getEmail()));
        subscriberEntity.setFirstName(StringUtils.upperCase(enrollSubscriberRequest.getFirstName()));
        subscriberEntity.setLastName(StringUtils.upperCase(enrollSubscriberRequest.getLastName()));

        return subscriberEntity;
    }

    @Converter
    public EnrollSubscriberResponse toEnrollSubscriberResponse(SubscriberEntity subscriberEntity) {
        return new EnrollSubscriberResponse(subscriberEntity.getId());
    }

}
