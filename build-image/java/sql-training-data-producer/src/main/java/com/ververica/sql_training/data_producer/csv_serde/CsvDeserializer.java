package com.ververica.sql_training.data_producer.csv_serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ververica.sql_training.data_producer.ali.records.BehaviorLog;
import com.ververica.sql_training.data_producer.ali.records.RawSample;

import java.io.IOException;
import java.util.Date;

/**
 * @author Snowden
 * @date 2020/6/10 19:52
 * @description TODO
 */
public class CsvDeserializer  {


    public static RawSample parseFromString(String line) {
        String[] split = line.split(",");
        if(split[0].contains("user")){
            return new RawSample(0,0,"",new Date(),0,0);
        }
        int user = Integer.valueOf(split[0]);
        int adgroupId  = Integer.valueOf(split[2]);
        String pid  = String.valueOf(split[3]);
        Date eventTime  = new Date(Long.valueOf(split[1])*1000);
        int nonclk  = Integer.valueOf(split[4]);
        int clkl  = Integer.valueOf(split[5]);
        return new RawSample(user,adgroupId,pid,eventTime,nonclk,clkl);
    }
    public static BehaviorLog parseFromStringToBehaviorLog(String line) {
        String[] split = line.split(",");
        if(split[0].contains("user")){
            return new BehaviorLog(0,"",new Date(),0,0);
        }
        int user = Integer.valueOf(split[0]);
        String btag  = String.valueOf(split[2]);
        Date eventTime  = new Date(Long.valueOf(split[1])*1000);
        int cate  = Integer.valueOf(split[3]);
        int brand  = Integer.valueOf(split[4]);
        return new BehaviorLog(user,btag,eventTime,cate,brand);
    }
}
