package com.ververica.sql_training.data_producer.ali;

import com.ververica.sql_training.data_producer.ali.records.AliRecord;
import com.ververica.sql_training.data_producer.json_serde.JsonSerializer;

import java.util.function.Consumer;

public class AliConsolePrinter implements Consumer<AliRecord> {

    private final JsonSerializer<AliRecord> serializer = new JsonSerializer<>();

    @Override
    public void accept(AliRecord record) {
        String jsonString = serializer.toJSONString(record);
        System.out.println(jsonString);
    }
}