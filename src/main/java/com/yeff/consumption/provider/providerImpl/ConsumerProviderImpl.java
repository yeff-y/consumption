package com.yeff.consumption.provider.providerImpl;

import com.yeff.consumption.exception.ConsumptionErrorCode;
import com.yeff.consumption.exception.ConsumptionExceptionFactory;
import com.yeff.consumption.mappers.ConsumerMapper;
import com.yeff.consumption.model.Consumer;
import com.yeff.consumption.model.ConsumerExample;
import com.yeff.consumption.provider.ConsumerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
@Repository
public class ConsumerProviderImpl implements ConsumerProvider {

    private final Logger logger = LoggerFactory.getLogger(ConsumerProviderImpl.class);

    @Autowired
    private ConsumerMapper consumerMapper;


    @Override
    public int insert(Consumer consumer) {
        return consumerMapper.insert(consumer);
    }

    @Override
    public List<Consumer> selectRecordsByName(String name) {
        Consumer consumer = new Consumer();
        consumer.setConsumerName(name);
        ConsumerExample example = _buildExample(consumer);
        List<Consumer> consumerList = consumerMapper.selectByExample(example);
        return consumerList;
    }

    @Override
    public List<Consumer> selectRecordsByDate(String date) {
        Consumer consumer = new Consumer();
        Date selectDate = _getDate(date);
        consumer.setCreateTime(selectDate);
        ConsumerExample example = _buildExample(consumer);
        List<Consumer> consumerList = consumerMapper.selectByExample(example);
        return consumerList;
    }

    @Override
    public List<Consumer> getRecordsByPeriod(String sDate, String eDate) {
        Date startDate = _getDate(sDate);
        Date endDate = _getDate(eDate);
        ConsumerExample example = _constructPeriodExample(startDate, endDate);
        return consumerMapper.selectByExample(example);

    }

    private ConsumerExample _constructPeriodExample(Date startDate, Date endDate) {
        ConsumerExample example = new ConsumerExample();
        ConsumerExample.Criteria criteria = example.createCriteria();

        if(startDate != null && endDate != null){
            criteria.andCreateTimeBetween(startDate,endDate);
        }
        return example;
    }

    private Date _getDate(String date) {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        String time = date;
        Date selectDate = null;
        try {
            selectDate = ft.parse(time);
        } catch (ParseException e) {
            logger.error("string date type convert to Date error,message: {}",e.getMessage());
            throw ConsumptionExceptionFactory.create(ConsumptionErrorCode.STRING_DATE_TYPE_PARSE_ERROR,date);
        }
        return selectDate;
    }

    private ConsumerExample _buildExample(Consumer consumer) {
        ConsumerExample example = new ConsumerExample();
        ConsumerExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(consumer.getConsumerName())) {
            criteria.andConsumerNameEqualTo(consumer.getConsumerName());
        }
        if (!StringUtils.isEmpty(consumer.getId())) {
            criteria.andIdEqualTo(consumer.getId());
        }
        if (!StringUtils.isEmpty(consumer.getCategory())) {
            criteria.andCategoryEqualTo(consumer.getCategory());
        }
        if (!StringUtils.isEmpty(consumer.getNecessary())) {
            criteria.andNecessaryEqualTo(consumer.getNecessary());
        }
        if (!StringUtils.isEmpty(consumer.getCreateTime())) {
            criteria.andCreateTimeLessThanOrEqualTo(consumer.getCreateTime());
        }
        return example;
    }
}
