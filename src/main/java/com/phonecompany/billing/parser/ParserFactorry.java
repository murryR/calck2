package com.phonecompany.billing.parser;

public class ParserFactorry {

    public ParserFactorry(){}

    /**
     *
     * @param phoneLog - path where call history can be found
     * @return
     */
    public static  PhoneParser initPhoneParser(String phoneLog){
        return  new PhoneParser(phoneLog);
    }
}
