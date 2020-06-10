package com.ververica.sql_training.data_producer.ali;

import com.ververica.sql_training.data_producer.*;
import com.ververica.sql_training.data_producer.ali.records.AliFileReader;
import com.ververica.sql_training.data_producer.ali.records.AliRecord;
import com.ververica.sql_training.data_producer.ali.records.BehaviorLog;
import com.ververica.sql_training.data_producer.ali.records.RawSample;
import com.ververica.sql_training.data_producer.records.DriverChange;
import com.ververica.sql_training.data_producer.records.Fare;
import com.ververica.sql_training.data_producer.records.Ride;
import com.ververica.sql_training.data_producer.records.TaxiRecord;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author Snowden
 * @date 2020/6/10 14:36
 * @description TODO
 */
public class AliRecordProducer {
    public static void main(String[] args) throws InterruptedException {

        boolean areSuppliersConfigured = false;
        boolean areConsumersConfigured = false;

        Supplier<AliRecord> rawSample = null;
        Supplier<AliRecord> behaviorLog = null;

        Consumer<AliRecord> rawSampleConsumer = null;
        Consumer<AliRecord> behaviorLogConsumer = null;

        double speedup = 1.0d;

        // parse arguments
        int argOffset = 0;
        while(argOffset < args.length) {

            String arg = args[argOffset++];
            switch (arg) {
                case "--input":
                    String source = args[argOffset++];
                    switch (source) {
                        case "file":
                            String basePath = args[argOffset++];
                            try {
                                rawSample = new AliFileReader(basePath + "/raw_sample.csv", RawSample.class);
                                behaviorLog = new AliFileReader(basePath + "/behavior_log.csv", BehaviorLog.class);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        default:
                            throw new IllegalArgumentException("Unknown input configuration");
                    }
                    areSuppliersConfigured = true;
                    break;
                case "--output":
                    String sink = args[argOffset++];
                    switch (sink) {
                        case "console":
                            rawSampleConsumer = new AliConsolePrinter();
                            behaviorLogConsumer = new AliConsolePrinter();
                            break;
                        case "kafka":
                            String brokers = args[argOffset++];
                            rawSampleConsumer = new AliKafkaProducer("rawSample", brokers);
                            behaviorLogConsumer = new AliKafkaProducer("behaviorLog", brokers);
                            break;
                        default:
                            throw new IllegalArgumentException("Unknown output configuration");
                    }
                    areConsumersConfigured = true;
                    break;
                case "--speedup":
                    speedup = Double.parseDouble(args[argOffset++]);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown parameter");
            }
        }

        // check if we have a source and a sink
        if (!areSuppliersConfigured) {
            throw new IllegalArgumentException("Input sources were not properly configured.");
        }
        if (!areConsumersConfigured) {
            throw new IllegalArgumentException("Output sinks were not properly configured");
        }

        // create three threads for each record type
        Thread ridesFeeder = new Thread(new AliRecordProducer.AliRecordFeeder(rawSample, new AliDelayer(speedup), rawSampleConsumer));
        Thread faresFeeder = new Thread(new AliRecordProducer.AliRecordFeeder(behaviorLog, new AliDelayer(speedup), behaviorLogConsumer));

        // start emitting data
        ridesFeeder.start();
        faresFeeder.start();

        // wait for threads to complete
        ridesFeeder.join();
        faresFeeder.join();
    }

    public static class AliRecordFeeder implements Runnable {

        private final Supplier<AliRecord> source;
        private final AliDelayer delayer;
        private final Consumer<AliRecord> sink;

        AliRecordFeeder(Supplier<AliRecord> source, AliDelayer delayer, Consumer<AliRecord> sink) {
            this.source = source;
            this.delayer = delayer;
            this.sink = sink;
        }

        @Override
        public void run() {
            Stream.generate(source).sequential()
                    .map(delayer)
                    .forEachOrdered(sink);
        }
    }
}
