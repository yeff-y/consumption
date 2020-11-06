package com.yeff.consumption.service;

import com.yeff.consumption.dto.ConsumerDTO;

import java.util.List;

public interface ConsumerService {

    void insertRecord(ConsumerDTO consumerDto);

    List<ConsumerDTO> getRecordByName(String name);

    List<ConsumerDTO> getRecordByTime(String date);

    List<ConsumerDTO> getRecordByPeriod(String sDate, String eDate);

}
