package net.dni.spring.common;

import lombok.extern.slf4j.Slf4j;
import net.dni.spring.common.api.EnrollSubscriberRequest;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

@Slf4j
public class ValidateRule {

    private ValidateRule() {
        //hide util constructor
    }

    public static boolean isValidFirstName(String input) {
        return StringUtils.isNotBlank(input);
    }

    public static boolean isValidLastName(String input) {
        return StringUtils.length(input) > 1;
    }

    public static boolean isValidEmail(String input) {
        if (StringUtils.isNotBlank(input)) {
            Pattern pattern = Pattern.compile("^(.+)@(.+)$");
            return pattern.matcher(input).matches();
        }
        return false;
    }


    public static void validateRule1(EnrollSubscriberRequest request) {
        log.info("validateRule1");
    }

    public static void validateRule2(EnrollSubscriberRequest request) {
        log.info("validateRule2");
    }

    public static void validateRule3(EnrollSubscriberRequest request) {
        log.info("validateRule3");
    }

    public static void validateRule4(EnrollSubscriberRequest request) {
        log.info("validateRule4");
    }

}
