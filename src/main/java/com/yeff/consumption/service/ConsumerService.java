package com.yeff.consumption.service;

import com.yeff.consumption.dto.ConsumerDto;

import java.util.List;

public interface ConsumerService {

    public void insertRecord(ConsumerDto consumerDto);

    public List<ConsumerDto> getRecordByName(String name);
}
