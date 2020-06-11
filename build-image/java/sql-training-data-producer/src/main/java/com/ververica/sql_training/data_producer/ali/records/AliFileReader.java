package com.ververica.sql_training.data_producer.ali.records;

import com.ververica.sql_training.data_producer.csv_serde.CsvDeserializer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author Snowden
 * @date 2020/6/10 14:43
 * @description TODO
 */
public class AliFileReader implements Supplier<AliRecord> {
    private final Iterator<AliRecord> records;
    private final String filePath;
    public AliFileReader(String filePath, Class<? extends AliRecord> recordClazz) throws IOException {
        this.filePath = filePath;
        try {

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8));

            Stream<String> lines = reader.lines().sequential();
            if(recordClazz.equals(RawSample.class)){
                records = lines.map(new Function<String, AliRecord>() {
                    @Override
                    public AliRecord apply(String s) {
                        return CsvDeserializer.parseFromString(s);
                    }
                }).iterator();
            }else {
                records = lines.map(new Function<String, AliRecord>() {
                    @Override
                    public AliRecord apply(String s) {
                        return CsvDeserializer.parseFromStringToBehaviorLog(s);
                    }
                }).iterator();
            }


        } catch (IOException e) {
            throw new IOException("Error reading TaxiRecords from file: " + filePath, e);
        }
    }
    @Override
    public AliRecord get() {
        if (records.hasNext()) {
            return records.next();
        } else {
            throw new NoSuchElementException("All records read from " + filePath);
        }
    }
}
