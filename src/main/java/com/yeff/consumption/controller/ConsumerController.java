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

    @GetMapping("/name")
    public CResponse<List<ConsumerDto>> getRecordsByConsumerName(@RequestParam("name") String name){
        List<ConsumerDto> consumerDtoList = consumerService.getRecordByName(name);
        return CResponse.successT(consumerDtoList);
    }

    @GetMapping("/date")
    public CResponse<List<ConsumerDto>> getRecordsByTime(@RequestParam("date") String date){
        List<ConsumerDto> consumerDtoList = consumerService.getRecordByTime(date);
        return CResponse.successT(consumerDtoList);
    }

    @GetMapping("/start/{sDate}/end/{eDate}")
    public CResponse<List<ConsumerDto>> getRecordsByPeriod(@PathVariable("sDate") String sDate,
                                                           @PathVariable("eDate")String eDate){
        List<ConsumerDto> consumerDtoList = consumerService.getRecordByPeriod(sDate,eDate);
        return CResponse.successT(consumerDtoList);
    }
}
