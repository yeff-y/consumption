package com.yeff.consumption.service;

import com.yeff.consumption.dto.ConsumerDto;

import java.util.List;

public interface ConsumerService {

    void insertRecord(ConsumerDto consumerDto);

    List<ConsumerDto> getRecordByName(String name);

    List<ConsumerDto> getRecordByTime(String date);

}
