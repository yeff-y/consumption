package com.yeff.consumption.controller;


import com.yeff.consumption.bean.CResponse;
import com.yeff.consumption.dto.ConsumerDTO;
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
    public CResponse<ConsumerDTO> addConsumeRecords(@RequestBody ConsumerDTO consumerDto){
        if(consumerDto.getConsumerName()== null){
            throw new BizException("-1","用户姓名不能为空！");
        }
        consumerService.insertRecord(consumerDto);
        return CResponse.successT(consumerDto);
    }

    @GetMapping("/name")
    public CResponse<List<ConsumerDTO>> getRecordsByConsumerName(@RequestParam("name") String name){
        List<ConsumerDTO> consumerDtoList = consumerService.getRecordByName(name);
        return CResponse.successT(consumerDtoList);
    }

    @GetMapping("/date")
    public CResponse<List<ConsumerDTO>> getRecordsByTime(@RequestParam("date") String date){
        List<ConsumerDTO> consumerDtoList = consumerService.getRecordByTime(date);
        return CResponse.successT(consumerDtoList);
    }

    @GetMapping("/start/{sDate}/end/{eDate}")
    public CResponse<List<ConsumerDTO>> getRecordsByPeriod(@PathVariable("sDate") String sDate,
                                                           @PathVariable("eDate")String eDate){
        List<ConsumerDTO> consumerDtoList = consumerService.getRecordByPeriod(sDate,eDate);
        return CResponse.successT(consumerDtoList);
    }
}
