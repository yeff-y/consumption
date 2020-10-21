package com.yeff.consumption.controller;


import com.yeff.consumption.bean.CResponse;
import com.yeff.consumption.dto.ConsumerDto;
import com.yeff.consumption.exception.handler.BizException;
import com.yeff.consumption.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consumer")
public class ConsumerController {

    @Autowired
    private ConsumerService consumerService;

    @PostMapping("/add/record")
    public CResponse<ConsumerDto> addConsumeRecords(@RequestBody ConsumerDto consumerDto){
        if(consumerDto.getConsumerName()== null){
            throw new BizException("-1","用户姓名不能为空！");
        }
        consumerService.insertRecord(consumerDto);
        return CResponse.successT(consumerDto);
    }

    @GetMapping("/query/consumerName")
    public CResponse<List<ConsumerDto>> getOneRecordByConsumerName(@RequestParam("consumerName") String consumerName){
        List<ConsumerDto> consumerDtoList = consumerService.getRecordByName(consumerName);
        return CResponse.successT(consumerDtoList);
    }
}
