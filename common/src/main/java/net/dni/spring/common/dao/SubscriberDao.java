package net.dni.spring.common.dao;

import net.dni.spring.common.entity.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriberDao extends JpaRepository<Subscriber, Long> {

    Optional<Subscriber> findByEmail(String email);

    Optional<Subscriber> findByFirstNameAndLastName(String firstName, String lastName);

}
