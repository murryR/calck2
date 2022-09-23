package com.phonecompany.billing.operator;

import com.phonecompany.billing.model.CallDetails;
import com.phonecompany.billing.parser.ParserFactorry;
import com.phonecompany.billing.parser.PhoneParser;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.*;
import java.util.logging.Logger;

public class OperatorTelephoneBillCalculator implements TelephoneBillCalculator {

    private CallDetailHelper helper;
    private final BillCalculatorSettings settings;
    private final HashMap<String, Long> countOfCalledNumberList;
    List<CallDetails> logList;

    private final Logger logger;


    public OperatorTelephoneBillCalculator() {
        helper = new CallDetailHelper();
        settings = new BillCalculatorSettings();
        helper = new CallDetailHelper();
        countOfCalledNumberList = new HashMap<>();
        logList = new ArrayList<>();
        logger = Logger.getLogger(OperatorTelephoneBillCalculator.class.getName());
    }


    /**
     * @param phoneLog - a place where call history can be found
     * @return - final price for calls based on phone call history and phone company strategies applied
     */
    @Override
    public BigDecimal calculate(String phoneLog) {
        logger.info("Calculate is called");
        double billToPay = 0;
        logger.info("Start parsing of file path: " + phoneLog);
        PhoneParser parser = ParserFactorry.initPhoneParser(phoneLog);
        logger.info("Parsing end with success");
        logList = parser.getRecords();
        LocalTime startTime;
        LocalTime endTime;
        logger.info("Checking if most frequently called number is billed");

        if (settings.isMostCalledNumberNotChargeActive()) {
            logger.info("Most frequently called number is not billed");
            initCountOfPhoneNumberCalls(logList);
            logger.info("Most frequently called number is: " + getMostFrequentlyCalledNumber() +
                    " is called " + getCountOfMostFrequentlyCalledNumber() + " times");
            deletePhoneCallDetailsFromBillingCallLog(getMostFrequentlyCalledNumber());
            logger.info("Most frequently called number is removed successfully from billing list");
        } else {
            logger.info("Most frequently called number is billed");
        }

        logger.info("Longer call special price is on: " + settings.isLongerThenDurationCallPriceActive());
        logger.info("HighTariff/LowTariff prising is on: " + settings.isHighAndLowTariffPriceActive());

        for (int i = 0; i < logList.size(); i++) {
            double priceForSingleCall = 0;
            CallDetails call = logList.get(i);
            startTime = call.getStartTime().toLocalTime();
            endTime = call.getEndTime().toLocalTime();
            logger.info("Calculating price for " + call.getPhoneNumber() + " durationOfCall: " +
                    helper.getDurationInMinForCall(startTime, endTime) + " call started: " + call.getStartTime() +
                    " call ended: " + call.getEndTime());
            if (settings.isLongerThenDurationCallPriceActive()) {
                if (settings.isHighAndLowTariffPriceActive()) {
                    priceForSingleCall = getPriceForLongCall(startTime, endTime, true);
                    billToPay += priceForSingleCall;
                } else {
                    priceForSingleCall = getPriceForLongCall(startTime, endTime, false);
                    billToPay += priceForSingleCall;
                }
            }

            if (settings.isHighAndLowTariffPriceActive()) {
                priceForSingleCall = getPriceOfCall(startTime, endTime, true);
                billToPay += priceForSingleCall;
            } else {
                priceForSingleCall = getPriceOfCall(startTime, endTime, false);
                billToPay += priceForSingleCall;
            }
            logger.info("Price for call is: " + priceForSingleCall);
        }
        logger.info("TotalBill for user is: " + billToPay);
        return new BigDecimal(billToPay);
    }

    /**
     * @param logList - list of calls based on call history
     *
     * initialize and fill map that contains each called number and frequency of called times based on call history
     */
    public void initCountOfPhoneNumberCalls(List<CallDetails> logList) {

        long count = 1L;
        for (int i = 0; i < logList.size(); i++) {
            String number = logList.get(i).getPhoneNumber();
            if (!countOfCalledNumberList.isEmpty()) {
                if (countOfCalledNumberList.containsKey(number)) {
                    count = countOfCalledNumberList.get(number) + 1;
                    countOfCalledNumberList.replace(number, count);
                    count = 1;
                } else {
                    countOfCalledNumberList.put(number, count);
                }
            } else {
                countOfCalledNumberList.put(number, count);
            }
        }
    }

