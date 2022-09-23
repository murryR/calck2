package com.phonecompany.billing.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

public class CallDetails {

    private final String phoneNumber;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;


    public CallDetails(String phoneNumber, String startTime, String endTime)
    {
        //13-01-2020 18:10:15
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        this. phoneNumber = phoneNumber;
        this.startTime = LocalDateTime.parse(startTime, formatter);
        this.endTime = LocalDateTime.parse(endTime, formatter);
    }

    /**
     *
     * @return a phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     *
     * @return start time of call
     */
    public  LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     *
     * @return end time of call
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     *
     * @return call duration in seconds
     */
    public long getDurationInSeconds(){
        Date end = new Date(endTime.getYear(),endTime.getMonth().getValue(),endTime.getDayOfMonth(),
                endTime.getHour(),endTime.getMinute(),endTime.getSecond());
        Date start = new Date(startTime.getYear(),startTime.getMonth().getValue(),startTime.getDayOfMonth(),
                startTime.getHour(),startTime.getMinute(),startTime.getSecond());
        long seconds =(end.getTime()-start.getTime())/1000;
        return seconds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CallDetails that = (CallDetails) o;
        return Objects.equals(phoneNumber, that.phoneNumber) &&
                Objects.equals(startTime,that.getStartTime()) &&
                Objects.equals(endTime, that.getEndTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(phoneNumber,startTime,endTime);
    }

    @Override
    public String toString() {
        return "CallDetails{ callNumber:"+phoneNumber+" started: "+startTime.toString()+" endCall:"+endTime+" }";
    }
}
