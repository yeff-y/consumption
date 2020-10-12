package com.yeff.consumption.provider;

import com.yeff.consumption.dto.ConsumerDto;
import com.yeff.consumption.model.Consumer;

import java.util.List;

public interface ConsumerProvider {

    public int insert(Consumer consumer);

    public List<Consumer> selectRecords(String name);
}
