package com.quickbill.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {
    //email pattern
    private static final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
    private static final Pattern PATTERN = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);

    //cheing is email valid
    public static boolean isValid(String email) {
        Matcher matcher = PATTERN.matcher(email);
        return matcher.matches();
    }
}