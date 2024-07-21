package net.dni.spring.common.api;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@NoArgsConstructor
@Data
public class BadRequestResponse {

    private Collection<BadRequestDetail> errors = new ArrayList<>();

    public BadRequestResponse(Collection<BadRequestDetail> errors) {
        this.errors = errors;
    }

}
