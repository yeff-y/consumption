package com.yeff.consumption.service.impl;


import com.yeff.consumption.dto.ConsumerDto;
import com.yeff.consumption.exception.ConsumptionErrorCode;
import com.yeff.consumption.exception.ConsumptionExceptionFactory;
import com.yeff.consumption.model.Consumer;
import com.yeff.consumption.provider.ConsumerProvider;
import com.yeff.consumption.service.ConsumerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


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
        if (consumerProvider.insert(consumer) != 1) {
            logger.error("add consumption record error");
            throw ConsumptionExceptionFactory.create(ConsumptionErrorCode.ADD_RECORD_ERROR,consumerDto);
        }
    }

    @Override
    public List<ConsumerDto> getRecordByName(String name) {
        List<Consumer> consumerList = consumerProvider.selectRecordsByName(name);
        if(CollectionUtils.isEmpty(consumerList)){
            logger.error("no such records about name: {}",name);
            throw ConsumptionExceptionFactory.create(ConsumptionErrorCode.NO_SUCH_NAME,name);
        }
        List<ConsumerDto> consumerDtoList = new ArrayList<>();
        consumerDtoList = consumerList.parallelStream().map(consumer -> {
            ConsumerDto consumerDto = _buildConsumerDto(consumer);
            return consumerDto;
        }).collect(Collectors.toList());
        return consumerDtoList;
    }

    @Override
    public List<ConsumerDto> getRecordByTime(String date) {
        List<Consumer> consumers = consumerProvider.selectRecordsByDate(date);
        if(CollectionUtils.isEmpty(consumers)){
            logger.error("no such records about name: {}",date);
            throw ConsumptionExceptionFactory.create(ConsumptionErrorCode.NO_RECORD_BEFORE_THIS_DATE,date);
        }
        List<ConsumerDto> consumerDtoList = new ArrayList<>();
        consumerDtoList = consumers.parallelStream().map(consumer -> {
            ConsumerDto consumerDto = _buildConsumerDto(consumer);
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
        consumerDto.setConsumerName(consumer.getConsumerName());
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
