package com.yeff.consumption.service.impl;


import com.yeff.consumption.dto.ConsumerDto;
import com.yeff.consumption.model.Consumer;
import com.yeff.consumption.provider.ConsumerProvider;
import com.yeff.consumption.service.ConsumerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ConsumerServiceImpl implements ConsumerService {

    private final Logger logger = LoggerFactory.getLogger(ConsumerServiceImpl.class);

    @Autowired
    private ConsumerProvider consumerProvider;

    @Override
    public void insertRecord(ConsumerDto consumerDto) {
        Consumer consumer = _of(consumerDto);
        consumerProvider.insert(consumer);
    }

    @Override
    public List<ConsumerDto> getRecordByName(String name) {
        List<Consumer> consumerList = consumerProvider.selectRecords(name);
        List<ConsumerDto> consumerDtoList = new ArrayList<>();
        consumerDtoList = consumerList.parallelStream().map(consumer -> {
            ConsumerDto consumerDto = _buildConsumerDto(consumer);
            consumerDto.setConsumerName(name);
            return consumerDto;
        }).collect(Collectors.toList());
        return consumerDtoList;
    }

    private ConsumerDto _buildConsumerDto(Consumer consumer) {
        ConsumerDto consumerDto = new ConsumerDto();
        consumerDto.setCategory(consumer.getCategory());
        consumerDto.setDescription(consumer.getDescription());
        consumerDto.setNecessary(consumer.getNecessary());
        consumerDto.setPrice(consumer.getPrice());
        consumerDto.setRemarks(consumer.getRemarks());
        return consumerDto;
    }

    private Consumer _of(ConsumerDto consumerDto){
        Consumer consumer = new Consumer();
        consumer.setId(UUID.randomUUID().toString().replaceAll("-",""));
        consumer.setCategory(consumerDto.getCategory());
        consumer.setConsumerName(consumerDto.getConsumerName());
        consumer.setCreateTime(new Date());
        consumer.setDescription(consumerDto.getDescription());
        consumer.setPrice(consumerDto.getPrice());
        consumer.setNecessary(consumerDto.getNecessary());
        consumer.setRemarks(consumerDto.getRemarks());
        consumer.setUpdateTime(new Date());
        return consumer;
    }
}
