package com.phonecompany.billing.operator;

import java.time.Duration;
import java.time.LocalTime;
public class CallDetailHelper {

    /**
     *
     * @param startTime - when call started
     * @param endTime - when call ended
     * @param highTariffStartTime - when high tariff pricing begins
     * @param highTariffEndTime - when high tariff pricing ends
     * @return check if given call is during high tariff period
     */
    public boolean isCallInHightTeriff(LocalTime startTime, LocalTime endTime,
                                       LocalTime highTariffStartTime, LocalTime highTariffEndTime){
        return startTime.compareTo(highTariffStartTime) > 0 && endTime.compareTo(highTariffEndTime) < 0;
    }

    /**
     *
     * @param startTime - when call started
     * @param endTime - when call ended
     * @param highTariffEndTime - when high tariff pricing ends
     * @return check if part of given call is during high tariff period
     */
    public boolean isCalPartlylInHightTeriff(LocalTime startTime, LocalTime endTime,
                                             LocalTime highTariffEndTime) {
        return startTime.compareTo(highTariffEndTime) < 0 || endTime.compareTo(highTariffEndTime) < 0;
    }

    /**
     *
     * @param startTime - when call started
     * @param endTime - when call ended
     * @param durationOfLongCallInMin - value of long call strategy
     * @return check if duration of call fulfil requirements for long call strategy
     */
    public boolean isLongCall(LocalTime startTime, LocalTime endTime , double durationOfLongCallInMin){
        return Double.compare(getTimeInMin(startTime, endTime), durationOfLongCallInMin) > 0;
    }

    /**
     *
     * @param startTime - when call started
     * @param endTime  - when call ended
     * @param highTariffStartTime - when high tariff pricing starts
     * @param highTariffEndTime - when high tariff pricing ends
     * @return duration of the call in minutes that is inside of high period pricing
     * for calls that are partly in high tariff pricing
     */
    public double getDurationInMinForHighTariff(LocalTime startTime, LocalTime endTime,
                                                LocalTime highTariffStartTime,LocalTime highTariffEndTime){
        double result= 0.00;
        if(endTime.compareTo(highTariffStartTime)>0 && startTime.compareTo(highTariffStartTime)<0){
            return getTimeInMin(highTariffStartTime,endTime);
        }
        if(startTime.compareTo(highTariffStartTime)>0 ){
            return getTimeInMin(highTariffEndTime,endTime);
        }
        return result;
    }

    /**
     *
     * @param startTime - when call started
     * @param endTime - when call ended
     * @return duration of the call in minutes
     */
    public double getDurationInMinForCall(LocalTime startTime, LocalTime endTime){
        return getTimeInMin(startTime,endTime);
    }

    /**
     *
     * @param durationOfCallInMin - duration of the call in minutes
     * @param price - price for call
     * @return how much one call is priced
     */
    public double getPriceForCall(double durationOfCallInMin, double price){
        return price * durationOfCallInMin;
    }

    /**
     *
     * @param start - when call started
     * @param end - when call ended
     * @return duration of call in minutes
     */
    private double getTimeInMin(LocalTime start,LocalTime end){
       return (double) (Duration.between(start, end).toMinutes())
               + (double) (Duration.between(start, end).toSeconds() % 60) /100;
    }



}
