package com.phonecompany.billing.operator;

import org.junit.jupiter.api.Test;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import static org.junit.jupiter.api.Assertions.*;

class CallDetailHelperTest {

    String DATE_TIME_FORMAT_PATTERN = "HH:mm:ss";
    LocalTime startTime = LocalTime.parse("15:59:01", DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN));
    LocalTime endTime = LocalTime.parse("16:06:12", DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN));
    BillCalculatorSettings settings = new BillCalculatorSettings();

    @Test
    void testCallInHightTeriffTrueThenReturnPriceOfCall() {
        //BEFORE
        CallDetailHelper helper = new CallDetailHelper();
        startTime = LocalTime.parse("15:00:01", DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN));
        endTime = LocalTime.parse("15:03:00", DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN));

        //WHEN
        boolean actual = helper.isCallInHightTeriff(startTime,endTime,
                    settings.getHighTariffStartTime(),settings.getHighTariffEndTime());
        double actualDurationOfCallInMin = helper.getDurationInMinForCall(startTime,endTime);
        double actualPriceOfCall = helper.getPriceForCall(actualDurationOfCallInMin,settings.getHighTariffPrice());

        //THEN
        assertTrue(actual);
        assertEquals(2.59, actualDurationOfCallInMin);
        assertEquals(2.59, actualPriceOfCall);
    }

    @Test
    void isCallInHightTeriffFalse() {
        //BEFORE
        CallDetailHelper helper = new CallDetailHelper();

        //WHEN
        boolean actual = helper.isCallInHightTeriff(startTime,endTime,
                    settings.getHighTariffStartTime(),settings.getHighTariffEndTime());

        //THEN
        assertFalse(actual);
    }

    @Test
    void testIfCallPartlyInHightTeriffTrueThenCalculate() {
        //BEFORE
        CallDetailHelper helper = new CallDetailHelper();

        //WHEN
        boolean actual = helper.isCalPartlylInHightTeriff(startTime,endTime,settings.getHighTariffEndTime());

        //THEN
        assertTrue(actual);
    }

    @Test
    void testIfCallPartlyInHightTeriffTrueThenReturnHighTariffDuration() {
        //BEFORE
        CallDetailHelper helper = new CallDetailHelper();
        //WHEN
        boolean actual = helper.isCalPartlylInHightTeriff(startTime,endTime,settings.getHighTariffEndTime());
        double highTariffCallDurationActual = helper.getDurationInMinForHighTariff(startTime,endTime,
                    settings.getHighTariffStartTime(),settings.getHighTariffEndTime());
        //THEN
        assertTrue(actual);
        assertEquals(6.12,highTariffCallDurationActual);
    }

    @Test
    void testLongDurationCallTrue() {
        //BEFORE
        CallDetailHelper helper = new CallDetailHelper();

        //WHEN
        boolean actual = helper.isLongCall(startTime,endTime,settings.getDurationOfLongCallInMin());

        //THEN
        assertTrue(actual);
    }

    @Test
    void testWhenLongDurationCallTrueThenGetPriceForLongerPartOfCall() {
        //BEFORE
        CallDetailHelper helper = new CallDetailHelper();

        //WHEN
        boolean actual = helper.isLongCall(startTime,endTime,settings.getDurationOfLongCallInMin());

        //THEN
        assertTrue(actual);
    }

    @Test
    void testLongDurationCallFalse() {
        //BEFORE
        CallDetailHelper helper = new CallDetailHelper();
        startTime = LocalTime.parse("15:00:01", DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN));
        endTime = LocalTime.parse("15:05:00", DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN));

        //WHEN
        boolean actual = helper.isLongCall(startTime,endTime,settings.getDurationOfLongCallInMin());

        //THEN
        assertFalse(actual);
    }

}