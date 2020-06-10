package com.ververica.sql_training.data_producer.ali.records;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @author Snowden
 * @date 2020/6/10 14:15
 * @description TODO
 */
public class RawSample implements AliRecord{
    @JsonFormat
    private int user;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Date eventTime;
    @JsonFormat
    private int adgroupId;
    @JsonFormat
    private String pid;
    @JsonFormat
    private int nonclk;
    @JsonFormat
    private int clkl;

    public RawSample() {}

    @Override
    public String toString() {
        return "RawSample{" +
                "user=" + user +
                ", eventTime=" + eventTime +
                ", adgroupId=" + adgroupId +
                ", pid='" + pid + '\'' +
                ", nonclk=" + nonclk +
                ", clkl=" + clkl +
                '}';
    }

    public RawSample(int user, int adgroupId, String pid, Date eventTime, int nonclk, int clkl) {
        this.user = user;
        this.adgroupId = adgroupId;
        this.pid = pid;
        this.eventTime = eventTime;
        this.nonclk = nonclk;
        this.clkl = clkl;
    }





    @Override
    public Date getEventTime() {
        return null;
    }
}
