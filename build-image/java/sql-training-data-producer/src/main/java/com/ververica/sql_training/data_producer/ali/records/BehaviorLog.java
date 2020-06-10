package com.ververica.sql_training.data_producer.ali.records;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @author Snowden
 * @date 2020/6/10 20:18
 */
public class BehaviorLog implements AliRecord{
    @JsonFormat
    private int user;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Date eventTime;
    @JsonFormat
    private String btag;
    @JsonFormat
    private int cate;
    @JsonFormat
    private int brand;
    public BehaviorLog() {}

    public BehaviorLog(int user, String btag, Date eventTime, int cate, int brand) {
        this.user = user;
        this.btag = btag;
        this.cate = cate;
        this.eventTime = eventTime;
        this.brand = brand;
    }

    @Override
    public Date getEventTime() {
        return null;
    }

    @Override
    public String toString() {
        return "BehaviorLog{" +
                "user=" + user +
                ", eventTime=" + eventTime +
                ", btag='" + btag + '\'' +
                ", cate=" + cate +
                ", brand=" + brand +
                '}';
    }
}
