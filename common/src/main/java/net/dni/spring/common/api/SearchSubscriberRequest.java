package net.dni.spring.common.api;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SearchSubscriberRequest {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;

}
