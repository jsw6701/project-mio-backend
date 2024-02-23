/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 *
 * Author : Seungwoo Jung
 * Create Date : 2024. 02. 23.
 * File Name : MsgService.java
 * DESC : 
 *****************************************************************/
package com.gdsc.projectmiobackend.discord;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MsgService {
    @Value("${discord.webhook.url}")
    String webhookUrl;

    public boolean sendMsg(String title, String description, String msg){

        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Content-Type", "application/json; utf-8");

            Embed embed = new Embed();
            embed.setTitle(title);
            embed.setDescription(description);
            ZonedDateTime now = ZonedDateTime.now();
            String formattedNow = now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            embed.setTimestamp(formattedNow);
            List<Embed> embeds = new ArrayList<>();
            embeds.add(embed);

            Message message = new Message(msg, embeds);

            HttpEntity<Message> messageEntity = new HttpEntity<>(message, httpHeaders);
            RestTemplate template = new RestTemplate();
            ResponseEntity<String> response = template.exchange(
                    webhookUrl,
                    HttpMethod.POST,
                    messageEntity,
                    String.class
            );


            // response에 대한 처리
            if(response.getStatusCode().value() != HttpStatus.NO_CONTENT.value()){
                log.error("메시지 전송 이후 에러 발생");
                return false;
            }

        } catch (Exception e) {
            log.error("에러 발생 :: " + e);
            return false;
        }

        return true;
    }
}