package com.phonecompany.billing.parser;

import com.phonecompany.billing.model.CallDetails;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PhoneParserTest {

    @Test
    public void testWhenPhoneCSVGivenThenSucesfullyParse(){
        //BEFORE

        //WHEN
        PhoneParser parser = ParserFactorry.initPhoneParser("src/main/resources/callListHistory.csv");

        //THEN
        assertEquals(20, parser.getRecords().size());
    }

}