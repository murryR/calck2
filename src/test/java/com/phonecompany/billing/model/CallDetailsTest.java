package com.phonecompany.billing.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CallDetailsTest {

    @Test
    public void testCalculateTimeDuration(){
        //BEFORE
        CallDetails call = new CallDetails("78787878","18-01-2020 08:59:20",
                                    "18-01-2020 09:10:00");
        long expectedDuration = 640;
        //WHEN

        long callDuration = call.getDurationInSeconds();
        //THEN
        assertEquals(expectedDuration,callDuration);
    }

    @Test
    public void testCompareTwoEqualCallDetails(){
        //BEFORE
        CallDetails actualCall = new CallDetails("78787878","18-01-2020 08:59:20",
                "18-01-2020 09:10:00");
        CallDetails expectedCall = new CallDetails("78787878","18-01-2020 08:59:20",
                "18-01-2020 09:10:00");
        //WHEN

        //THEN
        assertEquals(expectedCall,actualCall);
    }

}