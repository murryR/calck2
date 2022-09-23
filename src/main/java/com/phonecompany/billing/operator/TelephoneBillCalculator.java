package com.phonecompany.billing.operator;

import java.math.BigDecimal;

public interface TelephoneBillCalculator {
    BigDecimal calculate (String phoneLog);

}
