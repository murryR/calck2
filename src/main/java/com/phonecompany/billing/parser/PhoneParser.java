package com.phonecompany.billing.parser;

import com.phonecompany.billing.model.CallDetails;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PhoneParser {

    private static final String COMMA_DELIMITER = ",";
    private final List<CallDetails> records;

    public PhoneParser(String phoneLog){
        records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(phoneLog))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                CallDetails call = new CallDetails(values[0],values[1],values[2]);
                records.add(call);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @return list of objects for each call in call history
     */
    public List<CallDetails> getRecords() {
        return records;
    }
}


