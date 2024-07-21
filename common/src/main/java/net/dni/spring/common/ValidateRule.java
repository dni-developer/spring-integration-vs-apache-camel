package net.dni.spring.common;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

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

}
