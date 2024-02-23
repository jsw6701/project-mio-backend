/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 *
 * Author : Seungwoo Jung
 * Create Date : 2024. 02. 23.
 * File Name : MsgServiceTest.java
 * DESC : 
 *****************************************************************/
package com.gdsc.projectmiobackend;

import com.gdsc.projectmiobackend.discord.MsgService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class MsgServiceTest {

    @Autowired
    private MsgService msgService;

    @Test
    public void 디스코드_메시지_테스트() throws Exception{
        // given
        boolean result = msgService.sendMsg("api/search", "해당 유저가 존재하지 않습니다.", "404 Not Found");
        // then
        Assertions.assertEquals(result,true);
    }

}