package com.yeff.consumption.service.impl;


import com.yeff.consumption.dto.ConsumerDTO;
import com.yeff.consumption.exception.ConsumptionErrorCode;
import com.yeff.consumption.exception.ConsumptionExceptionFactory;
import com.yeff.consumption.model.Consumer;
import com.yeff.consumption.provider.ConsumerProvider;
import com.yeff.consumption.service.ConsumerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
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
    public void insertRecord(ConsumerDTO consumerDto) {
        Consumer consumer = _of(consumerDto);
        if (consumerProvider.insert(consumer) != 1) {
            logger.error("add consumption record error");
            throw ConsumptionExceptionFactory.create(ConsumptionErrorCode.ADD_RECORD_ERROR,consumerDto);
        }
    }

    @Override
    public List<ConsumerDTO> getRecordByName(String name) {
        List<Consumer> consumerList = consumerProvider.selectRecordsByName(name);
        if(CollectionUtils.isEmpty(consumerList)){
            logger.error("no such records about name: {}",name);
            throw ConsumptionExceptionFactory.create(ConsumptionErrorCode.NO_SUCH_NAME,name);
        }
        return _ofListConsumerDTO(consumerList);
    }

    @Override
    public List<ConsumerDTO> getRecordByTime(String date) {
        List<Consumer> consumers = consumerProvider.selectRecordsByDate(date);
        if(CollectionUtils.isEmpty(consumers)){
            logger.error("no records before that time: {}",date);
            throw ConsumptionExceptionFactory.create(ConsumptionErrorCode.NO_RECORD_BEFORE_THIS_DATE,date);
        }
        return _ofListConsumerDTO(consumers);
    }

    @Override
    public List<ConsumerDTO> getRecordByPeriod(String sDate, String eDate) {
        Assert.notNull(sDate, "start date can not be null");
        Assert.notNull(eDate, "end date can not be null");
        List<Consumer> consumers = consumerProvider.getRecordsByPeriod(sDate, eDate);

        if (CollectionUtils.isEmpty(consumers)) {
            logger.error("no records between start date: {} and end date: {}", sDate, eDate);
            throw ConsumptionExceptionFactory.create(ConsumptionErrorCode.NO_RECORDS_BETWEEN_PERIOD, sDate, eDate);
        }
        return _ofListConsumerDTO(consumers);
    }

    private ConsumerDTO _buildConsumerDto(Consumer consumer) {
        ConsumerDTO consumerDto = new ConsumerDTO();
        consumerDto.setCategory(consumer.getCategory());
        consumerDto.setDescription(consumer.getDescription());
        consumerDto.setNecessary(consumer.getNecessary());
        consumerDto.setPrice(consumer.getPrice());
        consumerDto.setRemarks(consumer.getRemarks());
        consumerDto.setConsumerName(consumer.getConsumerName());
        consumerDto.setCreateTime(consumer.getCreateTime());
        return consumerDto;
    }

    private Consumer _of(ConsumerDTO consumerDto){
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

    private  List<ConsumerDTO> _ofListConsumerDTO(List<Consumer> consumers){
        List<ConsumerDTO> consumerDtoList = new ArrayList<>();
        consumerDtoList = consumers.parallelStream().map(consumer -> {
            ConsumerDTO consumerDto = _buildConsumerDto(consumer);
            return consumerDto;
        }).collect(Collectors.toList());
        return consumerDtoList;
    }
}
