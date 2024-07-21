package net.dni.spring.camel.exception;

import lombok.Getter;
import net.dni.spring.common.api.BadRequestDetail;
import org.apache.camel.Exchange;

import java.util.ArrayList;
import java.util.Collection;

@Getter
public class ValidationException extends org.apache.camel.ValidationException {

    private Collection<BadRequestDetail> errors = new ArrayList<>();

    public ValidationException(Exchange exchange, String message) {
        super(exchange, message);
    }

    public ValidationException(Exchange exchange, String message, Collection<BadRequestDetail> errors) {
        this(exchange, message);
        this.errors = errors;
    }

    public ValidationException(String message, Exchange exchange, Throwable cause) {
        super(message, exchange, cause);
    }

    public ValidationException(String message, Collection<BadRequestDetail> errors, Exchange exchange, Throwable cause) {
        this(message, exchange, cause);
        this.errors = errors;
    }
}
