package com.quickbill.helpers;
import java.util.regex.Pattern;

public class PostCodeValidator {
    // postal code regex pattern
    private static final String POSTAL_CODE_PATTERN = "^[0-9]{2}-[0-9]{3}$";
    private static final Pattern pattern = Pattern.compile(POSTAL_CODE_PATTERN);

    //checking is postcode valid
    public static boolean isValid(String postalCode) {
        if(postalCode==null)return false;
        return pattern.matcher(postalCode).matches();
    }
}
