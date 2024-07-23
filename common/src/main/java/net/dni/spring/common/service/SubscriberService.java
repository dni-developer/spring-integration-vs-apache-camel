package net.dni.spring.common.service;

import net.dni.spring.common.api.EnrollSubscriberRequest;
import net.dni.spring.common.api.EnrollSubscriberResponse;
import net.dni.spring.common.api.SearchSubscriberRequest;
import net.dni.spring.common.api.SearchSubscriberResponse;
import net.dni.spring.common.dao.SubscriberDao;
import net.dni.spring.common.entity.SubscriberEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class SubscriberService {

    private final SubscriberDao subscriberDao;

    public SubscriberService(SubscriberDao subscriberDao) {
        this.subscriberDao = subscriberDao;
    }

    public SubscriberEntity save(SubscriberEntity subscriberEntity) {
        return subscriberDao.save(subscriberEntity);
    }

    public Optional<SubscriberEntity> findById(Long id) {
        return subscriberDao.findById(id);
    }

    public Optional<SubscriberEntity> findBySearchSubscriberRequest(SearchSubscriberRequest request) {
        if (Objects.nonNull(request.getId())) {
            return subscriberDao.findById(request.getId());
        } else if (StringUtils.isNotBlank(request.getEmail())) {
            return subscriberDao.findByEmail(request.getEmail());
        } else if (StringUtils.isNotBlank(request.getFirstName()) && StringUtils.isNotBlank(request.getLastName())) {
            return subscriberDao.findByFirstNameAndLastName(request.getFirstName(), request.getLastName());
        } else {
            return Optional.empty();
        }
    }

    public SearchSubscriberResponse searchSubscriber(SearchSubscriberRequest request) {
        Optional<SubscriberEntity> optional = findBySearchSubscriberRequest(request);
        SearchSubscriberResponse response = new SearchSubscriberResponse();
        optional.ifPresent(response::setSubscriberEntity);
        return response;
    }

    public EnrollSubscriberResponse enrollSubscriber(EnrollSubscriberRequest request) {
        SubscriberEntity subscriberEntity = new SubscriberEntity();
        subscriberEntity.setFirstName(request.getFirstName());
        subscriberEntity.setLastName(request.getLastName());
        subscriberEntity.setEmail(request.getEmail());
        subscriberEntity = save(subscriberEntity);
        EnrollSubscriberResponse response = new EnrollSubscriberResponse();
        response.setSubscriberId(subscriberEntity.getId());
        return response;
    }

}