    /**
     * @return phone number that is most frequently called based on call history
     */
    public String getMostFrequentlyCalledNumber() {
        return countOfCalledNumberList.entrySet().stream()
                .max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1)
                .get()
                .getKey();
    }

    /**
     * @return counts of called times for most frequently called number
     */
    public long getCountOfMostFrequentlyCalledNumber() {
        return Collections.max(countOfCalledNumberList.entrySet(), Comparator.comparingLong(Map.Entry::getValue))
                .getValue();
    }

    /**
     * @return map of phone numbers and times that are called based on call history log
     */
    public Map<String, Long> getCountOfCalledNumberList() {
        return countOfCalledNumberList;
    }

    /**
     * @param phoneNumber - phone number that is going to be removed from call log
     *
     *  deleting all the calls to given phone number from list of phone calls log for prising
     */
    public void deletePhoneCallDetailsFromBillingCallLog(String phoneNumber) {

        for (int i = 0; i < logList.size(); i++) {
            if (logList.get(i).getPhoneNumber().equals(phoneNumber)) {
                logList.remove(i);
            }
        }

    }

    /**
     * @return list of calls for prising
     */
    public List<CallDetails> getCallLogHistory() {
        return logList;
    }

    /**
     * @param startOfCall - when call started
     * @param endOfCall - when call ended
     * @param isTariffPrisingOn - controller if High/Low tariff strategy is active for pricing
     * @return calculated price for long call strategy based on activation of High/Low tariff strategy for pricing
     */
    public double getPriceForLongCall(LocalTime startOfCall, LocalTime endOfCall, boolean isTariffPrisingOn) {

        double priceResult = 0.00;
        double callDuration = helper.getDurationInMinForCall(startOfCall, endOfCall);
        double longCallDurationForPricing = callDuration - settings.getDurationOfLongCallInMin();
        LocalTime endOfCallForNormalPricing = endOfCall.minusMinutes(Long.valueOf((long) longCallDurationForPricing));

        priceResult += getPriceOfCall(startOfCall, endOfCallForNormalPricing, isTariffPrisingOn);
        priceResult += longCallDurationForPricing * settings.getLongCallPrice();


        return priceResult;
    }

    /**
     * @param startOfCall - when call started
     * @param endOfCall - when call ended
     * @param isTariffPricingOn - controller if High/Low tariff strategy is active for pricing
     * @return calculated price for call based on activation of High/Low tariff strategy for pricing
     */
    public double getPriceOfCall(LocalTime startOfCall, LocalTime endOfCall, boolean isTariffPricingOn) {
        double priceResult = 0.00;
        double chargedByTarifCallDuration = helper.getDurationInMinForCall(startOfCall, endOfCall);

        if (isTariffPricingOn) {
            if (helper.isCallInHightTeriff(startOfCall, endOfCall,
                    settings.getHighTariffStartTime(), settings.getHighTariffEndTime())) {
                priceResult = priceResult + (chargedByTarifCallDuration * settings.getHighTariffPrice());
            } else if (helper.isCalPartlylInHightTeriff(startOfCall, endOfCall, settings.getHighTariffEndTime())) {
                double durationForHighTariffPricing = helper.getDurationInMinForHighTariff(startOfCall, endOfCall,
                        settings.getHighTariffStartTime(), settings.getHighTariffEndTime());
                priceResult = priceResult + (durationForHighTariffPricing * settings.getHighTariffPrice());
                priceResult = priceResult +
                        ((chargedByTarifCallDuration - durationForHighTariffPricing) * settings.getLowTariffPrice());

            } else {
                priceResult = priceResult + (chargedByTarifCallDuration * settings.getLowTariffPrice());
            }
        } else {
            priceResult = chargedByTarifCallDuration * settings.getDefaultPrice();
        }

        return priceResult;
    }

}
