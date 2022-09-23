package com.phonecompany.billing.operator;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class BillCalculatorSettings {

    private double lowTariffPrice;
    private double highTariffPrice;
    private double longCallPrice;
    private final boolean isHighAndLowTariffPriceActive;
    private final boolean isMostCalledNumberNotChargeActive;
    private final boolean isLongerThenDurationCallPriceActive;
    private double durationOfLongCallInMin;

    private final double defautPrice;

    String DATE_TIME_FORMAT_PATTERN = "HH:mm:ss";
    private LocalTime highTariffStartTime;
    private LocalTime highTariffEndTime;


    public BillCalculatorSettings(long lowTariffPrice, long highTariffPrice, long longCallPrice,
                                  boolean isHighAndLowTariffPriceActive, boolean isMostCalledNumberNotChargeActive,
                                  boolean isLongerThenDurationCallPriceActive,
                                  long durationOfLongCallInMin, double defautPrice) {
        this.lowTariffPrice = lowTariffPrice;
        this.highTariffPrice = highTariffPrice;
        this.longCallPrice = longCallPrice;
        this.isHighAndLowTariffPriceActive = isHighAndLowTariffPriceActive;
        this.isMostCalledNumberNotChargeActive = isMostCalledNumberNotChargeActive;
        this.isLongerThenDurationCallPriceActive = isLongerThenDurationCallPriceActive;
        this.durationOfLongCallInMin = durationOfLongCallInMin;
        this.defautPrice = defautPrice;
    }

    public  BillCalculatorSettings(double defautPrice,boolean isMostCalledNumberNotChargeActive){
        this.defautPrice = defautPrice;
        this.isMostCalledNumberNotChargeActive = isMostCalledNumberNotChargeActive;
        this.isHighAndLowTariffPriceActive = false;
        this.isLongerThenDurationCallPriceActive = false;
    }


    public BillCalculatorSettings(){
        this.lowTariffPrice = 0.5;
        this.highTariffPrice = 1;
        this.longCallPrice = 0.2;
        this.isHighAndLowTariffPriceActive = true;
        this.isMostCalledNumberNotChargeActive = true;
        this.isLongerThenDurationCallPriceActive = true;
        this.durationOfLongCallInMin = 5;
        this.highTariffStartTime = LocalTime.parse("08:00:00",
                DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN));
        this.highTariffEndTime = LocalTime.parse("16:00:00",
                DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN));
        this.defautPrice = (this.highTariffPrice+this.lowTariffPrice+this.longCallPrice)/3;
    }

    /**
     *
     * @return price for low tariff
     */
    public double getLowTariffPrice() {
        return lowTariffPrice;
    }

    /**
     *
     * @return price for high tariff
     */
    public double getHighTariffPrice() {
        return highTariffPrice;
    }

    /**
     *
     * @return price for long calls
     */
    public double getLongCallPrice() {
        return longCallPrice;
    }

    /**
     *
     * @return boolean if high and low prices are applicable for calculation of phone call price
     */
    public boolean isHighAndLowTariffPriceActive() {
        return isHighAndLowTariffPriceActive;
    }

    /**
     *
     * @return boolean if calls for phone number that are most frequently called is priced
     */
    public boolean isMostCalledNumberNotChargeActive() {
        return isMostCalledNumberNotChargeActive;
    }

    /**
     *
     * @return if it is active strategy for special price of longer then some duration call
     */
    public boolean isLongerThenDurationCallPriceActive() {
        return isLongerThenDurationCallPriceActive;
    }

    /**
     *
     * @return duration of longer call
     */
    public double getDurationOfLongCallInMin() {
        return durationOfLongCallInMin;
    }

    /**
     *
     * @return start LocalTime for High Tariff
     */
    public LocalTime getHighTariffStartTime() {
        return highTariffStartTime;
    }

    /**
     *
     * @return end LocalTime for High Tariff
     */
    public LocalTime getHighTariffEndTime() {
        return highTariffEndTime;
    }

    /**
     *
     * @param highTariffStartTime
     *  setting time for beginning of high tariff
     */
    public void setHighTariffStartTime(LocalTime highTariffStartTime) {
        this.highTariffStartTime = highTariffStartTime;
    }

    /**
     *
     * @param highTariffEndTime
     * setting time for end of high tariff
     */
    public void setHighTariffEndTime(LocalTime highTariffEndTime) {
        this.highTariffEndTime = highTariffEndTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BillCalculatorSettings)) return false;
        BillCalculatorSettings that = (BillCalculatorSettings) o;
        return Double.compare(that.lowTariffPrice, lowTariffPrice) == 0 &&
                Double.compare(that.highTariffPrice, highTariffPrice) == 0 &&
                Double.compare(that.longCallPrice, longCallPrice) == 0 &&
                isHighAndLowTariffPriceActive == that.isHighAndLowTariffPriceActive &&
                isMostCalledNumberNotChargeActive == that.isMostCalledNumberNotChargeActive &&
                isLongerThenDurationCallPriceActive == that.isLongerThenDurationCallPriceActive &&
                Double.compare(that.durationOfLongCallInMin, durationOfLongCallInMin) == 0 &&
                Objects.equals(highTariffStartTime, that.highTariffStartTime) &&
                Objects.equals(highTariffEndTime, that.highTariffEndTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lowTariffPrice, highTariffPrice, longCallPrice, isHighAndLowTariffPriceActive,
                isMostCalledNumberNotChargeActive, isLongerThenDurationCallPriceActive, durationOfLongCallInMin,
                highTariffStartTime, highTariffEndTime);
    }

    @Override
    public String toString() {
        return "BillCalculatorSettings{" +
                "lowTariffPrice=" + lowTariffPrice +
                ", highTariffPrice=" + highTariffPrice +
                ", longCallPrice=" + longCallPrice +
                ", isHighAndLowTariffPriceActive=" + isHighAndLowTariffPriceActive +
                ", isMostCalledNumberNotChargeActive=" + isMostCalledNumberNotChargeActive +
                ", isLongerThenDurationCallPriceActive=" + isLongerThenDurationCallPriceActive +
                ", durationOfLongCallInMin=" + durationOfLongCallInMin +
                '}';
    }


    /**
     *
     * @return default price if no high/low price strategy is applied
     */
    public double getDefaultPrice() {
        return this.defautPrice;
    }
}
