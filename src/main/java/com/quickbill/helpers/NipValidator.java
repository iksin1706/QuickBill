package com.quickbill.helpers;

public class NipValidator {
    //checking is nip valid
    public static boolean isValid(String nip){
        boolean valid=true;
        if(nip==null)valid=false;
        try {
            long l = Long.parseLong(nip);
        }
        catch (Exception e){
            valid=false;
        }

        if(nip.length()!=10)valid=false;
        return valid;
    }
}
