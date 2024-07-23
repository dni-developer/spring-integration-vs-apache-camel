package net.dni.spring.common.dao;

import net.dni.spring.common.entity.SubscriberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriberDao extends JpaRepository<SubscriberEntity, Long> {

    Optional<SubscriberEntity> findByEmail(String email);

    Optional<SubscriberEntity> findByFirstNameAndLastName(String firstName, String lastName);

}
