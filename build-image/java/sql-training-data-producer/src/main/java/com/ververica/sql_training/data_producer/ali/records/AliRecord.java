package com.ververica.sql_training.data_producer.ali.records;

import java.util.Date;

/**
 * @author Snowden
 * @date 2020/6/10 14:14
 */
public interface AliRecord {
    Date getEventTime();
}
