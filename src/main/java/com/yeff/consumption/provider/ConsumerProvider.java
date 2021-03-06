package com.yeff.consumption.provider;

import com.yeff.consumption.model.Consumer;

import java.util.List;

public interface ConsumerProvider {

    public int insert(Consumer consumer);

    public List<Consumer> selectRecordsByName(String name);

    public List<Consumer> selectRecordsByDate(String date);

    public List<Consumer> getRecordsByPeriod(String sDate,String eDate);
}
