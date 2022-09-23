package com.phonecompany.billing.operator;

import com.phonecompany.billing.model.CallDetails;
import com.phonecompany.billing.parser.ParserFactorry;
import com.phonecompany.billing.parser.PhoneParser;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

class OperatorTelephoneBillCalculatorTest {
    private final PhoneParser parser = ParserFactorry.initPhoneParser("src/main/resources/callListHistory.csv");
    private final List<CallDetails> logList = parser.getRecords();
    private final String DATE_TIME_FORMAT_PATTERN = "HH:mm:ss";
    private LocalTime startTime = LocalTime.parse("15:58:00",
                                    DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN));
    private LocalTime endTime = LocalTime.parse("16:08:00",
                                    DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN));

    @Test
    public void testWhenListOfCallLogsGivenAndMFCNumberPolicyIsActiveThenFillTheCountListAndGetMostFrequentlyCalledPhoneNumberAndCountOfCalls(){
        //BEFORE
        OperatorTelephoneBillCalculator underTestCals = new OperatorTelephoneBillCalculator();
        String actualPhoneNumberMostCalled;
        long actualCountOfCalledNumber;
        final  String expectedPhoneNumberMostCalled = "420776562353";
        final long expectedCountOfCalledNumber = 7;

        //WHEN
        underTestCals.initCountOfPhoneNumberCalls(logList);
        actualPhoneNumberMostCalled = underTestCals.getMostFrequentlyCalledNumber();
        actualCountOfCalledNumber = underTestCals.getCountOfMostFrequentlyCalledNumber();

        //THEN
        assertEquals(expectedCountOfCalledNumber,actualCountOfCalledNumber);
        assertEquals(expectedPhoneNumberMostCalled,actualPhoneNumberMostCalled);
    }

    @Test
    public void testWhenMostFrequentlyPhoneNumberBillingPolicyTurnThenRemoveThisPhoneNumberFromBill(){
        //BEFORE
        OperatorTelephoneBillCalculator underTestCals = new OperatorTelephoneBillCalculator();
        underTestCals.initCountOfPhoneNumberCalls(logList);
        String actualPhoneNumberMostCalled = underTestCals.getMostFrequentlyCalledNumber();

        //WHEN
        underTestCals.deletePhoneCallDetailsFromBillingCallLog(actualPhoneNumberMostCalled);

        //THEN
        assertNotEquals(underTestCals.getCallLogHistory().stream()
                .map(CallDetails::getPhoneNumber)
                .findAny() ,
                actualPhoneNumberMostCalled);
    }

    @Test
    public void testWhenCallIsLongThenReturnPriceCallInBothTariffTimeCase(){
        //BEFORE
        OperatorTelephoneBillCalculator underTestCals = new OperatorTelephoneBillCalculator();
        underTestCals.initCountOfPhoneNumberCalls(logList);
        double expectedPrice = ((2*0.5)+(3))+(5*0.2);

        //WHEN
        double actualPrice = underTestCals.getPriceForLongCall(startTime,endTime, true);

        //THEN
        assertEquals(expectedPrice,actualPrice);
    }

    @Test
    public void testWhenCallIsLongThenReturnPriceCallInLowTariffTimeCase(){
        //BEFORE
        startTime = LocalTime.parse("17:00:00", DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN));
        endTime = LocalTime.parse("17:08:00", DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN));
        OperatorTelephoneBillCalculator underTestCals = new OperatorTelephoneBillCalculator();
        underTestCals.initCountOfPhoneNumberCalls(logList);
        double expectedPrice = (5*0.5)+(3*0.2);

        //WHEN
        double actualPrice = underTestCals.getPriceForLongCall(startTime,endTime, true);

        //THEN
        assertEquals(expectedPrice,actualPrice);
    }

    @Test
    public void testWhenCallIsLongThenReturnPriceCallInHighTariffTimeCase(){
        //BEFORE
        startTime = LocalTime.parse("13:00:00", DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN));
        endTime = LocalTime.parse("13:18:00", DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN));
        OperatorTelephoneBillCalculator underTestCals = new OperatorTelephoneBillCalculator();
        underTestCals.initCountOfPhoneNumberCalls(logList);
        double expectedPrice = (5)+(13*0.2);

        //WHEN
        double actualPrice = underTestCals.getPriceForLongCall(startTime,endTime,true);

        //THEN
        assertEquals(expectedPrice,actualPrice);
    }

    @Test
    public void testWhenCallIsBothTariffTimeThenReturnPriceOfCall(){
        //BEFORE
        OperatorTelephoneBillCalculator underTestCals = new OperatorTelephoneBillCalculator();
        underTestCals.initCountOfPhoneNumberCalls(logList);
        double expectedPrice = (2*0.5)+(8);

        //WHEN
        double actualPrice = underTestCals.getPriceOfCall(startTime,endTime,true);

        //THEN
        assertEquals(expectedPrice,actualPrice);
    }

    @Test
    public void testWhenCallIsBothTariffTimeThenReturnPriceOfCallCaseEndOfCallInHigTariff(){
        //BEFORE
        startTime = LocalTime.parse("07:58:00", DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN));
        endTime = LocalTime.parse("08:08:00", DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN));
        OperatorTelephoneBillCalculator underTestCals = new OperatorTelephoneBillCalculator();
        underTestCals.initCountOfPhoneNumberCalls(logList);
        double expectedPrice = (2*0.5)+(8);

        //WHEN
        double actualPrice = underTestCals.getPriceOfCall(startTime,endTime,true);

        //THEN
        assertEquals(expectedPrice,actualPrice);
    }

    @Test
    public void testWhenCallIsLowTariffTimeThenReturnPriceOfCall(){
        //BEFORE
        startTime = LocalTime.parse("17:00:00", DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN));
        endTime = LocalTime.parse("17:08:00", DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN));
        OperatorTelephoneBillCalculator underTestCals = new OperatorTelephoneBillCalculator();
        underTestCals.initCountOfPhoneNumberCalls(logList);
        double expectedPrice = 8*0.5;

        //WHEN
        double actualPrice = underTestCals.getPriceOfCall(startTime,endTime,true);

        //THEN
        assertEquals(expectedPrice,actualPrice);
    }

    @Test
    public void testWhenCallIsHighTariffTimeThenReturnPriceOfCall(){
        //BEFORE
        startTime = LocalTime.parse("13:00:00", DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN));
        endTime = LocalTime.parse("13:18:00", DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN));
        OperatorTelephoneBillCalculator underTestCals = new OperatorTelephoneBillCalculator();
        double expectedPrice = 18;

        //WHEN
        double actualPrice = underTestCals.getPriceOfCall(startTime,endTime,true);

        //THEN
        assertEquals(expectedPrice,actualPrice);
    }


}