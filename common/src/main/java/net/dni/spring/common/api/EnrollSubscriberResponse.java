package net.dni.spring.common.api;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.dni.spring.common.entity.Subscriber;

@NoArgsConstructor
@Data
public class EnrollSubscriberResponse {

    private Subscriber subscriber;

}
