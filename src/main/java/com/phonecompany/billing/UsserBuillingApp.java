package com.phonecompany.billing;

import com.phonecompany.billing.operator.OperatorTelephoneBillCalculator;

public class UsserBuillingApp {

    public static void main(String[] args) {
        OperatorTelephoneBillCalculator myOperator = new OperatorTelephoneBillCalculator();
        myOperator.calculate("src/main/resources/callListHistory.csv");
    }

}
