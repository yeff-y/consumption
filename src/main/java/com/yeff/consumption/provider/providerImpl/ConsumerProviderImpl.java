package com.yeff.consumption.provider.providerImpl;

import com.yeff.consumption.dto.ConsumerDto;
import com.yeff.consumption.mappers.ConsumerMapper;
import com.yeff.consumption.model.Consumer;
import com.yeff.consumption.provider.ConsumerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
@Repository
public class ConsumerProviderImpl implements ConsumerProvider {

    @Autowired
    private ConsumerMapper consumerMapper;


    @Override
    public int insert(Consumer consumer) {
        return consumerMapper.insert(consumer);
    }

    @Override
    public List<Consumer> selectRecords(String name) {
        List<Consumer> consumerList = consumerMapper.selectByName(name);
        return consumerList;
    }
}
